# aquarinte
###### \java\guitests\guihandles\CommandBoxHandle.java
``` java
    /**
     * Types text into the command box and sets caret at the end of text.
     * Overwrites previous input.
     */
    public void setText(String input) {
        guiRobot.interact(() -> getRootNode().setText(input));
        guiRobot.interact(() -> getRootNode().positionCaret(getInput().length()));
    }

    /**
     * Inserts text into command box.
     */
    public void insertText(String input) {
        int caretPos = getRootNode().getCaretPosition();
        guiRobot.interact(() -> getRootNode().insertText(caretPos, input));
        guiRobot.interact(() -> getRootNode().positionCaret(caretPos + input.length()));
    }

    /**
     * Sets the caret position in the command box to {@code index}.
     */
    public void setCaretPosition(int index) {
        guiRobot.interact(() -> getRootNode().positionCaret(index));
    }

    /**
     * Removes change listener for autocomplete, so that it will not interfere with JUnit System Tests.
     */
    public void disableAutocomplete() {
        getRootNode().textProperty().removeListener(CommandBox.getAutocompleteListener());
    }
}
```
###### \java\seedu\address\logic\commands\AddCommandIntegrationTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() throws Exception {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() throws Exception {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(prepareCommand(validPerson, model), model,
                String.format(AddCommand.MESSAGE_SUCCESS_PERSON, validPerson), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(prepareCommand(personInList, model), model, AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

```
###### \java\seedu\address\logic\commands\AddCommandIntegrationTest.java
``` java
    @Test
    public void execute_duplicateNric_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        Nric nric = model.getAddressBook().getPersonList().get(0).getNric();
        Person duplicateNric = new PersonBuilder().withName("Red").withPhone("90002134").withEmail("red@gmail.com")
                .withAddress("24 Pallet Town").withNric(nric.toString()).build();
        assertCommandFailure(prepareCommand(duplicateNric, model), model, AddCommand.MESSAGE_DUPLICATE_NRIC);
    }

    @Test
    public void execute_newPetPatient_success() throws Exception {
        Person validPerson = model.getAddressBook().getPersonList().get(0);
        PetPatient validPetPatient = new PetPatientBuilder().withOwnerNric(validPerson.getNric().toString()).build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPetPatient(validPetPatient);

        assertCommandSuccess(prepareCommand(validPetPatient, validPerson.getNric(), model), model,
                String.format(AddCommand.MESSAGE_SUCCESS_PETPATIENT, validPetPatient, validPerson), expectedModel);
    }

    @Test
    public void execute_duplicatePetPatient_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        PetPatient duplicate = model.getAddressBook().getPetPatientList().get(0);
        assertCommandFailure(prepareCommand(duplicate, duplicate.getOwner(), model),
                model, AddCommand.MESSAGE_DUPLICATE_PET_PATIENT);
    }

    @Test
    public void execute_newPetPatientWithNricDoesNotExist_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        // Nric does not exists in address book
        String nricDoesNotExist = VALID_NRIC_DION;
        PetPatient validPetPatient = new PetPatientBuilder().withOwnerNric(nricDoesNotExist).build();

        assertCommandFailure(prepareCommand(validPetPatient, validPetPatient.getOwner(), model),
                model, AddCommand.MESSAGE_INVALID_NRIC);
    }

    @Test
    public void execute_newAppointment_success() throws Exception {
        PetPatient existing = model.getAddressBook().getPetPatientList().get(0);
        Person owner = model.getPersonWithNric(existing.getOwner());
        Appointment appt = new AppointmentBuilder().withOwnerNric(owner.getNric().toString())
                .withPetPatientName(existing.getName().toString()).build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addAppointment(appt);

        assertCommandSuccess(prepareCommand(appt, existing.getOwner(), existing.getName(), model), model,
                String.format(AddCommand.MESSAGE_SUCCESS_APPOINTMENT, appt, owner, existing), expectedModel);
    }

    @Test
    public void execute_newAppointmentWithNricDoesNotExists_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        String nricDoesNotExist = VALID_NRIC_DION;
        String petPatientNameExists = model.getAddressBook().getPetPatientList().get(0).getName().toString();
        Appointment validAppt = new AppointmentBuilder().withOwnerNric(nricDoesNotExist)
                .withPetPatientName(petPatientNameExists).build();

        assertCommandFailure(prepareCommand(validAppt, validAppt.getOwnerNric(), validAppt.getPetPatientName(), model),
                model, AddCommand.MESSAGE_INVALID_NRIC);
    }

    @Test
    public void execute_newAppointmentWithPetPatientNameDoesNotExists_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        String petNameDoesNotExist = VALID_NAME_NERO;
        String ownerNricExists = model.getAddressBook().getPersonList().get(0).getNric().toString();
        Appointment validAppt = new AppointmentBuilder().withOwnerNric(ownerNricExists)
                .withPetPatientName(petNameDoesNotExist).build();

        assertCommandFailure(prepareCommand(validAppt, validAppt.getOwnerNric(), validAppt.getPetPatientName(), model),
                model, AddCommand.MESSAGE_INVALID_PET_PATIENT);
    }

    @Test
    public void execute_duplicateAppointment_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        Appointment duplicate = model.getAddressBook().getAppointmentList().get(0);
        assertCommandFailure(prepareCommand(duplicate, duplicate.getOwnerNric(), duplicate.getPetPatientName(), model),
                model, AddCommand.MESSAGE_DUPLICATE_APPOINTMENT);
    }

    @Test
    public void execute_duplicateDateTimeAppointment_throwsCommandException()
        throws ConcurrentAppointmentException, PastAppointmentException {
        PetPatient existing = model.getAddressBook().getPetPatientList().get(0);
        Person owner = model.getPersonWithNric(existing.getOwner());
        String dupDateTime = model.getAddressBook().getAppointmentList().get(0).getFormattedLocalDateTime();

        Appointment appt = new AppointmentBuilder().withRemark(VALID_REMARK_ONE).withDateTime(dupDateTime)
                .withAppointmentTags(VALID_TAG_CHECKUP).withOwnerNric(owner.getNric().toString())
                .withPetPatientName(existing.getName().toString()).build();

        assertCommandFailure(prepareCommand(appt, owner.getNric(), existing.getName(), model),
                model, AddCommand.MESSAGE_DUPLICATE_DATETIME);
    }

    @Test
    public void execute_addAllNew_success() throws Exception {
        Person newPerson = new PersonBuilder().withName(VALID_NAME_CHARLIE).withPhone(VALID_PHONE_CHARLIE)
                .withEmail(VALID_EMAIL_CHARLIE).withAddress(VALID_ADDRESS_CHARLIE).withNric(VALID_NRIC_CHARLIE).build();

        PetPatient newPetPatient = new PetPatientBuilder().withName(VALID_NAME_NERO).withSpecies(VALID_SPECIES_NERO)
                .withBreed(VALID_BREED_NERO).withColour(VALID_COLOUR_NERO).withBloodType(VALID_BLOODTYPE_NERO)
                .withOwnerNric(newPerson.getNric().toString()).build();

        Appointment newAppt = new AppointmentBuilder().withDateTime(VALID_DATE_ONE).withRemark(VALID_REMARK_ONE)
                .withAppointmentTags(VALID_TAG_CHECKUP).withOwnerNric(newPerson.getNric().toString())
                .withPetPatientName(newPetPatient.getName().toString()).build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(newPerson);
        expectedModel.addPetPatient(newPetPatient);
        expectedModel.addAppointment(newAppt);

        assertCommandSuccess(prepareCommand(newPerson, newPetPatient, newAppt, model), model,
                String.format(AddCommand.MESSAGE_SUCCESS_EVERYTHING, newPerson, newPetPatient, newAppt), expectedModel);
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code person} into the {@code model}.
     */
    private AddCommand prepareCommand(Person person, Model model) {
        AddCommand command = new AddCommand(person);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code petpatient} into the {@code model}.
     */
    private AddCommand prepareCommand(PetPatient petPatient, Nric ownerNric, Model model) {
        AddCommand command = new AddCommand(petPatient, ownerNric);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code appointment} into the {@code model}.
     */
    private AddCommand prepareCommand(Appointment appt, Nric ownerNric, PetPatientName petPatientName, Model model) {
        AddCommand command = new AddCommand(appt, ownerNric, petPatientName);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code person}, {@code petpatient} and
     * {@code appointment} into the {@code model}.
     */
    private AddCommand prepareCommand(Person person, PetPatient petPatient, Appointment appt, Model model) {
        AddCommand command = new AddCommand(person, petPatient, appt);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS_PERSON, validPerson), resultToAddPerson.feedbackToUser);
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);

        //add a new pet patient (b) under person (a)
        PetPatient validPetPatient = new PetPatientBuilder().build();
        CommandResult resultToAddPetPatient = getAddCommandForPetPatient(validPetPatient, validPerson.getNric(),
                modelStub).execute();
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS_PETPATIENT, validPetPatient, validPerson),
                resultToAddPetPatient.feedbackToUser);
        assertEquals(Arrays.asList(validPetPatient), modelStub.petPatientsAdded);

        //add a new appt for pet patient (b) under person (a)
        Appointment validAppointment = new AppointmentBuilder().build();
        CommandResult resultToAddAppointment = getAddCommandForAppointment(validAppointment, validPerson.getNric(),
                validPetPatient.getName(), modelStub).execute();
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS_APPOINTMENT, validAppointment, validPerson,
                validPetPatient), resultToAddAppointment.feedbackToUser);
        assertEquals(Arrays.asList(validAppointment), modelStub.appointmentsAdded);

        //add new person, new pet patient and new appointment
        Person newPerson = TypicalPersons.BENSON;
        PetPatient newPetPatient = TypicalPetPatients.JEWEL;
        Appointment newAppt = TypicalAppointments.BENSON_APP;
        CommandResult resultToAddAll = getAddCommandForNewPersonPetPatientAppointment(newPerson, newPetPatient, newAppt,
                modelStub).execute();
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS_EVERYTHING, newPerson, newPetPatient, newAppt),
                resultToAddAll.feedbackToUser);
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
    public void execute_duplicateAppointmentDateTime_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateDateTimeException();
        Person validPerson = new PersonBuilder().build();
        PetPatient validPetPatient = new PetPatientBuilder().build();
        Appointment validAppointment = new AppointmentBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_DATETIME);

        getAddCommandForNewPersonPetPatientAppointment(validPerson, validPetPatient, validAppointment,
                modelStub).execute();
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").withNric("T0011223G").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        PetPatient joseph = new PetPatientBuilder().build();
        PetPatient tia = new PetPatientBuilder().withName("Tia").build();
        AddCommand addJosephCommand = new AddCommand(joseph, alice.getNric());
        AddCommand addTiaCommand = new AddCommand(tia, bob.getNric());

        Appointment appt = new AppointmentBuilder().build();
        Appointment appt2 = new AppointmentBuilder().withDateTime("2018-11-11 15:30").build();
        AddCommand addApptCommand = new AddCommand(appt, alice.getNric(), joseph.getName());
        AddCommand addAppt2Command = new AddCommand(appt2, bob.getNric(), tia.getName());

        AddCommand addAllCommand = new AddCommand(alice, joseph, appt);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));
        assertTrue(addJosephCommand.equals(addJosephCommand));
        assertTrue(addApptCommand.equals(addApptCommand));
        assertTrue(addAllCommand.equals(addAllCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        AddCommand addJosephCommandCopy = new AddCommand(joseph, alice.getNric());
        assertTrue(addJosephCommand.equals(addJosephCommandCopy));

        AddCommand addApptCommandCopy = new AddCommand(appt, alice.getNric(), joseph.getName());
        assertTrue(addApptCommand.equals(addApptCommandCopy));

        AddCommand addAllCommandCopy = new AddCommand(alice, joseph, appt);
        assertTrue(addAllCommand.equals(addAllCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));
        assertFalse(addJosephCommand.equals(2));
        assertFalse(addApptCommand.equals(new AppointmentBuilder().build()));
        assertFalse(addAllCommand.equals("hello"));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));
        assertFalse(addJosephCommand.equals(null));
        assertFalse(addApptCommand.equals(null));
        assertFalse(addAllCommand.equals(null));

        // different person/pet patient/appointment -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
        assertFalse(addJosephCommand.equals(addTiaCommand));
        assertFalse(addApptCommand.equals(addAppt2Command));
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
        public Person getPersonWithNric(Nric nric) {
            return null;
        }

        @Override
        public void addPetPatient(PetPatient petPatient) throws DuplicatePetPatientException {
            fail("This method should not be called.");
        }

        @Override
        public PetPatient getPetPatientWithNricAndName(Nric nric, PetPatientName petPatientName) {
            return null;
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
        public void addAppointment(Appointment appointment) throws DuplicateAppointmentException,
                DuplicateDateTimeException {
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
        public void updatePetPatient(PetPatient target, PetPatient editedPetPatient)
                throws DuplicatePetPatientException {
            fail("This method should not be called.");
        }

        @Override
        public void updateAppointment(Appointment target, Appointment editedAppointment)
                throws DuplicateAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public ArrayList<PetPatient> getPetPatientsWithNric(Nric ownerNric) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ArrayList<Appointment> getAppointmentsWithNric(Nric ownerNric) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ArrayList<Appointment> getAppointmentsWithNricAndPetName(Nric ownerNric, PetPatientName petPatientName) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public Appointment getClashingAppointment(LocalDateTime dateTime) {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public boolean hasConcurrentAppointment(LocalDateTime oldDateTime, LocalDateTime newDateTime) {
            fail("This method should not be called.");
            return false;
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
            return null;
        }

        @Override
        public void updateFilteredPetPatientList(Predicate<PetPatient> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public List<Tag> getTagList() {
            return null;
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
            //do nothing
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
     * A Model stub that always throw a DuplicateDateTimeException when trying to add an appointment.
     */
    private class ModelStubThrowingDuplicateDateTimeException extends ModelStub {
        @Override
        public void addPerson(Person person) {
            //do nothing
        }

        @Override
        public void addPetPatient(PetPatient petPatient) {
            //do nothing
        }

        @Override
        public void addAppointment(Appointment appt) throws DuplicateDateTimeException {
            throw new DuplicateDateTimeException();
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

        @Override
        public void addPerson(Person person) throws DuplicatePersonException, DuplicateNricException {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public Person getPersonWithNric(Nric nric) {
            for (Person p : personsAdded) {
                if (p.getNric().equals(nric)) {
                    return p;
                }
            }
            return null;
        }

        @Override
        public void addPetPatient(PetPatient petPatient) throws DuplicatePetPatientException {
            requireNonNull(petPatient);
            petPatientsAdded.add(petPatient);
        }

        @Override
        public PetPatient getPetPatientWithNricAndName(Nric nric, PetPatientName petPatientName) {
            for (PetPatient p : petPatientsAdded) {
                if (p.getOwner().equals(nric) && p.getName().equals(petPatientName)) {
                    return p;
                }
            }
            return null;
        }

        @Override
        public void addAppointment(Appointment appt) throws DuplicateAppointmentException, DuplicateDateTimeException {
            requireNonNull(appt);
            appointmentsAdded.add(appt);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
```
###### \java\seedu\address\logic\commands\ChangeThemeCommandTest.java
``` java
public class ChangeThemeCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void constructor_nullTheme_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ChangeThemeCommand(null);
    }

    @Test
    public void execute_lightTheme_changeThemeSuccessful() {
        CommandResult result = new ChangeThemeCommand(TypicalThemes.LIGHT).execute();
        assertEquals("Current theme: light", result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ChangeThemeRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_darkTheme_changeThemeSuccessful() {
        CommandResult result = new ChangeThemeCommand(TypicalThemes.DARK).execute();
        assertEquals("Current theme: dark", result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ChangeThemeRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }
}
```
###### \java\seedu\address\logic\parser\AddCommandParserTest.java
``` java
    @Test
    public void parse_allPetPatientFieldsPresent_success() {

        PetPatient expectedPetPatient = new PetPatientBuilder().withName(VALID_NAME_JOKER)
                .withSpecies(VALID_SPECIES_JOKER).withBreed(VALID_BREED_JOKER).withColour(VALID_COLOUR_JOKER)
                .withBloodType(VALID_BLOODTYPE_JOKER).withTags(VALID_TAG_FIV).withOwnerNric(VALID_NRIC_FION).build();

        Nric fion = new Nric(VALID_NRIC_FION);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + TAG_DESC_FIV + OPTION_OWNER
                        + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));

        // multiple names - last name accepted
        assertParseSuccess(parser, OPTION_PET + NAME_DESC_NERO + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                        + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + TAG_DESC_FIV + OPTION_OWNER
                        + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));

        // multiple species - last species accepted
        assertParseSuccess(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_NERO + SPECIES_DESC_JOKER
                        + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + TAG_DESC_FIV + OPTION_OWNER
                        + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));

        // multiple breed - last breed accepted
        assertParseSuccess(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER + BREED_DESC_NERO
                        + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + TAG_DESC_FIV + OPTION_OWNER
                        + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));

        // multiple colour - last colour accepted
        assertParseSuccess(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER + BREED_DESC_JOKER
                        + COLOUR_DESC_NERO + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + TAG_DESC_FIV + OPTION_OWNER
                        + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));

        // multiple blood type - last blood type accepted
        assertParseSuccess(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER + BREED_DESC_JOKER
                        + COLOUR_DESC_JOKER + BLOODTYPE_DESC_NERO + BLOODTYPE_DESC_JOKER + TAG_DESC_FIV + OPTION_OWNER
                        + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));

        // multiple tags - all accepted
        assertParseSuccess(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER + BREED_DESC_JOKER
                + COLOUR_DESC_JOKER + BLOODTYPE_DESC_NERO + BLOODTYPE_DESC_JOKER + TAG_DESC_FIV + TAG_DESC_DEPRESSION
                + OPTION_OWNER + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));
    }

    @Test
    public void parse_optionalPetPatientFieldsMissing_success() {
        // zero tags
        PetPatient expectedPetPatient = new PetPatientBuilder().withName(VALID_NAME_JOKER)
                .withSpecies(VALID_SPECIES_JOKER).withBreed(VALID_BREED_JOKER).withColour(VALID_COLOUR_JOKER)
                .withBloodType(VALID_BLOODTYPE_JOKER).withOwnerNric(VALID_NRIC_FION).build();

        Nric fion = new Nric(VALID_NRIC_FION);

        assertParseSuccess(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, new AddCommand(expectedPetPatient, fion));
    }

    @Test
    public void parse_compulsoryPetPatientFieldMissing_failure() {
        String invalidPetPatient = String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_PETPATIENT);
        String invalidAddCommand = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        String missingNricPrefix = String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_MISSING_NRIC_PREFIX);

        // missing name prefix
        assertParseFailure(parser, OPTION_PET + VALID_NAME_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, invalidPetPatient);

        // missing species prefix
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + VALID_SPECIES_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, invalidPetPatient);

        // missing breed prefix
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + VALID_BREED_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, invalidPetPatient);

        // missing colour prefix
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + VALID_COLOUR_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, invalidPetPatient);

        // missing blood type prefix
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + VALID_BLOODTYPE_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, invalidPetPatient);

        // missing nric prefix
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + VALID_NRIC_FION, missingNricPrefix);

        // missing all pet patient prefixes
        assertParseFailure(parser, OPTION_PET + VALID_NAME_JOKER + VALID_SPECIES_JOKER
                + VALID_BREED_JOKER + VALID_COLOUR_JOKER + VALID_BLOODTYPE_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, invalidPetPatient);

        // missing all prefixes
        assertParseFailure(parser, OPTION_PET + VALID_NAME_JOKER + VALID_SPECIES_JOKER
                + VALID_BREED_JOKER + VALID_COLOUR_JOKER + VALID_BLOODTYPE_JOKER + OPTION_OWNER
                + VALID_NRIC_FION, invalidPetPatient);

        // missing options
        assertParseFailure(parser, NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + NRIC_DESC_FION,
                invalidAddCommand);
    }

    @Test
    public void parse_invalidPetPatientValue_failure() {
        // invalid name
        assertParseFailure(parser, OPTION_PET + INVALID_NAME_DESC + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, PetPatientName.MESSAGE_PET_NAME_CONSTRAINTS);

        // invalid nric
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + INVALID_NRIC_DESC, Nric.MESSAGE_NRIC_CONSTRAINTS);

        // invalid breed
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + INVALID_BREED_DESC + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, Breed.MESSAGE_PET_BREED_CONSTRAINTS);

        // invalid species
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + INVALID_SPECIES_DESC
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, Species.MESSAGE_PET_SPECIES_CONSTRAINTS);

        // invalid colour
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + INVALID_COLOUR_DESC + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, Colour.MESSAGE_PET_COLOUR_CONSTRAINTS);

        // invalid blood type
        assertParseFailure(parser, OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + INVALID_BLOODTYPE_DESC + OPTION_OWNER
                + NRIC_DESC_FION, BloodType.MESSAGE_PET_BLOODTYPE_CONSTRAINTS);
    }

    @Test
    public void parse_allAppointmentFieldsPresent_success() {
        Appointment appt = new AppointmentBuilder().withDateTime(VALID_DATE_ONE).withRemark(VALID_REMARK_ONE)
                .withAppointmentTags(VALID_TAG_CHECKUP).withOwnerNric(VALID_NRIC_FION)
                .withPetPatientName(VALID_NAME_JOKER).build();

        Nric fion = new Nric(VALID_NRIC_FION);
        PetPatientName joker = new PetPatientName(VALID_NAME_JOKER);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + OPTION_APPOINTMENT + DATE_DESC_ONE + REMARK_DESC_ONE
                + TAG_DESC_CHECKUP + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER,
                new AddCommand(appt, fion, joker));

        // multiple date time - last one is accepted
        assertParseSuccess(parser,  OPTION_APPOINTMENT + DATE_DESC_TWO + DATE_DESC_ONE + REMARK_DESC_ONE
                        + TAG_DESC_CHECKUP + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER,
                new AddCommand(appt, fion, joker));

        // multiple remarks - last one is accepted
        assertParseSuccess(parser,  OPTION_APPOINTMENT + DATE_DESC_ONE + REMARK_DESC_TWO + REMARK_DESC_ONE
                        + TAG_DESC_CHECKUP + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER,
                new AddCommand(appt, fion, joker));

        // multiple tags - all accepted
        assertParseSuccess(parser,  OPTION_APPOINTMENT + DATE_DESC_ONE + REMARK_DESC_ONE + TAG_DESC_VACCINATION
                        + TAG_DESC_CHECKUP + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER,
                new AddCommand(appt, fion, joker));

        // multiple nric - last one is accepted
        assertParseSuccess(parser,  OPTION_APPOINTMENT + DATE_DESC_ONE + REMARK_DESC_ONE
                        + TAG_DESC_CHECKUP + OPTION_OWNER + NRIC_DESC_CHARLIE + NRIC_DESC_FION + OPTION_PET
                        + NAME_DESC_JOKER, new AddCommand(appt, fion, joker));

        // multiple pet name - last one is accepted
        assertParseSuccess(parser,  OPTION_APPOINTMENT + DATE_DESC_ONE + REMARK_DESC_ONE
                + TAG_DESC_CHECKUP + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET
                + NAME_DESC_NERO + NAME_DESC_JOKER, new AddCommand(appt, fion, joker));
    }

    @Test
    public void parse_compulsoryAppointmentFieldMissing_failure() {
        String invalidAppt = String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_APPOINTMENT);
        String invalidCommand = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing date time prefix
        assertParseFailure(parser, OPTION_APPOINTMENT + VALID_DATE_ONE + REMARK_DESC_ONE + TAG_DESC_CHECKUP
                + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER, invalidAppt);

        // missing remark prefix
        assertParseFailure(parser, OPTION_APPOINTMENT + DATE_DESC_ONE + VALID_REMARK_ONE + TAG_DESC_CHECKUP
                + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER, invalidAppt);

        // missing tag prefix
        assertParseFailure(parser, OPTION_APPOINTMENT + DATE_DESC_ONE + REMARK_DESC_ONE + VALID_TAG_CHECKUP
                + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER, invalidAppt);

        // missing options
        assertParseFailure(parser, DATE_DESC_ONE + REMARK_DESC_ONE + TAG_DESC_CHECKUP + NRIC_DESC_FION
                + NAME_DESC_JOKER, invalidCommand);

        // missing -o, -p
        assertParseFailure(parser, OPTION_APPOINTMENT + VALID_DATE_ONE + REMARK_DESC_ONE + TAG_DESC_CHECKUP,
                invalidCommand);
    }

    @Test
    public void parse_invalidAppointmentValue_failure() {
        String invalidDateMsg = "Please give a valid date based on the format yyyy-MM-dd!";
        String invalidDateTimeMsg = "Please ensure all fields are valid and follow the format of yyyy-MM-dd HH:mm!";

        // invalid remark
        assertParseFailure(parser, OPTION_APPOINTMENT + DATE_DESC_ONE + INVALID_REMARK_DESC + TAG_DESC_CHECKUP
                + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER, Remark.MESSAGE_REMARK_CONSTRAINTS);

        // invalid date
        assertParseFailure(parser, OPTION_APPOINTMENT + INVALID_DATETIME_DESC + REMARK_DESC_ONE + TAG_DESC_CHECKUP
                + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER, invalidDateMsg);

        // invalid time
        assertParseFailure(parser, OPTION_APPOINTMENT + INVALID_TIME_DESC + REMARK_DESC_ONE + TAG_DESC_CHECKUP
                + OPTION_OWNER + NRIC_DESC_FION + OPTION_PET + NAME_DESC_JOKER, invalidDateTimeMsg);
    }

    @Test
    public void parse_invalidCommandOption_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // invalid option
        assertParseFailure(parser, INVALID_OPTION + NAME_DESC_JOKER + SPECIES_DESC_JOKER
                + BREED_DESC_JOKER + COLOUR_DESC_JOKER + BLOODTYPE_DESC_JOKER + OPTION_OWNER
                + NRIC_DESC_FION, expectedMessage);

        // wrong order: -a, -p, -o
        assertParseFailure(parser, OPTION_APPOINTMENT + DATE_DESC_ONE + REMARK_DESC_ONE + TAG_DESC_CHECKUP
                        + OPTION_PET + NAME_DESC_JOKER + SPECIES_DESC_JOKER + BREED_DESC_JOKER + COLOUR_DESC_JOKER
                        + BLOODTYPE_DESC_JOKER + OPTION_OWNER + NRIC_DESC_FION, expectedMessage);
    }

}
```
###### \java\seedu\address\logic\parser\ChangeThemeCommandParserTest.java
``` java
public class ChangeThemeCommandParserTest {
    private ChangeThemeCommandParser parser = new ChangeThemeCommandParser();

    @Test
    public void parse_invalidUsage() {
        //empty
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeThemeCommand.MESSAGE_USAGE));

        //more than 1 argument
        assertParseFailure(parser, "light dark", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeThemeCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "dark blue", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeThemeCommand.MESSAGE_USAGE));
    }
    @Test
    public void parse_invalidTheme() {

        //themes do not exist
        assertParseFailure(parser, INVALID_THEME_PINK, Theme.MESSAGE_THEME_CONSTRAINTS);
        assertParseFailure(parser, INVALID_THEME_LIGHTT, Theme.MESSAGE_THEME_CONSTRAINTS);
    }

    @Test
    public void parse_validTheme_caseInsensitive() {
        //LIGHT
        assertParseSuccess(parser, "LIGHT", new ChangeThemeCommand(LIGHT));

        //LIghT
        assertParseSuccess(parser, "LIghT", new ChangeThemeCommand(LIGHT));

        //DaRk
        assertParseSuccess(parser, "DaRk", new ChangeThemeCommand(DARK));

        //DARk
        assertParseSuccess(parser, "DARk", new ChangeThemeCommand(DARK));
    }
}
```
###### \java\seedu\address\testutil\PetPatientUtil.java
``` java
/**
 * A utility class for Pet Patient.
 */
public class PetPatientUtil {
    /**
     * Returns an add command string for adding the {@code petpatient}.
     */
    public static String getAddCommand(PetPatient petPatient, Nric ownerNric) {
        return AddCommand.COMMAND_WORD + OPTION_PET + " " + getPetPatientDetails(petPatient)
                + OPTION_OWNER + " " + PREFIX_NRIC + ownerNric.toString();
    }

    /**
     * Returns an add command string for adding the {@code petpatient}.
     */
    public static String getAddCommandAlias(PetPatient petPatient, Nric ownerNric) {
        return AddCommand.COMMAND_ALIAS + " " + OPTION_PET + " " + getPetPatientDetails(petPatient)
                + " " + OPTION_OWNER + " " + PREFIX_NRIC + ownerNric.toString();
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPetPatientDetails(PetPatient petPatient) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + petPatient.getName().fullName + " ");
        sb.append(PREFIX_SPECIES + petPatient.getSpecies().species + " ");
        sb.append(PREFIX_BREED + petPatient.getBreed().breed + " ");
        sb.append(PREFIX_COLOUR + petPatient.getColour().colour + " ");
        sb.append(PREFIX_BLOODTYPE + petPatient.getBloodType().bloodType + " ");
        petPatient.getTags().stream().forEach(s -> sb.append(PREFIX_TAG + s.tagName + " "));
        return sb.toString();
    }
}
```
###### \java\seedu\address\testutil\TypicalThemes.java
``` java
/**
 * A utility class containing a list of {@code Theme} objects to be used in tests.
 */
public class TypicalThemes {

    public static final Theme DARK = new Theme("dark");
    public static final Theme LIGHT = new Theme("light");
}
```
###### \java\seedu\address\ui\CommandBoxTest.java
``` java
    @Test
    public void commandBox_autocompleteCommandWord() {
        //add command
        testAutocompleteForUserInput("    ", -1, "a", 1, "    add");
        testAutocompleteForUserInput("a", -1, "d", 1, "add");

        //clear command
        testAutocompleteForUserInput("cl", -1, "e", 1, "clear");

        //delete command
        testAutocompleteForUserInput(" ", -1, "d", 1, " delete");

        //edit command
        testAutocompleteForUserInput("  ", -1, "e", 1, "  edit");
        testAutocompleteForUserInput("e", -1, "d", 1, "edit");

        //exit command
        testAutocompleteForUserInput(" ", -1, "e", 2, " exit");
        testAutocompleteForUserInput("e", -1, "x", 1, "exit");

        //help command
        testAutocompleteForUserInput("", -1, "he", 1, "help");
        testAutocompleteForUserInput("h", -1, "e", 1, "help");
        testAutocompleteForUserInput("he", -1, "l", 1, "help");

        //history command
        testAutocompleteForUserInput(" ", -1, "h", 2, " history");
        testAutocompleteForUserInput("h", -1, "i", 1, "history");
        testAutocompleteForUserInput("hi", -1, "s", 1, "history");

        //list command
        testAutocompleteForUserInput(" ", -1, "l", 1, " list");

        //listappt command
        testAutocompleteForUserInput(" ", -1, "l", 2, " listappt");

        //theme command
        testAutocompleteForUserInput("t", -1, "h", 1, "theme");

        //undo
        testAutocompleteForUserInput("u", -1, "n", 1, "undo");

        //redo
        testAutocompleteForUserInput("r", -1, "e", 1, "redo");
    }

    @Test
    public void commandBox_commandNoAutocompleteOptionAndPrefix() {
        testAutocompleteForUserInput("clear ", -1, "n", 1, "");
        testAutocompleteForUserInput("clear ", -1, "-", 1, "");
        testAutocompleteForUserInput("list ", -1, " n", 1, "");
        testAutocompleteForUserInput("list ", -1, "-", 1, "");
        testAutocompleteForUserInput("exit ", -1, "n", 1, "");
        testAutocompleteForUserInput("exit ", -1, "-", 1, "");
        testAutocompleteForUserInput("help ", -1, "n", 1, "");
        testAutocompleteForUserInput("help ", -1, "-", 1, "");
        testAutocompleteForUserInput("history ", -1, "-", 1, "");
        testAutocompleteForUserInput("undo ", -1, "-", 1, "");
        testAutocompleteForUserInput("redo ", -1, "-", 1, "");

    }

    @Test
    public void commandBox_autocompleteOption() {
        testAutocompleteForUserInput("delete ", -1, "-", 3, "delete -fa");
        testAutocompleteForUserInput("delete ", -1, "-", 4, "delete -fo");
        testAutocompleteForUserInput("delete ", -1, "-", 5, "delete -fp");
        testAutocompleteForUserInput("add ", -1, "-", 1, "add -a");
        testAutocompleteForUserInput("find ", -1, "-", 7, "find -o");
        testAutocompleteForUserInput("find ", -1, "-", 8, "find -p");
        testAutocompleteForUserInput("listappt ", -1, "-", 2,
                "listappt -d");
        testAutocompleteForUserInput("listappt ", -1, "-", 6,
                "listappt -m");
        testAutocompleteForUserInput("listappt ", -1, "-", 9,
                "listappt -w");
        testAutocompleteForUserInput("listappt ", -1, "-", 10,
                "listappt -y");
    }

    @Test
    public void commandBox_autocompletePrefix() {
        // prefix a/
        testAutocompleteForUserInput("add -o ", -1, "a", 1, "add -o a/");
        testAutocompleteForUserInput("add -o", -1, " ", 1, "add -o a/");

        // prefix b/
        testAutocompleteForUserInput("find -p ", -1, "b", 1,
                "find -p b/");

        // prefix bt/
        testAutocompleteForUserInput("add -p ", -1, "b", 2,
                "add -p bt/");

        // prefix c/
        testAutocompleteForUserInput("add -p ", -1, "c", 1,
                "add -p c/");

        // prefix d/
        testAutocompleteForUserInput("add -a ", -1, "d", 1,
                "add -a d/");

        // prefix n/
        testAutocompleteForUserInput("add -o ", -1, "n", 1,
                "add -o n/");

        // prefix nr/
        testAutocompleteForUserInput("add -o ", -1, "n", 2,
                "add -o nr/");
        testAutocompleteForUserInput("add -o ", -1, "nr", 1,
                "add -o nr/");

        // prefix r/
        testAutocompleteForUserInput("add -a ", -1, "r", 1,
                "add -a r/");

        // prefix s/
        testAutocompleteForUserInput("find -p ", -1, "s", 1,
                "find -p s/");

        // prefix t/
        testAutocompleteForUserInput("find -o ", -1, "t", 1,
                "find -o t/");
    }

    @Test
    public void commandBox_autocompleteNric() {
        // autocomplete suggestions for nric for add command that follows "-o nr/"
        testAutocompleteForUserInput("add -p -o ", -1, "nr/", 1,
                "add -p -o nr/F0184556R");
        testAutocompleteForUserInput("add -p -o ", -1, "nr/F018", 1,
                "add -p -o nr/F0184556R");
        testAutocompleteForUserInput("add -p -o ", -1, "nr/", 2,
                "add -p -o nr/F2345678U");
        testAutocompleteForUserInput("add -p -o ", -1, "nr/S", 1,
                "add -p -o nr/S0123456Q");

        // no nric autocomplete suggestions for nric if add command does not have "-o nr/"
        testAutocompleteForUserInput("add -p ", -1, "nr/S", 1,
                "add -p nr/S");
        testAutocompleteForUserInput("add -a ", -1, "nr/F", 1,
                "add -a nr/F");

        // autocomplete suggestions for nric for "edit -p" command
        testAutocompleteForUserInput("edit -p ", -1, "nr/", 1,
                "edit -p nr/F0184556R");
        testAutocompleteForUserInput("edit -p ", -1, "nr/F018", 1,
                "edit -p nr/F0184556R");
        testAutocompleteForUserInput("edit -p ", -1, "nr/", 2,
                "edit -p nr/F2345678U");
        testAutocompleteForUserInput("edit -p ", -1, "nr/S", 1,
                "edit -p nr/S0123456Q");

        // no nric autocomplete suggestions for nric if edit command does not start with "edit -p"
        testAutocompleteForUserInput("edit -o ", -1, "nr/", 1,
                "edit -o nr/");
        testAutocompleteForUserInput("edit -o ", -1, "nr/S", 1,
                "edit -o nr/S");

        // autocomplete suggestions for nric for "find -o" command
        testAutocompleteForUserInput("find -o ", -1, "nr/", 1,
                "find -o nr/F0184556R");
        testAutocompleteForUserInput("find -o ", -1, "nr/", 3,
                "find -o nr/G1111111B");
        testAutocompleteForUserInput("find -o ", -1, "nr/T", 3,
                "find -o nr/T0120956W");
        testAutocompleteForUserInput("find -o ", -1, "nr/T0", 2,
                "find -o nr/T0123456L");

        // no nric autocomplete suggestion if edit command does not start with "find -o"
        testAutocompleteForUserInput("find -p ", -1, "nr/T0", 2,
                "find -p nr/T0");
        testAutocompleteForUserInput("find -a ", -1, "nr/S", 2,
                "find -a nr/S");
    }

    @Test
    public void commandBox_autocompletePetPatientName() {
        testAutocompleteForUserInput("add -a -o -p", -1, " n/", 1,
                "add -a -o -p n/Jenn");
        testAutocompleteForUserInput("add -a -o -p", -1, " n/", 3,
                "add -a -o -p n/Joker");

        // autocomplete will not work
        testAutocompleteForUserInput("edit -p", -1, " n/", 1,
                "edit -p n/");
        testAutocompleteForUserInput("find -p", -1, " n/", 2,
                "find -p n/");
    }

    @Test
    public void commandBox_autocompleteSpecies() {
        testAutocompleteForUserInput("add -p ", -1, "s/C", 1,
                "add -p s/Cat");
        testAutocompleteForUserInput("add -p ", -1, "s/d", 1,
                "add -p s/Dog");
    }

    @Test
    public void commandBox_autocompleteBreed() {
        testAutocompleteForUserInput("add -p ", -1, "b/P", 1,
                "add -p b/Persian Ragdoll");
        testAutocompleteForUserInput("find -p ", -1, "b/G", 1,
                "find -p b/Golden Retriever");
    }

    @Test
    public void commandBox_autocompleteColour() {
        testAutocompleteForUserInput("find -p ", -1, "c/c", 1,
                "find -p c/calico");
        testAutocompleteForUserInput("find -p ", -1, "c/g", 1,
                "find -p c/golden");
    }

    @Test
    public void commandBox_autocompleteBloodType() {
        testAutocompleteForUserInput("find -p ", -1, "bt/", 1,
                "find -p bt/A");
        testAutocompleteForUserInput("find -p ", -1, "bt/D", 1,
                "find -p bt/DEA 4+");
    }

    @Test
    public void commandBox_autocompleteTag() {
        // person tags
        testAutocompleteForUserInput("add -o ", -1, "t/", 2,
                "add -o t/owesMoney");
        testAutocompleteForUserInput("add -o ", -1, "t/F", 1,
                "add -o t/friends");

        // pet patient tags
        testAutocompleteForUserInput("add -p ", -1, "t/d", 1,
                "add -p t/depression");
        testAutocompleteForUserInput("add -p ", -1, "t/", 1,
                "add -p t/3legged");

        // appointment tags
        testAutocompleteForUserInput("add -a ", -1, "t/", 1,
                "add -a t/checkup");
        testAutocompleteForUserInput("add -a ", -1, "t/", 2,
                "add -a t/vaccination");

        // no option: all tags
        testAutocompleteForUserInput("add ", -1, "t/", 2,
                "add t/checkup");
        testAutocompleteForUserInput("add ", -1, "t/", 3,
                "add t/depression");
        testAutocompleteForUserInput("add ", -1, "t/", 5,
                "add t/owesMoney");
    }

    @Test
    public void commandBox_autocompleteMiddleOfText() {
        testAutocompleteForUserInput("add -p -o t/", 9, " nr/G1", 1,
                "add -p -o nr/G1111111B t/");
        testAutocompleteForUserInput("add -a -o -p t/", 12, " n/Jo", 1,
                "add -a -o -p n/Joker t/");
        testAutocompleteForUserInput("add -p n/joker s/cat b/persian c/brown bt/AB -o nr/S9600666G",
                38, " t/D", 1,
                "add -p n/joker s/cat b/persian c/brown t/depression bt/AB -o nr/S9600666G");
    }

    /**
     * Sets commandbox text to {@code userInput}.
     * Sets caret position to {@code index} if {@code index} > 0.
     * Inserts {@code userInput2} to commandbox.
     *
     * Checks that {@code userInput1} + {@code userInput2} with the {@code numOfTabs} to select an option
     * on autocomplete's context menu will result in {@code actualCommand}.
     */
    private void testAutocompleteForUserInput(String userInput1, int index, String userInput2, int numOfTabs,
                                              String actualCommand) {
        commandBoxHandle.setText(userInput1);

        if (index > 0) {
            commandBoxHandle.setCaretPosition(index);
        }

        for (int i = 0; i < userInput2.length(); i++) {
            char c = userInput2.charAt(i);
            commandBoxHandle.insertText(Character.toString(c));
        }

        while (numOfTabs > 0) {
            guiRobot.push(KeyCode.TAB);
            numOfTabs--;
        }

        guiRobot.push(KeyCode.ENTER);
        assertEquals(actualCommand, commandBoxHandle.getInput());
    }
}
```
###### \java\systemtests\AddCommandSystemTest.java
``` java
        /* Case: add a pet patient without tags to a non-empty address book, command with leading spaces and
         * trailing spaces -> added
         */
        PetPatient toAddPet = NERO;
        Nric bobNric = BOB.getNric();
        command = "   " + AddCommand.COMMAND_WORD + "  " + OPTION_PET + "  " + NAME_DESC_NERO
                + "  " +  SPECIES_DESC_NERO + "  " + BREED_DESC_NERO + "  " +  COLOUR_DESC_NERO + "  "
                + BLOODTYPE_DESC_NERO + "  " + OPTION_OWNER + "  " + NRIC_DESC_BOB + "  ";
        assertCommandSuccess(command, toAddPet, bobNric);

        /* Case: add a pet patient, missing tags -> added */
        assertCommandSuccess(KARUPIN, KARUPIN.getOwner());

        /* Case: add a pet patient with all fields same as another pet patient in the address book except name
        -> added */
        toAddPet = new PetPatientBuilder().withName("Joseph").withSpecies(VALID_SPECIES_NERO)
                .withBreed(VALID_BREED_NERO).withColour(VALID_COLOUR_NERO).withBloodType(VALID_BLOODTYPE_NERO)
                .withTags().withOwnerNric(bobNric.toString()).build();
        command = AddCommand.COMMAND_WORD + OPTION_PET + " n/Joseph" + SPECIES_DESC_NERO + BREED_DESC_NERO
                + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandSuccess(command, toAddPet, bobNric);

        /* Case: add a pet patient with all fields same as another pet patient in the address book except species
        -> added */
        toAddPet = new PetPatientBuilder().withName(VALID_NAME_NERO).withSpecies("Dog").withBreed(VALID_BREED_NERO)
                .withColour(VALID_COLOUR_NERO).withBloodType(VALID_BLOODTYPE_NERO).withTags()
                .withOwnerNric(bobNric.toString()).build();
        command = AddCommand.COMMAND_WORD + OPTION_PET + NAME_DESC_NERO + " s/Dog" + BREED_DESC_NERO
                + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandSuccess(command, toAddPet, bobNric);

        /* Case: add a pet patient with all fields same as another pet patient in the address book except breed
        -> added */
        toAddPet = new PetPatientBuilder().withName(VALID_NAME_NERO).withSpecies(VALID_SPECIES_NERO)
                .withBreed("Maltese").withColour(VALID_COLOUR_NERO).withBloodType(VALID_BLOODTYPE_NERO).withTags()
                .withOwnerNric(bobNric.toString()).build();
        command = AddCommand.COMMAND_WORD + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO + " b/Maltese"
                + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandSuccess(command, toAddPet, bobNric);

        /* Case: add a pet patient with all fields same as another pet patient in the address book except colour
        -> added */
        toAddPet = new PetPatientBuilder().withName(VALID_NAME_NERO).withSpecies(VALID_SPECIES_NERO)
                .withBreed(VALID_BREED_NERO).withColour("silver").withBloodType(VALID_BLOODTYPE_NERO).withTags()
                .withOwnerNric(bobNric.toString()).build();
        command = AddCommand.COMMAND_WORD + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO + BREED_DESC_NERO
                + " c/silver" + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandSuccess(command, toAddPet, bobNric);

        /* Case: add a pet patient with all fields same as another pet patient in the address book except blood type
        -> added */
        toAddPet = new PetPatientBuilder().withName(VALID_NAME_NERO).withSpecies(VALID_SPECIES_NERO)
                .withBreed(VALID_BREED_NERO).withColour(VALID_COLOUR_NERO).withBloodType("A").withTags()
                .withOwnerNric(bobNric.toString()).build();
        command = AddCommand.COMMAND_WORD + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO + BREED_DESC_NERO
                + COLOUR_DESC_NERO + " bt/A" + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandSuccess(command, toAddPet, bobNric);

        /* Case: add an appointment to a  non-empty address book, command with leading spaces and trailing spaces
        -> added */
        Appointment toAddAppt = BOB_APP;
        command = "   " + AddCommand.COMMAND_WORD + "  " + OPTION_APPOINTMENT + "  " + DATE_DESC_THREE
                + "  " +  REMARK_DESC_THREE + "  " + TAG_DESC_VACCINATION + "  " + OPTION_OWNER + "  "
                + NRIC_DESC_BOB + OPTION_PET + "  " + NAME_DESC_NERO + "  ";
        assertCommandSuccess(command, toAddAppt, bobNric, BOB_APP.getPetPatientName());
        //@author

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the person list before adding -> added */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while a person card is selected --------------------------- */

        /* Case: selects first card in the person list, add a person -> added, card selection remains unchanged */
        selectPerson(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate person -> rejected */
        command = PersonUtil.getAddCommand(HOON);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: add a duplicate person except with different tags -> rejected */
        // "friends" is an existing tag used in the default model, see TypicalPersons#ALICE
        // This test will fail if a new tag that is not in the model is used, see the bug documented in
        // AddressBook#addPerson(Person)
        command = PersonUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_OWNER + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + NRIC_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_PERSON));

        /* Case: missing phone -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_OWNER + NAME_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + NRIC_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_PERSON));

        /* Case: missing email -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_OWNER + NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY
                + NRIC_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_PERSON));

        /* Case: missing address -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_OWNER + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + NRIC_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_PERSON));

```
###### \java\systemtests\AddCommandSystemTest.java
``` java
        /* Case: add a duplicate pet patient -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO + BREED_DESC_NERO
                + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PET_PATIENT);

        /* Case: missing pet patient name -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + SPECIES_DESC_NERO + BREED_DESC_NERO
                + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_PETPATIENT));

        /* Case: missing pet patient species -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + NAME_DESC_NERO + BREED_DESC_NERO
                + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_PETPATIENT));

        /* Case: missing pet patient breed -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO
                + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_PETPATIENT));

        /* Case: missing pet patient colour -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO
                + BREED_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_PETPATIENT));

        /* Case: missing pet patient blood type -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO
                + BREED_DESC_NERO + COLOUR_DESC_NERO + OPTION_OWNER + NRIC_DESC_BOB;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_PETPATIENT));

        /* Case: missing option and owner's nric -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO
                + BREED_DESC_NERO + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing owner's nric -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_PET + NAME_DESC_NERO + SPECIES_DESC_NERO
                + BREED_DESC_NERO + COLOUR_DESC_NERO + BLOODTYPE_DESC_NERO + OPTION_OWNER;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_MISSING_NRIC_PREFIX));

        /* Case: missing appointment date -> rejected */
        command = AddCommand.COMMAND_WORD + OPTION_APPOINTMENT
                + REMARK_DESC_THREE + TAG_DESC_VACCINATION + OPTION_OWNER + NRIC_DESC_BOB + OPTION_PET
                + NAME_DESC_NERO;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_APPOINTMENT));

        /* Case: missing appointment remark -> rejected */
        command = AddCommand.COMMAND_WORD + OPTION_APPOINTMENT + DATE_DESC_THREE
                + TAG_DESC_VACCINATION + OPTION_OWNER + NRIC_DESC_BOB + OPTION_PET
                + NAME_DESC_NERO;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_APPOINTMENT));

        /* Case: missing appointment tag -> rejected */
        command = AddCommand.COMMAND_WORD + OPTION_APPOINTMENT + DATE_DESC_THREE
                + REMARK_DESC_THREE + OPTION_OWNER + NRIC_DESC_BOB + OPTION_PET
                + NAME_DESC_NERO;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_ERROR_APPOINTMENT));

        /* Case: missing appointment's owner nric -> rejected */
        command = AddCommand.COMMAND_WORD + OPTION_APPOINTMENT + DATE_DESC_THREE
                + REMARK_DESC_THREE + TAG_DESC_VACCINATION + OPTION_OWNER + OPTION_PET
                + NAME_DESC_NERO;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_MISSING_NRIC_PREFIX));

        /* Case: missing appointment's pet patient name -> rejected */
        command = AddCommand.COMMAND_WORD + OPTION_APPOINTMENT + DATE_DESC_THREE
                + REMARK_DESC_THREE + TAG_DESC_VACCINATION + OPTION_OWNER + NRIC_DESC_BOB + OPTION_PET;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_MISSING_PET_PATIENT_NAME_PREFIX));


    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage}, {@code PersonListPanel} and {@code PetPatientListPanel} equal to the
     * corresponding components in the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Appointment toAdd, Nric ownerNric, PetPatientName petPatientName)
            throws CommandException {
        assertCommandSuccess(AppointmentUtil.getAddCommand(toAdd, ownerNric, petPatientName), toAdd, ownerNric,
                petPatientName);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(PetPatient, Nric)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(PetPatient, Nric)
     */
    private void assertCommandSuccess(String command, Appointment toAdd, Nric ownerNric, PetPatientName petName)
            throws CommandException {
        Model expectedModel = getModel();
        Person owner = getModel().getPersonWithNric(ownerNric);
        PetPatient pet = getModel().getPetPatientWithNricAndName(ownerNric, petName);

        if (owner == null) {
            throw new CommandException(MESSAGE_INVALID_NRIC);
        }

        if (pet == null) {
            throw new CommandException(MESSAGE_INVALID_PET_PATIENT);
        }

        try {
            expectedModel.addAppointment(toAdd);
        } catch (DuplicateAppointmentException dae) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        } catch (DuplicateDateTimeException e) {
            throw new IllegalArgumentException("this date time already exists in the model.");
        } catch (ConcurrentAppointmentException c) {
            throw new IllegalArgumentException("there is another appointment which is concurrent");
        } catch (PastAppointmentException p) {
            throw new IllegalArgumentException("this date has already past.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS_APPOINTMENT, toAdd, owner, pet);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage}, {@code PersonListPanel} and {@code PetPatientListPanel} equal to the
     * corresponding components in the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(PetPatient toAdd, Nric ownerNric) throws CommandException {
        assertCommandSuccess(PetPatientUtil.getAddCommand(toAdd, ownerNric), toAdd, ownerNric);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(PetPatient, Nric)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(PetPatient, Nric)
     */
    private void assertCommandSuccess(String command, PetPatient toAdd, Nric ownerNric) throws CommandException {
        Model expectedModel = getModel();
        Person owner = getModel().getPersonWithNric(ownerNric);
        if (owner == null) {
            throw new CommandException(MESSAGE_INVALID_NRIC);
        }

        try {
            expectedModel.addPetPatient(toAdd);
        } catch (DuplicatePetPatientException dpe) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS_PETPATIENT, toAdd, owner);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

```
###### \java\systemtests\AddressBookSystemTest.java
``` java
    /**
     * Returns a command box with autocomplete function disabled.
     */
    public CommandBoxHandle getCommandBox() {
        mainWindowHandle.getCommandBox().disableAutocomplete();
        return mainWindowHandle.getCommandBox();
    }

```
