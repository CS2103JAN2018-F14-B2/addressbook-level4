package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.EditCommand.Type.EDIT_APPOINTMENT;
import static seedu.address.logic.commands.EditCommand.Type.EDIT_PERSON;
import static seedu.address.logic.commands.EditCommand.Type.EDIT_PET_PATIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BLOODTYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BREED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COLOUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NRIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Remark;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.petpatient.PetPatient;
import seedu.address.model.petpatient.PetPatientName;
import seedu.address.model.tag.Tag;

//@@author chialejing
/**
 * Edits the details of an existing person, pet patient or appointment in the address book.
 */
public class EditCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_ALIAS = "ed";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the contact / owner / appointment identified by their respective index numbers. "
            + "Existing values will be overwritten by the input values. "
            + "Note that INDEX must be a positive integer.\n"
            + "To edit the details of an existing contact: "
            + COMMAND_WORD + " -o " + "INDEX "
            + "[" + PREFIX_NAME + "CONTACT_NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_NRIC + "NRIC] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "To edit the details of an existing pet patient: "
            + COMMAND_WORD + " -p " + "INDEX "
            + "[" + PREFIX_NAME + "PET_PATIENT_NAME] "
            + "[" + PREFIX_SPECIES + "SPECIES] "
            + "[" + PREFIX_BREED + "BREED] "
            + "[" + PREFIX_COLOUR + "COLOUR] "
            + "[" + PREFIX_BLOODTYPE + "BLOOD_TYPE] "
            + "[" + PREFIX_NRIC + "OWNER_NRIC] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "To edit the details of an existing appointment: "
            + COMMAND_WORD + " -a " + "INDEX "
            + "[" + PREFIX_DATE + "YYYY-MM-DD HH:MM] "
            + "[" + PREFIX_REMARK + "REMARK] "
            + "[" + PREFIX_NRIC + "OWNER_NRIC] "
            + "[" + PREFIX_NAME + "PET_PATIENT_NAME] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "\n";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_EDIT_PET_PATIENT_SUCCESS = "Edited Pet Patient: %1$s";
    public static final String MESSAGE_EDIT_APPOINTMENT_SUCCESS = "Edited Appointment: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    /**
     * Enum to support the type of edit command that the user wishes to execute.
     */
    public enum Type { EDIT_PERSON, EDIT_PET_PATIENT, EDIT_APPOINTMENT };

    private Index index;
    private EditPersonDescriptor editPersonDescriptor;
    private EditPetPatientDescriptor editPetPatientDescriptor;
    private EditAppointmentDescriptor editAppointmentDescriptor;
    private Type type;

    private Person personToEdit;
    private Person editedPerson;
    private PetPatient petPatientToEdit;
    private PetPatient editedPetPatient;
    private Appointment appointmentToEdit;
    private Appointment editedAppointment;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
        this.type = EDIT_PERSON;
    }

    /**
     * @param index of the pet patient in the filtered pet patient list to edit
     * @param editPetPatientDescriptor details to edit the pet patient with
     */
    public EditCommand(Index index, EditPetPatientDescriptor editPetPatientDescriptor) {
        requireNonNull(index);
        requireNonNull(editPetPatientDescriptor);

        this.index = index;
        this.editPetPatientDescriptor = new EditPetPatientDescriptor(editPetPatientDescriptor);
        this.type = EDIT_PET_PATIENT;
    }

    /**
     * @param index of the appointment in the filtered appointment list to edit
     * @param editAppointmentDescriptor details to edit the appointment with
     */
    public EditCommand(Index index, EditAppointmentDescriptor editAppointmentDescriptor) {
        requireNonNull(index);
        requireNonNull(editAppointmentDescriptor);

        this.index = index;
        this.editAppointmentDescriptor = new EditAppointmentDescriptor(editAppointmentDescriptor);
        this.type = EDIT_APPOINTMENT;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            switch (type) {
            case EDIT_PERSON:
                model.updatePerson(personToEdit, editedPerson);
                // TODO: complete function to check for pets and appointments under Person
                break;
            case EDIT_PET_PATIENT:
                // TODO: complete updatePetPatient() and relevant checks + edits
                break;
            case EDIT_APPOINTMENT:
                // TODO: complete updateAppointment() and relevant checks + edits
                break;
            default:
                break;
            }
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        switch (type) {
        case EDIT_PERSON:
            preprocessUndoableCommandForPerson();
            break;
        case EDIT_PET_PATIENT:
            preprocessUndoableCommandForPetPatient();
            break;
        case EDIT_APPOINTMENT:
            preprocessUndoableCommandForAppointment();
            break;
        default:
            break;
        }
    }
    /**
     * Obtains the last shown person list.
     */
    protected void preprocessUndoableCommandForPerson() throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        }

        personToEdit = lastShownPersonList.get(index.getZeroBased());
        editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
    }

    /**
     * Obtains the last shown pet patient list.
     */
    protected void preprocessUndoableCommandForPetPatient() throws CommandException {
        List<PetPatient> lastShownPetPatientList = model.getFilteredPetPatientList();

        if (index.getZeroBased() >= lastShownPetPatientList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PET_PATIENT_DISPLAYED_INDEX);
        }

        petPatientToEdit = lastShownPetPatientList.get(index.getZeroBased());
        editedPetPatient = createEditedPetPatient(petPatientToEdit, editPetPatientDescriptor);
    }

    /**
     * Obtains the last shown appointment list.
     */
    protected void preprocessUndoableCommandForAppointment() throws CommandException {
        List<Appointment> lastShownAppointmentList = model.getFilteredAppointmentList();

        if (index.getZeroBased() >= lastShownAppointmentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        }

        appointmentToEdit = lastShownAppointmentList.get(index.getZeroBased());
        editedAppointment = createEditedAppointment(appointmentToEdit, editAppointmentDescriptor);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Nric updatedNric = editPersonDescriptor.getNric().orElse(personToEdit.getNric());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedNric, updatedTags);
    }

    /**
     * Creates and returns a {@code PetPatient} with the details of {@code petPatientToEdit}
     * edited with {@code editPetPatientDescriptor}.
     */
    private static PetPatient createEditedPetPatient(PetPatient petPatientToEdit,
                                                     EditPetPatientDescriptor editPetPatientDescriptor) {
        assert petPatientToEdit != null;

        PetPatientName updatedName = editPetPatientDescriptor.getName().orElse(petPatientToEdit.getName());
        String updatedSpecies = editPetPatientDescriptor.getSpecies().orElse(petPatientToEdit.getSpecies());
        String updatedBreed = editPetPatientDescriptor.getBreed().orElse(petPatientToEdit.getBreed());
        String updatedColour = editPetPatientDescriptor.getColour().orElse(petPatientToEdit.getColour());
        String updatedBloodType = editPetPatientDescriptor.getBloodType().orElse(petPatientToEdit.getBloodType());
        Nric updatedOwnerNric = editPetPatientDescriptor.getOwnerNric().orElse(petPatientToEdit.getOwner());
        Set<Tag> updatedTags = editPetPatientDescriptor.getTags().orElse(petPatientToEdit.getTags());

        return new PetPatient(
                updatedName,
                updatedSpecies,
                updatedBreed,
                updatedColour,
                updatedBloodType,
                updatedOwnerNric,
                updatedTags
        );
    }

    /**
     * Creates and returns a {@code Appointment} with the details of {@code appointmentToEdit}
     * edited with {@code editAppointmentDescriptor}.
     */
    private static Appointment createEditedAppointment(Appointment appointmentToEdit,
                                                       EditAppointmentDescriptor editAppointmentDescriptor) {
        assert appointmentToEdit != null;

        Nric updatedOwnerNric = editAppointmentDescriptor.getOwnerNric()
                .orElse(appointmentToEdit.getOwnerNric());
        PetPatientName updatedPetPatientName = editAppointmentDescriptor.getPetPatientName()
                .orElse(appointmentToEdit.getPetPatientName());
        Remark updatedRemark = editAppointmentDescriptor.getRemark()
                .orElse(appointmentToEdit.getRemark());
        LocalDateTime updatedLocalDateTime = editAppointmentDescriptor.getLocalDateTime()
                .orElse(appointmentToEdit.getDateTime());
        Set<Tag> updatedTags = editAppointmentDescriptor.getTags()
                .orElse(appointmentToEdit.getAppointmentTags());

        return new Appointment(
                updatedOwnerNric,
                updatedPetPatientName,
                updatedRemark,
                updatedLocalDateTime,
                updatedTags
        );
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Nric nric;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setNric(toCopy.nric);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.phone, this.email,
                    this.address, this.nric, this.tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setNric(Nric nric) {
            this.nric = nric;
        }

        public Optional<Nric> getNric() {
            return Optional.ofNullable(nric);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getNric().equals(e.getNric())
                    && getTags().equals(e.getTags());
        }
    }

    /**
     * Stores the details to edit the pet patient with. Each non-empty field value will replace the
     * corresponding field value of the pet patient.
     */
    public static class EditPetPatientDescriptor {
        private PetPatientName name;
        private String species;
        private String breed;
        private String colour;
        private String bloodType;
        private Nric ownerNric;
        private Set<Tag> tags;

        public EditPetPatientDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPetPatientDescriptor(EditPetPatientDescriptor toCopy) {
            setName(toCopy.name);
            setSpecies(toCopy.species);
            setBreed(toCopy.breed);
            setColour(toCopy.colour);
            setBloodType(toCopy.bloodType);
            setOwnerNric(toCopy.ownerNric);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.species, this.breed,
                    this.colour, this.bloodType, this.ownerNric, this.tags);
        }

        public void setName(PetPatientName name) {
            this.name = name;
        }

        public Optional<PetPatientName> getName() {
            return Optional.ofNullable(name);
        }

        public void setSpecies(String species) {
            this.species = species;
        }

        public Optional<String> getSpecies() {
            return Optional.ofNullable(species);
        }

        public void setBreed(String breed) {
            this.breed = breed;
        }

        public Optional<String> getBreed() {
            return Optional.ofNullable(breed);
        }

        public void setColour(String colour) {
            this.colour = colour;
        }

        public Optional<String> getColour() {
            return Optional.ofNullable(colour);
        }

        public void setBloodType(String bloodType) {
            this.bloodType = bloodType;
        }

        public Optional<String> getBloodType() {
            return Optional.ofNullable(bloodType);
        }

        public void setOwnerNric(Nric nric) {
            this.ownerNric = ownerNric;
        }

        public Optional<Nric> getOwnerNric() {
            return Optional.ofNullable(ownerNric);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPetPatientDescriptor)) {
                return false;
            }

            // state check
            EditPetPatientDescriptor e = (EditPetPatientDescriptor) other;

            return getName().equals(e.getName())
                    && getSpecies().equals(e.getSpecies())
                    && getBreed().equals(e.getBreed())
                    && getColour().equals(e.getColour())
                    && getBloodType().equals(e.getBloodType())
                    && getOwnerNric().equals(e.getOwnerNric())
                    && getTags().equals(e.getTags());
        }
    }

    /**
     * Stores the details to edit the appointment with. Each non-empty field value will replace the
     * corresponding field value of the appointment.
     */
    public static class EditAppointmentDescriptor {
        private Nric ownerNric;
        private PetPatientName petPatientName;
        private Remark remark;
        private LocalDateTime localDateTime;
        private Set<Tag> tags;

        public EditAppointmentDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditAppointmentDescriptor(EditAppointmentDescriptor toCopy) {
            setOwnerNric(toCopy.ownerNric);
            setPetPatientName(toCopy.petPatientName);
            setRemark(toCopy.remark);
            setLocalDateTime(toCopy.localDateTime);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.ownerNric, this.petPatientName, this.remark,
                    this.localDateTime, this.tags);
        }

        public void setOwnerNric(Nric ownerNric) {
            this.ownerNric = ownerNric;
        }

        public Optional<Nric> getOwnerNric() {
            return Optional.ofNullable(ownerNric);
        }

        public void setPetPatientName(PetPatientName petPatientName) {
            this.petPatientName = petPatientName;
        }

        public Optional<PetPatientName> getPetPatientName() {
            return Optional.ofNullable(petPatientName);
        }

        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        public Optional<LocalDateTime> getLocalDateTime() {
            return Optional.ofNullable(localDateTime);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditAppointmentDescriptor)) {
                return false;
            }

            // state check
            EditAppointmentDescriptor e = (EditAppointmentDescriptor) other;

            return getOwnerNric().equals(e.getOwnerNric())
                    && getPetPatientName().equals(e.getPetPatientName())
                    && getRemark().equals(e.getRemark())
                    && getLocalDateTime().equals(e.getLocalDateTime())
                    && getTags().equals(e.getTags());
        }
    }
}
