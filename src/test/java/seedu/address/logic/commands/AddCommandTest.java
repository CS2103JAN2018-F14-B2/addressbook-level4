package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicateNricException;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.petpatient.PetPatient;
import seedu.address.model.petpatient.PetPatientName;
import seedu.address.model.petpatient.exceptions.DuplicatePetPatientException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PetPatientBuilder;
import seedu.address.testutil.TypicalAppointments;
import seedu.address.testutil.TypicalPersons;
import seedu.address.testutil.TypicalPetPatients;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String messageAddpetpatient = "New pet patient added: %1$s \nunder owner: %2$s";
    private final String messageAddappointment = "New appointment made: %1$s\nunder owner: %2$s\nfor pet patient: %3$s";
    private final String messageAddall = "New person added: %1$s\nNew pet patient added: %2$s\n"
            + "New appointment made: %3$s";

    @Test
    public void constructor_nullPersonPetPatientAppointment_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand((Person) null, (PetPatient) null, (Appointment) null);
    }

    @Test
    public void constructor_nullAppointmentNricPetPatientName_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand((Appointment) null, (Nric) null, (PetPatientName) null);
    }

    @Test
    public void constructor_nullPetPatientNric_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand((PetPatient) null, (Nric) null);
    }

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand((Person) null);
    }

    @Test
    public void execute_objectsAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingAllAdded modelStub = new ModelStubAcceptingAllAdded();

        //add a new person (a)
        Person validPerson = new PersonBuilder().build();
        CommandResult resultToAddPerson = getAddCommandForPerson(validPerson, modelStub).execute();
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), resultToAddPerson.feedbackToUser);
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);

        //add a new pet patient (b) under person (a)
        PetPatient validPetPatient = new PetPatientBuilder().build();
        CommandResult resultToAddPetPatient = getAddCommandForPetPatient(validPetPatient, validPerson.getNric(),
                modelStub).execute();
        assertEquals(String.format(messageAddpetpatient, validPetPatient, validPerson),
                resultToAddPetPatient.feedbackToUser);
        assertEquals(Arrays.asList(validPetPatient), modelStub.petPatientsAdded);

        //add a new appt for pet patient (b) under person (a)
        Appointment validAppointment = new AppointmentBuilder().build();
        CommandResult resultToAddAppointment = getAddCommandForAppointment(validAppointment, validPerson.getNric(),
                validPetPatient.getName(), modelStub).execute();
        assertEquals(String.format(messageAddappointment, validAppointment, validPerson, validPetPatient),
                resultToAddAppointment.feedbackToUser);
        assertEquals(Arrays.asList(validAppointment), modelStub.appointmentsAdded);

        //add new person, new pet patient and new appointment
        Person newPerson = TypicalPersons.BENSON;
        PetPatient newPetPatient = TypicalPetPatients.JEWEL;
        Appointment newAppt = TypicalAppointments.BOB_APP;
        CommandResult resultToAddAll = getAddCommandForNewPersonPetPatientAppointment(newPerson, newPetPatient, newAppt,
                modelStub).execute();
        assertEquals(String.format(messageAddall, newPerson, newPetPatient, newAppt), resultToAddAll.feedbackToUser);

    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Person validPerson = new PersonBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validPerson, modelStub).execute();
    }

    @Test
    public void execute_duplicateNric_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateNricException();
        Person validPerson = new PersonBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_NRIC);

        getAddCommandForPerson(validPerson, modelStub).execute();
    }

    @Test
    public void execute_duplicatePetPatient_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePetPatientException();
        Person validPerson = new PersonBuilder().build();
        PetPatient validPetPatient = new PetPatientBuilder().build();
        Appointment validAppointment = new AppointmentBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PET_PATIENT);

        getAddCommandForNewPersonPetPatientAppointment(validPerson, validPetPatient, validAppointment,
                modelStub).execute();
    }

    @Test
    public void execute_duplicateAppointment_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateAppointmentException();
        Person validPerson = new PersonBuilder().build();
        PetPatient validPetPatient = new PetPatientBuilder().build();
        Appointment validAppointment = new AppointmentBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_APPOINTMENT);

        getAddCommandForNewPersonPetPatientAppointment(validPerson, validPetPatient, validAppointment,
                modelStub).execute();
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given person, pet patient and appointment.
     */
    private AddCommand getAddCommandForNewPersonPetPatientAppointment(Person person, PetPatient petPatient,
                                                                      Appointment appt, Model model) {
        AddCommand command = new AddCommand(person, petPatient, appt);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Generates a new AddCommand with the details of the given appointment, owner's nric and pet patient name.
     */
    private AddCommand getAddCommandForAppointment(Appointment appt, Nric ownerNric, PetPatientName petPatientName,
                                                   Model model) {
        AddCommand command = new AddCommand(appt, ownerNric, petPatientName);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Generates a new AddCommand with the details of the given pet patient and owner's nric.
     */
    private AddCommand getAddCommandForPetPatient(PetPatient petPatient, Nric ownerNric, Model model) {
        AddCommand command = new AddCommand(petPatient, ownerNric);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Generates a new AddCommand with the details of the given person.
     */
    private AddCommand getAddCommandForPerson(Person person, Model model) {
        AddCommand command = new AddCommand(person);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException, DuplicateNricException {
            fail("This method should not be called.");
        }

        @Override
        public void addPetPatient(PetPatient petPatient) throws DuplicatePetPatientException {
            fail("This method should not be called.");
        }

        @Override
        public void deletePetPatient(PetPatient target) {
            fail("This method should not be called.");
        }

        @Override
        public List<PetPatient> deletePetPatientDependencies(Person key) {
            return null;
        }

        @Override
        public List<Appointment> deleteAppointmentDependencies(PetPatient target) {
            return null;
        }

        @Override
        public void addAppointment(Appointment appointment) throws DuplicateAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteAppointment(Appointment target) {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Appointment> getFilteredAppointmentList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<PetPatient> getFilteredPetPatientList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPetPatientList(Predicate<PetPatient> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to add a person.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            throw new DuplicatePersonException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always throw a DuplicateNricException when trying to add a person.
     */
    private class ModelStubThrowingDuplicateNricException extends ModelStub {
        @Override
        public void addPerson(Person person) throws DuplicateNricException {
            throw new DuplicateNricException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always throw a DuplicatePetPatientException when trying to add a pet patient.
     */
    private class ModelStubThrowingDuplicatePetPatientException extends ModelStub {
        @Override
        public void addPerson(Person person) {
            //do nothing
        }

        @Override
        public void addPetPatient(PetPatient petPatient) throws DuplicatePetPatientException {
            throw new DuplicatePetPatientException();
        }

        @Override
        public void addAppointment(Appointment appt) {
            //do nothing
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always throw a DuplicateAppointmentException when trying to add an appointment.
     */
    private class ModelStubThrowingDuplicateAppointmentException extends ModelStub {
        @Override
        public void addPerson(Person person) {
            //do nothing
        }

        @Override
        public void addPetPatient(PetPatient petPatient) {
            //also do nothing
        }

        @Override
        public void addAppointment(Appointment appt) throws DuplicateAppointmentException {
            throw new DuplicateAppointmentException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the persons, pet patients and appointments being added.
     */
    private class ModelStubAcceptingAllAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();
        final ArrayList<PetPatient> petPatientsAdded = new ArrayList<>();
        final ArrayList<Appointment> appointmentsAdded = new ArrayList<>();

        private AddressBook addressBook = new AddressBook();

        @Override
        public void addPerson(Person person) throws DuplicatePersonException, DuplicateNricException {
            requireNonNull(person);
            addressBook.addPerson(person);
            personsAdded.add(person);
        }

        @Override
        public void addPetPatient(PetPatient petPatient) throws DuplicatePetPatientException {
            requireNonNull(petPatient);
            addressBook.addPetPatient(petPatient);
            petPatientsAdded.add(petPatient);
        }

        @Override
        public void addAppointment(Appointment appt) throws DuplicateAppointmentException, DuplicateDateTimeException {
            requireNonNull(appt);
            addressBook.addAppointment(appt);
            appointmentsAdded.add(appt);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }
    }
}
