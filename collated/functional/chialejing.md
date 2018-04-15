# chialejing
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
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
            // + "[" + PREFIX_NRIC + "OWNER_NRIC] "
            // + "[" + PREFIX_NAME + "PET_PATIENT_NAME] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "\n";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Contact: %1$s";
    public static final String MESSAGE_EDIT_PET_PATIENT_SUCCESS = "Edited Pet Patient: %1$s";
    public static final String MESSAGE_EDIT_APPOINTMENT_SUCCESS = "Edited Appointment: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This contact already exists in the address book.";
    public static final String MESSAGE_DUPLICATE_PET_PATIENT = "This pet patient already exists in the address book.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the address book.";
    public static final String MESSAGE_MISSING_PERSON = "The target contact cannot be missing.";
    public static final String MESSAGE_MISSING_PET_PATIENT = "The target pet patient cannot be missing";
    public static final String MESSAGE_MISSING_APPOINTMENT = "The target appointment cannot be missing.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT_TIMING = "Duplicate in appointment timing.";
    public static final String MESSAGE_PAST_APPOINTMENT = "Appointment cannot be in the past.";
    public static final String MESSAGE_CONCURRENT_APPOINTMENT = "Appointment cannot be concurrent "
            + "with other appointments.";
    /**
     * Enum to support the type of edit command that the user wishes to execute.
     */
    public enum Type { EDIT_PERSON, EDIT_PET_PATIENT, EDIT_APPOINTMENT };

    private Index index;
    private EditPersonDescriptor editPersonDescriptor;
    private EditPetPatientDescriptor editPetPatientDescriptor;
    private EditAppointmentDescriptor editAppointmentDescriptor;
    private Type type;

    private Person personToEdit; // original
    private Person editedPerson; // edited
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
                resolvePersonDependencies();
                model.updatePerson(personToEdit, editedPerson);
                break;
            case EDIT_PET_PATIENT:
                resolvePetPatientDependencies();
                model.updatePetPatient(petPatientToEdit, editedPetPatient);
                break;
            case EDIT_APPOINTMENT:
                checkForClashes();
                // checkForSameAppointmentTiming();
                checkForConcurrentAppointments();
                checkForPastAppointment();
                model.updateAppointment(appointmentToEdit, editedAppointment);
                break;
            default:
                break;
            }
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new CommandException(MESSAGE_MISSING_PERSON);
        } catch (DuplicatePetPatientException dppe) {
            throw new CommandException(MESSAGE_DUPLICATE_PET_PATIENT);
        } catch (PetPatientNotFoundException ppnfe) {
            throw new CommandException(MESSAGE_MISSING_PET_PATIENT);
        } catch (DuplicateDateTimeException ddte) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT_TIMING);
        } catch (DuplicateAppointmentException dae) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentNotFoundException anfe) {
            throw new CommandException(MESSAGE_MISSING_APPOINTMENT);
        } catch (PastAppointmentException pae) {
            throw new CommandException(MESSAGE_PAST_APPOINTMENT);
        } catch (ConcurrentAppointmentException cae) {
            throw new CommandException(MESSAGE_CONCURRENT_APPOINTMENT);
        }
        switch (type) {
        case EDIT_PERSON:
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
        case EDIT_PET_PATIENT:
            model.updateFilteredPetPatientList(PREDICATE_SHOW_ALL_PET_PATIENTS);
            return new CommandResult(String.format(MESSAGE_EDIT_PET_PATIENT_SUCCESS, editedPetPatient));
        case EDIT_APPOINTMENT:
            model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
            return new CommandResult(String.format(MESSAGE_EDIT_APPOINTMENT_SUCCESS, editedAppointment));
        default:
            return null;
        }
    }

    /**
     * Checks whether person's NRIC has been modified
     * If yes, update all other relevant pet patients and appointments under the same person
     * If no, do nothing
     */
    private void resolvePersonDependencies() throws DuplicatePetPatientException, PetPatientNotFoundException,
            DuplicateAppointmentException, AppointmentNotFoundException {

        Nric oldNric = personToEdit.getNric();
        Nric newNric = editedPerson.getNric();

        if (!oldNric.equals(newNric)) {
            updatePetPatientsByOwnerNric(oldNric, newNric);
            updateAppointmentByOwnerNric(oldNric, newNric);
        }
    }

    /**
     * Checks whether pet patient's name or owner NRIC has been modified
     * If yes, update all other relevant appointments and also the update the new owner for the pet
     */
    private void resolvePetPatientDependencies() throws CommandException,
            AppointmentNotFoundException, DuplicateAppointmentException {

        Nric oldNric = petPatientToEdit.getOwner();
        Nric newNric = editedPetPatient.getOwner();
        PetPatientName oldPetName = petPatientToEdit.getName();
        PetPatientName newPetName = editedPetPatient.getName();

        if (!oldNric.equals(newNric)) { // nric edited, I want to change owner
            Person newOwner = model.getPersonWithNric(newNric); // new owner must exist
            if (newOwner == null) {
                throw new CommandException("New owner must exist first before updating pet patient's owner NRIC!");
            }
            // we only update nric for appointments for that specific pet patient!
            // this is because it might be an owner transfer. If there are some other pets under the previous owner,
            // he/she may still be holding on to these pets.
            updateAppointmentByOwnerNricForSpecificPetName(oldNric, newNric, oldPetName);
        }
        if (!oldPetName.equals(newPetName)) { // name edited
            updateAppointmentByPetPatientName(newNric, oldPetName, newPetName);
        }
    }

    /**
     * Checks whether there are clashes in appointment date and time (i.e. same timing with another appointment)
     */
    private void checkForClashes() throws DuplicateDateTimeException {
        LocalDateTime oldDateTime = appointmentToEdit.getDateTime();
        LocalDateTime newDateTime = editedAppointment.getDateTime();

        if (!oldDateTime.equals(newDateTime)) {
            Appointment appointmentWithClash = model.getClashingAppointment(newDateTime);
            if (appointmentWithClash != null) {
                throw new DuplicateDateTimeException();
            }
        }
    }

    /**
     * Checks whether there are clashes in appointment date and time (concurrent appointments)
     */
    private void checkForConcurrentAppointments() throws ConcurrentAppointmentException {
        LocalDateTime oldDateTime = appointmentToEdit.getDateTime();
        LocalDateTime newDateTime = editedAppointment.getDateTime();

        if (model.hasConcurrentAppointment(oldDateTime, newDateTime)) {
            throw new ConcurrentAppointmentException();
        }
    }

    /**
     * Checks whether appointment datetime given is in the past
     */
    private void checkForPastAppointment() throws PastAppointmentException {
        LocalDateTime newDateTime = editedAppointment.getDateTime();

        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new PastAppointmentException();
        }
    }

    /**
     * Checks whether the new timing for the appointment is equivalent to the old one
     */
    private void checkForSameAppointmentTiming() throws CommandException {
        LocalDateTime oldDateTime = appointmentToEdit.getDateTime();
        LocalDateTime newDateTime = editedAppointment.getDateTime();

        if (newDateTime.equals(oldDateTime)) {
            throw new CommandException("Appointment timing has not changed.");
        }
    }

    /**
     * Helper function to update pet patient's owner from an old nric to new nric
     */
    private void updatePetPatientsByOwnerNric(Nric oldNric, Nric newNric) throws
            PetPatientNotFoundException, DuplicatePetPatientException {

        ArrayList<PetPatient> petPatientArrayList = model.getPetPatientsWithNric(oldNric);
        EditPetPatientDescriptor eppd = new EditPetPatientDescriptor();
        eppd.setOwnerNric(newNric);

        for (PetPatient currPetPatient : petPatientArrayList) {
            PetPatient modifiedPetPatient = createEditedPetPatient(currPetPatient, eppd);
            model.updatePetPatient(currPetPatient, modifiedPetPatient);
            model.updateFilteredPetPatientList(PREDICATE_SHOW_ALL_PET_PATIENTS);
        }
    }

    /**
     * Helper function to update appointment's owner from an old nric to new nric
     */
    private void updateAppointmentByOwnerNric(Nric oldNric, Nric newNric) throws
            AppointmentNotFoundException, DuplicateAppointmentException {

        ArrayList<Appointment> appointmentArrayList = model.getAppointmentsWithNric(oldNric);
        EditAppointmentDescriptor ead = new EditAppointmentDescriptor();
        ead.setOwnerNric(newNric);

        for (Appointment currAppointment : appointmentArrayList) {
            Appointment modifiedAppointment = createEditedAppointment(currAppointment, ead);
            model.updateAppointment(currAppointment, modifiedAppointment);
            model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        }
    }

    /**
     * Helper function to update pet patient name in appointment
     */
    private void updateAppointmentByPetPatientName(Nric ownerNric, PetPatientName oldPetName,
                                                   PetPatientName newPetName) throws
            DuplicateAppointmentException, AppointmentNotFoundException {

        ArrayList<Appointment> appointmentArrayList =
                model.getAppointmentsWithNricAndPetName(ownerNric, oldPetName);
        EditAppointmentDescriptor ead = new EditAppointmentDescriptor();
        ead.setPetPatientName(newPetName);

        for (Appointment currAppointment : appointmentArrayList) {
            Appointment modifiedAppointment = createEditedAppointment(currAppointment, ead);
            model.updateAppointment(currAppointment, modifiedAppointment);
            model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        }
    }

    /**
     * Helper function to update the pet patient owner's NRIC for all its appointment
     */
    private void updateAppointmentByOwnerNricForSpecificPetName(Nric oldNric, Nric newNric, PetPatientName oldPetName)
            throws DuplicateAppointmentException, AppointmentNotFoundException {

        ArrayList<Appointment> appointmentArrayList =
                model.getAppointmentsWithNricAndPetName(oldNric, oldPetName);
        EditAppointmentDescriptor ead = new EditAppointmentDescriptor();
        ead.setOwnerNric(newNric);

        for (Appointment currAppointment : appointmentArrayList) {
            Appointment modifiedAppointment = createEditedAppointment(currAppointment, ead);
            model.updateAppointment(currAppointment, modifiedAppointment);
            model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        }
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
        Species updatedSpecies = editPetPatientDescriptor.getSpecies().orElse(petPatientToEdit.getSpecies());
        Breed updatedBreed = editPetPatientDescriptor.getBreed().orElse(petPatientToEdit.getBreed());
        Colour updatedColour = editPetPatientDescriptor.getColour().orElse(petPatientToEdit.getColour());
        BloodType updatedBloodType = editPetPatientDescriptor.getBloodType().orElse(petPatientToEdit.getBloodType());
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
        switch (type) {
        case EDIT_PERSON:
            return index.equals(e.index)
                    && editPersonDescriptor.equals(e.editPersonDescriptor)
                    && Objects.equals(personToEdit, e.personToEdit);
        case EDIT_PET_PATIENT:
            return index.equals(e.index)
                    && editPetPatientDescriptor.equals(e.editPetPatientDescriptor)
                    && Objects.equals(petPatientToEdit, e.petPatientToEdit);
        case EDIT_APPOINTMENT:
            return index.equals(e.index)
                    && editAppointmentDescriptor.equals(e.editAppointmentDescriptor)
                    && Objects.equals(appointmentToEdit, e.appointmentToEdit);
        default:
            return false;
        }
    }
}
```
###### \java\seedu\address\logic\descriptors\EditAppointmentDescriptor.java
``` java
/**
 * Stores the details to edit the appointment with. Each non-empty field value will replace the
 * corresponding field value of the appointment.
 */
public class EditAppointmentDescriptor {
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
```
###### \java\seedu\address\logic\descriptors\EditPetPatientDescriptor.java
``` java
/**
 * Stores the details to edit the pet patient with. Each non-empty field value will replace the
 * corresponding field value of the pet patient.
 */
public class EditPetPatientDescriptor {
    private PetPatientName name;
    private Species species;
    private Breed breed;
    private Colour colour;
    private BloodType bloodType;
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

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Optional<Species> getSpecies() {
        return Optional.ofNullable(species);
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Optional<Breed> getBreed() {
        return Optional.ofNullable(breed);
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Optional<Colour> getColour() {
        return Optional.ofNullable(colour);
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public Optional<BloodType> getBloodType() {
        return Optional.ofNullable(bloodType);
    }

    public void setOwnerNric(Nric nric) {
        this.ownerNric = nric;
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
```
###### \java\seedu\address\logic\parser\EditCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private static final Pattern EDIT_COMMAND_FORMAT_PERSON = Pattern.compile("-(o)+(?<personInfo>.*)");
    private static final Pattern EDIT_COMMAND_FORMAT_PET_PATIENT = Pattern.compile("-(p)+(?<petPatientInfo>.*)");
    private static final Pattern EDIT_COMMAND_FORMAT_APPOINTMENT = Pattern.compile("-(a)+(?<appointmentInfo>.*)");

    /**
     * Parses the different types of Objects (Person, PetPatient or Appointment) based on whether user has
     * provided "-o", "-p" or "-a" in the command
     * @param args String to parse
     * @return EditCommand object to edit the object
     * @throws ParseException if invalid format is detected
     */
    public EditCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        // Edit existing person
        final Matcher matcherForPerson = EDIT_COMMAND_FORMAT_PERSON.matcher(trimmedArgs);
        if (matcherForPerson.matches()) {
            String personInfo = matcherForPerson.group("personInfo");
            return parsePerson(personInfo);
        }

        // Edit existing pet patient
        final Matcher matcherForPetPatient = EDIT_COMMAND_FORMAT_PET_PATIENT.matcher(trimmedArgs);
        if (matcherForPetPatient.matches()) {
            String petPatientInfo = matcherForPetPatient.group("petPatientInfo");
            return parsePetPatient(petPatientInfo);
        }

        // Edit existing appointment
        final Matcher matcherForAppointment = EDIT_COMMAND_FORMAT_APPOINTMENT.matcher(trimmedArgs);
        if (matcherForAppointment.matches()) {
            String appointmentInfo = matcherForAppointment.group("appointmentInfo");
            return parseAppointment(appointmentInfo);
        }

        // throws exception for invalid format
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
    }

```
###### \java\seedu\address\logic\parser\EditCommandParser.java
``` java
    /**
     * Parses the given {@code petPatientInfo} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parsePetPatient(String petPatientInfo) throws ParseException {
        requireNonNull(petPatientInfo);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(petPatientInfo, PREFIX_NAME, PREFIX_SPECIES, PREFIX_BREED,
                        PREFIX_COLOUR, PREFIX_BLOODTYPE, PREFIX_NRIC, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditPetPatientDescriptor editPetPatientDescriptor = new EditPetPatientDescriptor();
        try {
            ParserUtil.parsePetPatientName(argMultimap.getValue(PREFIX_NAME))
                    .ifPresent(editPetPatientDescriptor::setName);
            ParserUtil.parseSpecies(argMultimap.getValue(PREFIX_SPECIES))
                    .ifPresent(editPetPatientDescriptor::setSpecies);
            ParserUtil.parseBreed(argMultimap.getValue(PREFIX_BREED))
                    .ifPresent(editPetPatientDescriptor::setBreed);
            ParserUtil.parseColour(argMultimap.getValue(PREFIX_COLOUR))
                    .ifPresent(editPetPatientDescriptor::setColour);
            ParserUtil.parseBloodType(argMultimap.getValue(PREFIX_BLOODTYPE))
                    .ifPresent(editPetPatientDescriptor::setBloodType);
            ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC))
                    .ifPresent(editPetPatientDescriptor::setOwnerNric);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                    .ifPresent(editPetPatientDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editPetPatientDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPetPatientDescriptor);
    }

    /**
     * Parses the given {@code appointmentInfo} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parseAppointment(String appointmentInfo) throws ParseException {
        requireNonNull(appointmentInfo);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(appointmentInfo, PREFIX_DATE, PREFIX_REMARK, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditAppointmentDescriptor editAppointmentDescriptor = new EditAppointmentDescriptor();
        try {
            // ParserUtil.parsePetPatientName(argMultimap.getValue(PREFIX_NAME))
            //        .ifPresent(editAppointmentDescriptor::setPetPatientName);
            ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_DATE))
                    .ifPresent(editAppointmentDescriptor::setLocalDateTime);
            ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK))
                    .ifPresent(editAppointmentDescriptor::setRemark);
            //ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC))
            //        .ifPresent(editAppointmentDescriptor::setOwnerNric);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                    .ifPresent(editAppointmentDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editAppointmentDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editAppointmentDescriptor);

    }

```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<PetPatientName> parsePetPatientName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(parsePetPatientName(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String species} into a {@code Species}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Species parseSpecies(String species) throws IllegalValueException {
        requireNonNull(species);
        String trimmedSpecies = species.trim();
        if (!Species.isValidSpecies(trimmedSpecies)) {
            throw new IllegalValueException(Species.MESSAGE_PET_SPECIES_CONSTRAINTS);
        }
        String[] wordsInSpecies = trimmedSpecies.split(" ");
        StringBuilder formattedSpecies = new StringBuilder();
        for (String s : wordsInSpecies) {
            formattedSpecies = formattedSpecies
                    .append(s.substring(0, 1).toUpperCase())
                    .append(s.substring(1).toLowerCase())
                    .append(" ");
        }
        return new Species(formattedSpecies.toString().trim());
    }

    /**
     * Parses a {@code Optional<String> species} into an {@code Optional<Species>} if {@code species} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Species> parseSpecies(Optional<String> species) throws IllegalValueException {
        requireNonNull(species);
        return species.isPresent() ? Optional.of(parseSpecies(species.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String breed} into a {@code Breed}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Breed parseBreed(String breed) throws IllegalValueException {
        requireNonNull(breed);
        String trimmedBreed = breed.trim();
        if (!Breed.isValidBreed(trimmedBreed)) {
            throw new IllegalValueException(Breed.MESSAGE_PET_BREED_CONSTRAINTS);
        }
        String[] wordsInBreed = trimmedBreed.split(" ");
        StringBuilder formattedBreed = new StringBuilder();
        for (String s : wordsInBreed) {
            formattedBreed = formattedBreed
                    .append(s.substring(0, 1).toUpperCase())
                    .append(s.substring(1).toLowerCase())
                    .append(" ");
        }
        return new Breed(formattedBreed.toString().trim());
    }

    /**
     * Parses a {@code Optional<String> breed} into an {@code Optional<Breed>} if {@code breed} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Breed> parseBreed(Optional<String> breed) throws IllegalValueException {
        requireNonNull(breed);
        return breed.isPresent() ? Optional.of(parseBreed(breed.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String colour} into a {@code Colour}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Colour parseColour(String colour) throws IllegalValueException {
        requireNonNull(colour);
        String trimmedColour = colour.trim();
        if (!Colour.isValidColour(trimmedColour)) {
            throw new IllegalValueException(Colour.MESSAGE_PET_COLOUR_CONSTRAINTS);
        }
        String[] wordsInColour = trimmedColour.split(" ");
        StringBuilder formattedColour = new StringBuilder();
        for (String s : wordsInColour) {
            formattedColour = formattedColour
                    .append(s.substring(0).toLowerCase())
                    .append(" ");
        }
        return new Colour(formattedColour.toString().trim());
    }

    /**
     * Parses a {@code Optional<String> colour} into an {@code Optional<Colour>} if {@code colour} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Colour> parseColour(Optional<String> colour) throws IllegalValueException {
        requireNonNull(colour);
        return colour.isPresent() ? Optional.of(parseColour(colour.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String bloodType} into a {@code String}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static BloodType parseBloodType(String bloodType) throws IllegalValueException {
        requireNonNull(bloodType);
        String trimmedBloodType = bloodType.trim();
        if (!BloodType.isValidBloodType(trimmedBloodType)) {
            throw new IllegalValueException(BloodType.MESSAGE_PET_BLOODTYPE_CONSTRAINTS);
        }
        String[] wordsInBloodType = trimmedBloodType.split(" ");
        StringBuilder formattedBloodType = new StringBuilder();
        for (String s : wordsInBloodType) {
            formattedBloodType = formattedBloodType
                    .append(s.substring(0).toUpperCase())
                    .append(" ");
        }
        return new BloodType(formattedBloodType.toString().trim());
    }

    /**
     * Parses a {@code Optional<String> bloodType} into an {@code Optional<BloodType>} if {@code bloodType} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<BloodType> parseBloodType(Optional<String> bloodType) throws IllegalValueException {
        requireNonNull(bloodType);
        return bloodType.isPresent() ? Optional.of(parseBloodType(bloodType.get())) : Optional.empty();
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Replaces the given pet patient {@code target} in the list with {@code editedPetPatient}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedPetPatient}.
     *
     * @throws DuplicatePetPatientException if updating the pet patient's details causes the pet patient to be
     *                                      equivalent to another existing pet patient in the list.
     * @throws PetPatientNotFoundException  if {@code target} could not be found in the list.
     * @see #syncWithMasterTagList(PetPatient)
     */
    public void updatePetPatient(PetPatient target, PetPatient editedPetPatient)
            throws DuplicatePetPatientException, PetPatientNotFoundException {
        requireNonNull(editedPetPatient);

        PetPatient syncEditedPetPatient = syncWithMasterTagList(editedPetPatient);
        petPatients.setPetPatient(target, syncEditedPetPatient);
        removeUselessTags();
    }

    /**
     * Replaces the given appointment {@code target} in the list with {@code editedAppointment}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedAppointment}.
     *
     * @throws DuplicateAppointmentException if updating the appointment's details causes the appointment to be
     *                                       equivalent to another existing appointment in the list.
     * @throws AppointmentNotFoundException  if {@code target} could not be found in the list.
     * @see #syncWithMasterTagList(Appointment)
     */
    public void updateAppointment(Appointment target, Appointment editedAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireNonNull(editedAppointment);

        Appointment syncEditedPetPatient = syncWithMasterTagList(editedAppointment);
        appointments.setAppointment(target, syncEditedPetPatient);
        removeUselessTags();
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Updates the master tag list to include tags in {@code petPatient} that are not in the list.
     *
     * @return a copy of this {@code petPatient} such that every tag in this pet patient points to a Tag object in the
     * master list.
     */
    private PetPatient syncWithMasterTagList (PetPatient petPatient) {
        final UniqueTagList currentPetPatientTags = new UniqueTagList(petPatient.getTags());
        tags.mergeFrom(currentPetPatientTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        currentPetPatientTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new PetPatient(
                petPatient.getName(),
                petPatient.getSpecies(),
                petPatient.getBreed(),
                petPatient.getColour(),
                petPatient.getBloodType(),
                petPatient.getOwner(),
                correctTagReferences);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void addPetPatient(PetPatient petPatient) throws DuplicatePetPatientException {
        addressBook.addPetPatient(petPatient);
        updateFilteredPetPatientList(PREDICATE_SHOW_ALL_PET_PATIENTS);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public ArrayList<PetPatient> getPetPatientsWithNric(Nric ownerNric) {
        ArrayList<PetPatient> petPatientArrayList = new ArrayList<>();
        for (PetPatient p : addressBook.getPetPatientList()) {
            if (p.getOwner().equals(ownerNric)) {
                petPatientArrayList.add(p);
            }
        }
        return petPatientArrayList;
    }

    @Override
    public ArrayList<Appointment> getAppointmentsWithNric(Nric ownerNric) {
        ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
        for (Appointment a : addressBook.getAppointmentList()) {
            if (a.getOwnerNric().equals(ownerNric)) {
                appointmentArrayList.add(a);
            }
        }
        return appointmentArrayList;
    }

    @Override
    public ArrayList<Appointment> getAppointmentsWithNricAndPetName(Nric ownerNric, PetPatientName petPatientName) {
        ArrayList<Appointment> appointmentArrayList = new ArrayList<>();
        for (Appointment a : addressBook.getAppointmentList()) {
            if (a.getOwnerNric().equals(ownerNric) && a.getPetPatientName().equals(petPatientName)) {
                appointmentArrayList.add(a);
            }
        }
        return appointmentArrayList;
    }

    @Override
    public Appointment getClashingAppointment(LocalDateTime dateTime) {
        for (Appointment a : addressBook.getAppointmentList()) {
            if (a.getDateTime().equals(dateTime)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public boolean hasConcurrentAppointment(LocalDateTime oldDateTime, LocalDateTime newDateTime) {
        for (Appointment a : addressBook.getAppointmentList()) {
            LocalDateTime dateTime = a.getDateTime();
            if (newDateTime.isAfter(dateTime)
                    && newDateTime.isBefore(dateTime.plusMinutes(30))
                    && !dateTime.equals(oldDateTime)) {
                return true;
            }
            if (newDateTime.isBefore(dateTime)
                    && newDateTime.plusMinutes(30).isAfter(dateTime)
                    && !dateTime.equals(oldDateTime)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updatePetPatient(PetPatient target, PetPatient editedPetPatient)
            throws DuplicatePetPatientException, PetPatientNotFoundException {
        requireAllNonNull(target, editedPetPatient);

        addressBook.updatePetPatient(target, editedPetPatient);
        indicateAddressBookChanged();
    }

    @Override
    public void updateAppointment(Appointment target, Appointment editedAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireAllNonNull(target, editedAppointment);

        addressBook.updateAppointment(target, editedAppointment);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    /**
     * Returns an unmodifiable view of the list of {@code PetPatient} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<PetPatient> getFilteredPetPatientList() {
        return FXCollections.unmodifiableObservableList(filteredPetPatients);
    }

    @Override
    public void updateFilteredPetPatientList(Predicate<PetPatient> predicate) {
        requireNonNull(predicate);
        filteredPetPatients.setPredicate(predicate);
    }

```
###### \java\seedu\address\model\petpatient\BloodType.java
``` java
/**
 * Represents a PetPatient's blood type in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBloodType(String)}
 */
public class BloodType {

    public static final String MESSAGE_PET_BLOODTYPE_CONSTRAINTS =
            "Pet Patient blood type should only contain alphabetic characters, punctuations and spaces, "
                    + "and it should not be blank.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String BLOODTYPE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}\\p{Punct}\\p{Blank}]*";

    public final String bloodType;

    /**
     * Constructs a {@code BloodType}.
     *
     * @param bloodType A valid bloodType.
     */
    public BloodType(String bloodType) {
        requireNonNull(bloodType);
        checkArgument(isValidBloodType(bloodType), MESSAGE_PET_BLOODTYPE_CONSTRAINTS);
        this.bloodType = bloodType;
    }

    /**
     * Returns true if a given string is a valid bloodType.
     */
    public static boolean isValidBloodType(String test) {
        return test.matches(BLOODTYPE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return bloodType;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof BloodType // instanceof handles nulls
                && this.bloodType.equals(((BloodType) other).bloodType)); // state check
    }

    @Override
    public int hashCode() {
        return bloodType.hashCode();
    }
}
```
###### \java\seedu\address\model\petpatient\Breed.java
``` java
/**
 * Represents a PetPatient's breed in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBreed(String)}
 */
public class Breed {

    public static final String MESSAGE_PET_BREED_CONSTRAINTS =
            "Pet Patient breed should only contain alphabetic characters and spaces, and it should not be blank.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String BREED_VALIDATION_REGEX = "[\\p{Alpha}][\\p{Alpha} ]*";

    public final String breed;

    /**
     * Constructs a {@code Breed}.
     *
     * @param breed A valid breed.
     */
    public Breed(String breed) {
        requireNonNull(breed);
        checkArgument(isValidBreed(breed), MESSAGE_PET_BREED_CONSTRAINTS);
        this.breed = breed;
    }

    /**
     * Returns true if a given string is a valid breed.
     */
    public static boolean isValidBreed(String test) {
        return test.matches(BREED_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return breed;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Breed // instanceof handles nulls
                && this.breed.equals(((Breed) other).breed)); // state check
    }

    @Override
    public int hashCode() {
        return breed.hashCode();
    }
}
```
###### \java\seedu\address\model\petpatient\Colour.java
``` java
/**
 * Represents a PetPatient's colour in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidColour(String)}
 */
public class Colour {

    public static final String MESSAGE_PET_COLOUR_CONSTRAINTS =
            "Pet Patient colour should only contain alphabetic characters and spaces, and it should not be blank.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String COLOUR_VALIDATION_REGEX = "[\\p{Alpha}][\\p{Alpha} ]*";

    public final String colour;

    /**
     * Constructs a {@code Colour}.
     *
     * @param colour A valid colour.
     */
    public Colour(String colour) {
        requireNonNull(colour);
        checkArgument(isValidColour(colour), MESSAGE_PET_COLOUR_CONSTRAINTS);
        this.colour = colour;
    }

    /**
     * Returns true if a given string is a valid colour.
     */
    public static boolean isValidColour(String test) {
        return test.matches(COLOUR_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return colour;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Colour // instanceof handles nulls
                && this.colour.equals(((Colour) other).colour)); // state check
    }

    @Override
    public int hashCode() {
        return colour.hashCode();
    }
}
```
###### \java\seedu\address\model\petpatient\exceptions\DuplicatePetPatientException.java
``` java
/**
 * Signals that the operation will result in duplicate PetPatient objects.
 */
public class DuplicatePetPatientException extends DuplicateDataException {
    public DuplicatePetPatientException() {
        super("Operation would result in duplicate pet patients");
    }
}
```
###### \java\seedu\address\model\petpatient\exceptions\PetPatientNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified pet patient.
 */
public class PetPatientNotFoundException extends Exception {
}
```
###### \java\seedu\address\model\petpatient\PetPatient.java
``` java
/**
 * Represents a PetPatient in the address book.
 * Guarantees: details are present, field values are validated.
 */
public class PetPatient {
    private final PetPatientName name;
    private final Species species; // e.g. dogs, cats, birds, etc.
    private final Breed breed; // different varieties of the same species
    private final Colour colour;
    private final BloodType bloodType;

    private final UniqueTagList tags;

    private final Optional<Date> dateOfBirth; // can be null
    private Nric ownerNric; // can be null (initially)
    private StringBuilder medicalHistory; // can be null (initially)

    //keep this constructor, as owner NRIC can be null initially when adding a new PetPatient
    public PetPatient(PetPatientName name,
                      Species species,
                      Breed breed,
                      Colour colour,
                      BloodType bloodType,
                      Set<Tag> tags) {
        requireAllNonNull(name, species, breed, colour, bloodType, tags);
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.bloodType = bloodType;
        this.tags = new UniqueTagList(tags);

        this.ownerNric = null;
        this.dateOfBirth = null;
        this.medicalHistory = new StringBuilder();
    }

    public PetPatient(PetPatientName name,
                      Species species,
                      Breed breed,
                      Colour colour,
                      BloodType bloodType,
                      Nric ownerNric,
                      Set<Tag> tags) {
        requireAllNonNull(name, species, breed, colour, bloodType, tags);
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.bloodType = bloodType;
        this.tags = new UniqueTagList(tags);
        this.ownerNric = ownerNric;
        this.dateOfBirth = null;
        this.medicalHistory = new StringBuilder();
    }

    //keep this constructor
    public PetPatient(PetPatientName name,
                      Species species,
                      Breed breed,
                      Colour colour,
                      BloodType bloodType,
                      Nric ownerNric,
                      Optional<Date> dateOfBirth,
                      Set<Tag> tags) {
        requireAllNonNull(name, species, breed, colour, bloodType, ownerNric, dateOfBirth, tags);
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.bloodType = bloodType;
        this.ownerNric = ownerNric;
        this.dateOfBirth = dateOfBirth;
        this.tags = new UniqueTagList(tags);
        this.medicalHistory = new StringBuilder();
    }

    public PetPatientName getName() {
        return name;
    }

    public Optional<Date> getDateOfBirth() {
        return dateOfBirth;
    }

    public Species getSpecies() {
        return species;
    }

    public Breed getBreed() {
        return breed;
    }

    public Colour getColour() {
        return colour;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public Nric getOwner() {
        return ownerNric;
    }

    public void setOwnerNric(Nric ownerNric) {
        this.ownerNric = ownerNric;
    }

    public StringBuilder getMedicalHistory() {
        return medicalHistory;
    }

    public void updateMedicalHistory(String newContent) {
        this.medicalHistory.append(newContent);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    /**
     * Returns a list of tags as a string, for find command.
     */
    public String getTagString() {
        StringBuilder tagString = new StringBuilder();
        Set<Tag> tagSet = Collections.unmodifiableSet(tags.toSet());
        for (Tag tag : tagSet) {
            tagString.append(tag.tagName);
            tagString.append(" ");
        }
        return tagString.toString().trim();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PetPatient)) {
            return false;
        }

        PetPatient otherPetPatient = (PetPatient) other;
        return otherPetPatient.getName().equals(this.getName())
                && otherPetPatient.getSpecies().equals(this.getSpecies())
                && otherPetPatient.getBreed().equals(this.getBreed())
                && otherPetPatient.getColour().equals(this.getColour())
                && otherPetPatient.getBloodType().equals(this.getBloodType())
                && otherPetPatient.getOwner().equals(this.getOwner());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, species, breed, colour, bloodType, tags, ownerNric);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\t")
                .append(getName())
                .append("\tSpecies: ")
                .append(getSpecies())
                .append("\tBreed: ")
                .append(getBreed())
                .append("\tColor: ")
                .append(getColour())
                .append("\tBlood Type: ")
                .append(getBloodType())
                .append("\t\tOwner's NRIC: ")
                .append(getOwner())
                .append("\tTags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
```
###### \java\seedu\address\model\petpatient\PetPatientName.java
``` java
/**
 * Represents a PetPatient's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class PetPatientName {

    public static final String MESSAGE_PET_NAME_CONSTRAINTS =
            "Pet Patient names should only contain alphanumeric characters and spaces, and it should not be blank.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code PetPatientName}.
     *
     * @param name A valid name.
     */
    public PetPatientName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_PET_NAME_CONSTRAINTS);
        this.fullName = name;
    }

    /**
     * Returns true if a given string is a valid pet patient name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PetPatientName // instanceof handles nulls
                && this.fullName.equals(((PetPatientName) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
```
###### \java\seedu\address\model\petpatient\Species.java
``` java
/**
 * Represents a PetPatient's species in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSpecies(String)}
 */
public class Species {

    public static final String MESSAGE_PET_SPECIES_CONSTRAINTS =
            "Pet Patient species should only contain alphabetic characters and spaces, and it should not be blank.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String SPECIES_VALIDATION_REGEX = "[\\p{Alpha}][\\p{Alpha} ]*";

    public final String species;

    /**
     * Constructs a {@code Species}.
     *
     * @param species A valid species.
     */
    public Species(String species) {
        requireNonNull(species);
        checkArgument(isValidSpecies(species), MESSAGE_PET_SPECIES_CONSTRAINTS);
        this.species = species;
    }

    /**
     * Returns true if a given string is a valid species.
     */
    public static boolean isValidSpecies(String test) {
        return test.matches(SPECIES_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return species;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Species // instanceof handles nulls
                && this.species.equals(((Species) other).species)); // state check
    }

    @Override
    public int hashCode() {
        return species.hashCode();
    }
}
```
###### \java\seedu\address\model\petpatient\UniquePetPatientList.java
``` java
/**
 * A list of pet patients that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see PetPatient#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePetPatientList implements Iterable<PetPatient> {
    private final ObservableList<PetPatient> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent pet patient as the given argument.
     */
    public boolean contains(PetPatient toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a pet patient to the list.
     *
     * @throws DuplicatePetPatientException if the pet patient to add is a duplicate of an existing pet patient
     * in the list.
     */
    public void add(PetPatient toAdd) throws DuplicatePetPatientException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePetPatientException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the pet patient {@code target} in the list with {@code editedPetPatient}.
     *
     * @throws DuplicatePetPatientException if the replacement is equivalent to another existing pet patient
     * in the list.
     * @throws PetPatientNotFoundException if {@code target} could not be found in the list.
     */
    public void setPetPatient(PetPatient target, PetPatient editedPetPatient)
            throws DuplicatePetPatientException, PetPatientNotFoundException {
        requireNonNull(editedPetPatient);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PetPatientNotFoundException();
        }

        if (!target.equals(editedPetPatient) && internalList.contains(editedPetPatient)) {
            throw new DuplicatePetPatientException();
        }

        internalList.set(index, editedPetPatient);
    }

    /**
     * Removes the equivalent pet patient from the list.
     *
     * @throws PetPatientNotFoundException if no such pet patient could be found in the list.
     */
    public boolean remove(PetPatient toRemove) throws PetPatientNotFoundException {
        requireNonNull(toRemove);
        final boolean petPatientFoundAndDeleted = internalList.remove(toRemove);
        if (!petPatientFoundAndDeleted) {
            throw new PetPatientNotFoundException();
        }
        return petPatientFoundAndDeleted;
    }

    public void setPetPatients(UniquePetPatientList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPetPatients(List<PetPatient> petPatients) throws DuplicatePetPatientException {
        requireAllNonNull(petPatients);
        final UniquePetPatientList replacement = new UniquePetPatientList();
        for (final PetPatient petPatient : petPatients) {
            replacement.add(petPatient);
        }
        setPetPatients(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<PetPatient> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<PetPatient> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePetPatientList // instanceof handles nulls
                && this.internalList.equals(((UniquePetPatientList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedPetPatient.java
``` java
/**
 * JAXB-friendly version of the PetPatient.
 */
public class XmlAdaptedPetPatient {
    public static final String MISSING_NAME_FIELD_MESSAGE_FORMAT = "Pet patient's name field is missing!";
    public static final String MISSING_SPECIES_FIELD_MESSAGE_FORMAT = "Pet patient's species field is missing!";
    public static final String MISSING_BREED_FIELD_MESSAGE_FORMAT = "Pet patient's breed field is missing!";
    public static final String MISSING_COLOUR_FIELD_MESSAGE_FORMAT = "Pet patient's colour field is missing!";
    public static final String MISSING_BLOODTYPE_FIELD_MESSAGE_FORMAT = "Pet patient's blood type field is missing!";
    public static final String MISSING_OWNER_FIELD_MESSAGE_FORMAT = "Pet patient's owner field is missing!";


    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String species;
    @XmlElement(required = true)
    private String breed;
    @XmlElement(required = true)
    private String colour;
    @XmlElement(required = true)
    private String bloodType;
    @XmlElement(required = true)
    private String ownerNric;

    @XmlElement
    private String dateOfBirth;
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();
    @XmlElement
    private String medicalHistory;

    /**
     * Constructs an XmlAdaptedPetPatient.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPetPatient() {}

    /**
     * Constructs an {@code XmlAdaptedPetPatient} with the given pet patient details.
     */
    public XmlAdaptedPetPatient(String name, String species, String breed, String colour,
                            String bloodType, String ownerNric, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.bloodType = bloodType;
        this.ownerNric = ownerNric;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given PetPatient into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPetPatient
     */
    public XmlAdaptedPetPatient(PetPatient source) {
        name = source.getName().fullName;
        species = source.getSpecies().species;
        breed = source.getBreed().breed;
        colour = source.getColour().colour;
        bloodType = source.getBloodType().bloodType;
        ownerNric = source.getOwner().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted pet patient object into the model's PetPatient object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted pet patient
     */
    public PetPatient toModelType() throws IllegalValueException {
        final List<Tag> petPatientTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            petPatientTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_NAME_FIELD_MESSAGE_FORMAT, PetPatientName.class.getSimpleName()));
        }
        if (!PetPatientName.isValidName(this.name)) {
            throw new IllegalValueException(PetPatientName.MESSAGE_PET_NAME_CONSTRAINTS);
        }
        final PetPatientName name = new PetPatientName(this.name);

        if (this.species == null) {
            throw new IllegalValueException(
                    String.format(MISSING_SPECIES_FIELD_MESSAGE_FORMAT, Species.class.getSimpleName()));
        }
        if (!Species.isValidSpecies(this.species)) {
            throw new IllegalValueException(Species.MESSAGE_PET_SPECIES_CONSTRAINTS);
        }
        final Species species = new Species(this.species);

        if (this.breed == null) {
            throw new IllegalValueException(
                    String.format(MISSING_BREED_FIELD_MESSAGE_FORMAT, Breed.class.getSimpleName()));
        }
        if (!Breed.isValidBreed(this.breed)) {
            throw new IllegalValueException(Breed.MESSAGE_PET_BREED_CONSTRAINTS);
        }
        final Breed breed = new Breed(this.breed);

        if (this.colour == null) {
            throw new IllegalValueException(
                    String.format(MISSING_COLOUR_FIELD_MESSAGE_FORMAT, Colour.class.getSimpleName()));
        }
        if (!Colour.isValidColour(this.colour)) {
            throw new IllegalValueException(Colour.MESSAGE_PET_COLOUR_CONSTRAINTS);
        }
        final Colour colour = new Colour(this.colour);

        if (this.bloodType == null) {
            throw new IllegalValueException(String.format(MISSING_BLOODTYPE_FIELD_MESSAGE_FORMAT));
        }
        if (!BloodType.isValidBloodType(this.bloodType)) {
            throw new IllegalValueException(BloodType.MESSAGE_PET_BLOODTYPE_CONSTRAINTS);
        }
        final BloodType bloodType = new BloodType(this.bloodType);

        if (this.ownerNric == null) {
            throw new IllegalValueException(
                    String.format(MISSING_OWNER_FIELD_MESSAGE_FORMAT, PetPatientName.class.getSimpleName()));
        }
        if (!Nric.isValidNric(this.ownerNric)) {
            throw new IllegalValueException(Nric.MESSAGE_NRIC_CONSTRAINTS);
        }
        final Nric ownerNric = new Nric(this.ownerNric);

        final Set<Tag> tags = new HashSet<>(petPatientTags);
        return new PetPatient(name, species, breed, colour, bloodType, ownerNric, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPetPatient)) {
            return false;
        }

        XmlAdaptedPetPatient otherPetPatient = (XmlAdaptedPetPatient) other;
        return Objects.equals(name, otherPetPatient.name)
                && Objects.equals(species, otherPetPatient.species)
                && Objects.equals(breed, otherPetPatient.breed)
                && Objects.equals(colour, otherPetPatient.colour)
                && Objects.equals(bloodType, otherPetPatient.bloodType)
                && Objects.equals(ownerNric, otherPetPatient.ownerNric)
                && tagged.equals(otherPetPatient.tagged);
    }
}
```
