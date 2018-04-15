# wynonaK
###### \java\seedu\address\logic\commands\DeleteCommandTest.java
``` java
    @Test
    public void executeDeleteOwnerWithTiedPet_validIndexUnfilteredList_failure() throws Exception {
        model.addPetPatient(TypicalPetPatients.JEWEL);
        DeleteCommand deleteCommand = prepareDeleteOwnerCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_DEPENDENCIES_EXIST);
    }

    @Test
    public void executeDeleteOwnerWithTiedPetWithTiedAppt_validIndexUnfilteredList_failure() throws Exception {
        model.addPetPatient(TypicalPetPatients.JEWEL);
        model.addAppointment(TypicalAppointments.ALICE_APP);
        DeleteCommand deleteCommand = prepareDeleteOwnerCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_DEPENDENCIES_EXIST);
    }

```
###### \java\seedu\address\logic\commands\DeleteCommandTest.java
``` java
    @Test
    public void executeDeleteForceOwner_validIndexUnfilteredList_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = prepareDeleteForceOwnerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeDeleteForceOwnerWithTiedPet_validIndexUnfilteredList_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PetPatient petPatientDependent = TypicalPetPatients.JEWEL;
        model.addPetPatient(petPatientDependent);
        DeleteCommand deleteCommand = prepareDeleteForceOwnerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete)
                + "\n"
                + String.format(DeleteCommand.MESSAGE_DELETE_PET_PATIENT_SUCCESS, petPatientDependent);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePetPatient(petPatientDependent);
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeDeleteForceOwnerWithTiedPetAndAppt_validIndexUnfilteredList_success() throws Exception {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PetPatient petPatientDependent = TypicalPetPatients.JEWEL;
        model.addPetPatient(petPatientDependent);
        Appointment appointmentDependent = TypicalAppointments.ALICE_APP;
        model.addAppointment(appointmentDependent);
        DeleteCommand deleteCommand = prepareDeleteForceOwnerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete)
                + "\n"
                + String.format(DeleteCommand.MESSAGE_DELETE_PET_PATIENT_SUCCESS, petPatientDependent)
                + "\n"
                + String.format(DeleteCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS, appointmentDependent);


        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAppointment(appointmentDependent);
        expectedModel.deletePetPatient(petPatientDependent);
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeDeletePetPatient_validIndexUnfilteredList_success() throws Exception {
        model.addPetPatient(TypicalPetPatients.JEWEL);
        model.addPetPatient(TypicalPetPatients.JOKER);
        PetPatient petPatientToDelete = model.getFilteredPetPatientList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = prepareDeletePetPatientCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PET_PATIENT_SUCCESS, petPatientToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePetPatient(petPatientToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeDeletePetPatientWithTiedApppointment_validIndexUnfilteredList_failure() throws Exception {
        model.addPetPatient(TypicalPetPatients.JOKER);
        model.addAppointment(TypicalAppointments.BENSON_APP);
        DeleteCommand deleteCommand = prepareDeletePetPatientCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_DEPENDENCIES_EXIST);
    }

    @Test
    public void executeDeleteForcePetPatient_validIndexUnfilteredList_success() throws Exception {
        model.addPetPatient(TypicalPetPatients.JEWEL);
        PetPatient petPatientToDelete = model.getFilteredPetPatientList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = prepareDeleteForcePetPatientCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PET_PATIENT_SUCCESS, petPatientToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePetPatient(petPatientToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeDeleteForcePetPatientWithTiedApppointment_validIndexUnfilteredList_success() throws Exception {
        model.addPetPatient(TypicalPetPatients.JOKER);
        Appointment appointment = TypicalAppointments.BENSON_APP;
        model.addAppointment(TypicalAppointments.BENSON_APP);
        PetPatient petPatientToDelete = model.getFilteredPetPatientList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = prepareDeleteForcePetPatientCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PET_PATIENT_SUCCESS, petPatientToDelete)
                + "\n"
                + String.format(DeleteCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS, appointment);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAppointment(appointment);
        expectedModel.deletePetPatient(petPatientToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeDeleteAppointment_validIndexUnfilteredList_success() throws Exception {
        model.addPetPatient(TypicalPetPatients.JEWEL);
        model.addPetPatient(TypicalPetPatients.JOKER);
        model.addAppointment(TypicalAppointments.ALICE_APP);
        model.addAppointment(TypicalAppointments.BENSON_APP);
        Appointment appointmentToDelete = model.getFilteredAppointmentList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = prepareDeleteAppointmentCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS, appointmentToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAppointment(appointmentToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeDeleteExecuteInvalidIndex_throwsException() throws Exception {
        thrown.expect(NullPointerException.class);
        new DeleteCommand(6, null).executeUndoableCommand();
    }

    @Test
    public void executeDeletePreProcessInvalidIndex_throwsException() throws Exception {
        thrown.expect(CommandException.class);
        new DeleteCommand(6, null).preprocessUndoableCommand();
    }

```
###### \java\seedu\address\logic\commands\DeleteCommandTest.java
``` java
    @Test
    public void executeDeleteForceOwner_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = prepareDeleteForceOwnerCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
    }

    @Test
    public void executeDeletePetPatient_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPetPatientList().size() + 1);
        DeleteCommand deleteCommand = prepareDeletePetPatientCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
    }

    @Test
    public void executeDeleteForcePetPatient_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPetPatientList().size() + 1);
        DeleteCommand deleteCommand = prepareDeleteForcePetPatientCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
    }

    @Test
    public void executeDeleteAppointment_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);
        DeleteCommand deleteCommand = prepareDeleteAppointmentCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
    }

```
###### \java\seedu\address\logic\commands\DeleteCommandTest.java
``` java
    @Test
    public void executeForce_validIndexFilteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = prepareDeleteForceOwnerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, personToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }
```
###### \java\seedu\address\logic\commands\DeleteCommandTest.java
``` java
    @Test
    public void executeForce_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = prepareDeleteForceOwnerCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
    }
```
###### \java\seedu\address\logic\commands\DeleteCommandTest.java
``` java
    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareDeleteOwnerCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(1, index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareDeleteForceOwnerCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(4, index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareDeletePetPatientCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(2, index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareDeleteForcePetPatientCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(5, index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareDeleteAppointmentCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(3, index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

```
###### \java\seedu\address\logic\commands\FindCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        HashMap<String, String[]> first = new HashMap<>();
        String[] firstKeyword = {""};
        first.put("", firstKeyword);
        HashMap<String, String[]> second = new HashMap<>();
        String[] secondKeyword = {""};
        second.put("", secondKeyword);

        FindCommand findFirstCommand = new FindCommand(first);
        FindCommand findSecondCommand = new FindCommand(second);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(first);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_allPresent_personFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Kurz"};
        String[] nric = {"F2345678U"};
        String[] tag = {"friends"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("ownerName", name);
        hashMap.put("ownerNric", nric);
        hashMap.put("ownerTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL));
    }

    @Test
    public void execute_nonExistentNameKeyword_personFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Kurzaad"};
        String[] nric = {"F2345678U"};
        String[] tag = {"friends"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("ownerName", name);
        hashMap.put("ownerNric", nric);
        hashMap.put("ownerTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_nonExistentNricKeyword_personFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Kurz"};
        String[] nric = {"F2981391U"};
        String[] tag = {"friends"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("ownerName", name);
        hashMap.put("ownerNric", nric);
        hashMap.put("ownerTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_nonExistentTagKeyword_personFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Kurz"};
        String[] nric = {"F2345678U"};
        String[] tag = {"friendstoo"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("ownerName", name);
        hashMap.put("ownerNric", nric);
        hashMap.put("ownerTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroNameKeywords_noPersonFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePersonNameCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroNricKeywords_noPersonFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePersonNricCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroTagKeywords_noPersonFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePersonTagCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroNameKeywords_noPetPatientFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePetNameCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroSpeciesKeywords_noPetPatientFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePetSpeciesCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroBreedKeywords_noPetPatientFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePetBreedCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroColorKeywords_noPetPatientFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePetColorCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroBloodTypeKeywords_noPetPatientFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePetBloodTypeCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_zeroTagKeywords_noPetPatientFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePetTagCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_nameKeyword_personsFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePersonNameCommand("Kurz");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL));
    }

    @Test
    public void execute_multipleNameKeywords_multiplePersonsFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePersonNameCommand("Kurz Elle Kunz");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));
    }

    @Test
    public void execute_nricKeyword_personFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePersonNricCommand("F2345678U");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL));
    }

    @Test
    public void execute_multipleNricKeywords_multiplePersonsFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);
        FindCommand command = preparePersonNricCommand("F2345678U T0120956W S0156956W");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));
    }

    @Test
    public void execute_tagKeyword_personFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 2);
        FindCommand command = preparePersonTagCommand("owesMoney");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_multipleTagKeyword_multiplePersonsFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 3);
        FindCommand command = preparePersonTagCommand("friends owesMoney");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    @Test
    public void execute_petAllFields_personFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 1);

        String[] name = {"Jewel"};
        String[] species = {"Cat"};
        String[] breed = {"Persian"};
        String[] colour = {"Calico"};
        String[] bloodType = {"AB"};
        String[] tag = {"Depression"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("petName", name);
        hashMap.put("petSpecies", species);
        hashMap.put("petBreed", breed);
        hashMap.put("petColour", colour);
        hashMap.put("petBloodType", bloodType);
        hashMap.put("petTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE));
    }

    @Test
    public void executePetAllFields_noFoundName_noPetFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Jewellish"};
        String[] species = {"Cat"};
        String[] breed = {"Persian"};
        String[] colour = {"Calico"};
        String[] bloodType = {"AB"};
        String[] tag = {"Depressions"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("petName", name);
        hashMap.put("petSpecies", species);
        hashMap.put("petBreed", breed);
        hashMap.put("petColour", colour);
        hashMap.put("petBloodType", bloodType);
        hashMap.put("petTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void executePetAllFields_noFoundSpecies_noPetFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Jewel"};
        String[] species = {"Dog"};
        String[] breed = {"Persian"};
        String[] colour = {"Calico"};
        String[] bloodType = {"AB"};
        String[] tag = {"Depressions"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("petName", name);
        hashMap.put("petSpecies", species);
        hashMap.put("petBreed", breed);
        hashMap.put("petColour", colour);
        hashMap.put("petBloodType", bloodType);
        hashMap.put("petTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void executePetAllFields_noFoundBreed_noPetFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Jewel"};
        String[] species = {"Cat"};
        String[] breed = {"Shorthair"};
        String[] colour = {"Calico"};
        String[] bloodType = {"AB"};
        String[] tag = {"Depressions"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("petName", name);
        hashMap.put("petSpecies", species);
        hashMap.put("petBreed", breed);
        hashMap.put("petColour", colour);
        hashMap.put("petBloodType", bloodType);
        hashMap.put("petTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void executePetAllFields_noFoundColour_noPetFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Jewel"};
        String[] species = {"Cat"};
        String[] breed = {"Persian"};
        String[] colour = {"Purple"};
        String[] bloodType = {"AB"};
        String[] tag = {"Depressions"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("petName", name);
        hashMap.put("petSpecies", species);
        hashMap.put("petBreed", breed);
        hashMap.put("petColour", colour);
        hashMap.put("petBloodType", bloodType);
        hashMap.put("petTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void executePetAllFields_noFoundBloodType_noPetFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Jewel"};
        String[] species = {"Cat"};
        String[] breed = {"Persian"};
        String[] colour = {"Calico"};
        String[] bloodType = {"ABD"};
        String[] tag = {"Depressions"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("petName", name);
        hashMap.put("petSpecies", species);
        hashMap.put("petBreed", breed);
        hashMap.put("petColour", colour);
        hashMap.put("petBloodType", bloodType);
        hashMap.put("petTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void executePetAllFields_noFoundTag_noPetFound() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 0);

        String[] name = {"Jewel"};
        String[] species = {"Cat"};
        String[] breed = {"Persian"};
        String[] colour = {"Calico"};
        String[] bloodType = {"AB"};
        String[] tag = {"Depressionsss"};

        HashMap<String, String[]> hashMap = new HashMap<>();

        hashMap.put("petName", name);
        hashMap.put("petSpecies", species);
        hashMap.put("petBreed", breed);
        hashMap.put("petColour", colour);
        hashMap.put("petBloodType", bloodType);
        hashMap.put("petTag", tag);

        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_petNameKeyword_personFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 1);
        FindCommand command = preparePetNameCommand("Joker");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_multipleNameKeyword_multiplePersonsFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 2);
        FindCommand command = preparePetNameCommand("Jewel Joker");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    @Test
    public void execute_petSpeciesKeyword_personFoundForPetDog() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 1);
        FindCommand command = preparePetSpeciesCommand("Dog");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_petSpeciesKeyword_multiplePersonFoundForPetCat() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 2);
        FindCommand command = preparePetSpeciesCommand("Cat");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    @Test
    public void execute_multiplePetSpeciesKeyword_multiplePersonFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 3);
        FindCommand command = preparePetSpeciesCommand("Dog Cat");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    @Test
    public void execute_breedKeyword_personFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 1);
        FindCommand command = preparePetBreedCommand("Golden Retriever");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_multipleBreedKeyword_multiplePersonFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 3);
        FindCommand command = preparePetBreedCommand("Persian Ragdoll Golden Retriever Domestic Shorthair");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    @Test
    public void execute_colorKeyword_personFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 1);
        FindCommand command = preparePetColorCommand("Brown");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_multiplePetColorKeyword_multiplePersonFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 3);
        FindCommand command = preparePetColorCommand("Calico Brown Golden");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    @Test
    public void execute_bloodTypeKeyword_personFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 1);
        FindCommand command = preparePetBloodTypeCommand("AB");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE));
    }

    @Test
    public void execute_multipleBloodTypeKeyword_multiplePersonFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 3);
        FindCommand command = preparePetBloodTypeCommand("AB A DEA 4+");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    @Test
    public void execute_tagKeyword_personFoundForPet() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 1);
        FindCommand command = preparePetTagCommand("3legged");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BENSON));
    }

    @Test
    public void execute_multipleTagKeyword_multiplePersonsFoundForPets() throws CommandException {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + "\n"
                + String.format(MESSAGE_PET_PATIENTS_LISTED_OVERVIEW, 2);
        FindCommand command = preparePetTagCommand("Depression 3Legged");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON));
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePersonNameCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("ownerName", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePersonNricCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("ownerNric", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePersonTagCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("ownerTag", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePetNameCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("petName", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePetSpeciesCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("petSpecies", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePetBreedCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("petBreed", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePetColorCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("petColour", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePetBloodTypeCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("petBloodType", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand preparePetTagCommand(String userInput) {
        String[] split = userInput.split("\\s+");
        HashMap<String, String[]> hashMap = new HashMap<>();
        hashMap.put("petTag", split);
        FindCommand command = new FindCommand(hashMap);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCommand command, String expectedMessage, List<Person> expectedList)
            throws CommandException {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage}
     */
    public static void assertCommandFailure(Command command, String expectedMessage) {
        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
```
###### \java\seedu\address\logic\commands\ListAppointmentCommandTest.java
``` java
public class ListAppointmentCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_getYear_success() throws CommandException {
        CommandResult result = new ListAppointmentCommand(1, Year.now()).execute();
        assertEquals(String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "year"), result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ChangeYearViewRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_getMonth_success() throws CommandException {
        CommandResult result = new ListAppointmentCommand(2, YearMonth.now()).execute();
        assertEquals(String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "month"), result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ChangeMonthViewRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_getWeek_success() throws CommandException {
        CommandResult result = new ListAppointmentCommand(3, LocalDate.now()).execute();
        assertEquals(String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "week"), result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ChangeWeekViewRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_getDate_success() throws CommandException {
        CommandResult result = new ListAppointmentCommand(4, LocalDate.now()).execute();
        assertEquals(String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "day"), result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ChangeDayViewRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_getPastYearNoAppt_failure() throws CommandException {
        Year year = Year.of(2017);
        thrown.expect(NullPointerException.class);
        new ListAppointmentCommand(1, year).execute();
    }

    @Test
    public void execute_getPastMonthYearNoAppt_failure() throws CommandException {
        YearMonth yearMonth = YearMonth.of(2017, 01);
        thrown.expect(NullPointerException.class);
        new ListAppointmentCommand(2, yearMonth).execute();
    }

    @Test
    public void execute_getPastWeekNoAppt_failure() throws CommandException {
        LocalDate date = LocalDate.of(2017, 01, 01);
        thrown.expect(NullPointerException.class);
        new ListAppointmentCommand(3, date).execute();
    }

    @Test
    public void execute_getPastDayNoAppt_failure() throws CommandException {
        LocalDate date = LocalDate.of(2017, 01, 01);
        thrown.expect(NullPointerException.class);
        new ListAppointmentCommand(4, date).execute();
    }

    @Test
    public void execute_getPast_failure() throws CommandException {
        LocalDate date = LocalDate.of(2017, 01, 01);
        thrown.expect(CommandException.class);
        new ListAppointmentCommand(5, date).execute();
    }

}
```
###### \java\seedu\address\logic\parser\DeleteCommandParserTest.java
``` java
    @Test
    public void parse_validOwnerArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "-o 1", new DeleteCommand(1, INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validForceOwnerArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "-fo 1", new DeleteCommand(4, INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validPetArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "-p 1", new DeleteCommand(2, INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validForcePetArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "-fp 1", new DeleteCommand(5, INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validAppointmentArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "-a 1", new DeleteCommand(3, INDEX_FIRST_PERSON));
    }

```
###### \java\seedu\address\logic\parser\FindCommandParserTest.java
``` java
    @Test
    public void parse_onlyOwnerOption_throwsParseException() {
        assertParseFailure(parser, " -o ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankNameOption_throwsParseException() {
        assertParseFailure(parser, " -o n/ ",
                String.format(Name.MESSAGE_NAME_CONSTRAINTS));
    }

    @Test
    public void parse_blankNricOption_throwsParseException() {
        assertParseFailure(parser, " -o nr/ ",
                String.format(Nric.MESSAGE_NRIC_CONSTRAINTS));
    }

    @Test
    public void parse_blankTagOption_throwsParseException() {
        assertParseFailure(parser, " -o t/ ",
                String.format(Tag.MESSAGE_TAG_CONSTRAINTS));
    }

    @Test
    public void parse_onlyPetOption_throwsParseException() {
        assertParseFailure(parser, " -p ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_blankPetNameOption_throwsParseException() {
        assertParseFailure(parser, " -p n/ ",
                String.format(PetPatientName.MESSAGE_PET_NAME_CONSTRAINTS));
    }

    @Test
    public void parse_blankSpeciesOption_throwsParseException() {
        assertParseFailure(parser, " -p s/ ",
                String.format(Species.MESSAGE_PET_SPECIES_CONSTRAINTS));
    }

    @Test
    public void parse_blankBreedOption_throwsParseException() {
        assertParseFailure(parser, " -p b/ ",
                String.format(Breed.MESSAGE_PET_BREED_CONSTRAINTS));
    }

    @Test
    public void parse_blankColourOption_throwsParseException() {
        assertParseFailure(parser, " -p c/ ",
                String.format(Colour.MESSAGE_PET_COLOUR_CONSTRAINTS));
    }

    @Test
    public void parse_blankBloodTypeOption_throwsParseException() {
        assertParseFailure(parser, " -p bt/ ",
                String.format(BloodType.MESSAGE_PET_BLOODTYPE_CONSTRAINTS));
    }

    @Test
    public void parse_blankPetTagOption_throwsParseException() {
        assertParseFailure(parser, " -p t/ ",
                String.format(Tag.MESSAGE_TAG_CONSTRAINTS));
    }
}
```
###### \java\seedu\address\logic\parser\ListAppointmentCommandParserTest.java
``` java
public class ListAppointmentCommandParserTest {
    private ListAppointmentCommandParser parser = new ListAppointmentCommandParser();

    @Test
    public void parse_fieldsExist_success() {
        assertParseSuccess(parser, " -y 2018 ", new ListAppointmentCommand(1, Year.of(2018)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        assertParseSuccess(parser, " -m 2018-12 ",
                new ListAppointmentCommand(2, YearMonth.parse("2018-12", formatter)));

        YearMonth date = YearMonth.now().withMonth(12);
        assertParseSuccess(parser, " -m 12 ",
                new ListAppointmentCommand(2, date));

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertParseSuccess(parser, " -w 2018-12-31 ",
                new ListAppointmentCommand(3, LocalDate.parse("2018-12-31", formatter)));


        assertParseSuccess(parser, " -d 2018-12-31 ",
                new ListAppointmentCommand(4, LocalDate.parse("2018-12-31", formatter)));
    }

    @Test
    public void parse_fieldsAbsentButOptionExist_success() {
        assertParseSuccess(parser, " -y ",
                new ListAppointmentCommand(1, Year.now()));

        assertParseSuccess(parser, " -m ",
                new ListAppointmentCommand(2, YearMonth.now()));

        assertParseSuccess(parser, " -w ",
                new ListAppointmentCommand(3, LocalDate.now()));

        assertParseSuccess(parser, " -d ",
                new ListAppointmentCommand(4, LocalDate.now()));
    }

    @Test
    public void parse_optionsAbsent_failure() {
        assertParseFailure(parser, " ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_optionsInvalid_failure() {
        assertParseFailure(parser, " -ajsdbiuaeih ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_fieldsInvalid_failure() {
        assertParseFailure(parser, " -y jadfoijnoiem ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " -m jadfoijnoiem ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " -w jadfoijnoiem ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " -d jadfoijnoiem ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

}
```
###### \java\seedu\address\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseDateTime_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDateTime((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDateTime((Optional<String>) null));
    }

    @Test
    public void parseDateTime_invalidDateTimeIncomplete_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseDateTime(INVALID_DATETIME_INCOMPLETE));
        Assert.assertThrows(IllegalValueException.class, (
            ) -> ParserUtil.parseDateTime(Optional.of(INVALID_DATETIME_INCOMPLETE)));
    }

    @Test
    public void parseDateTime_invalidDate_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseDateTime(INVALID_DATETIME_DATE));
        Assert.assertThrows(IllegalValueException.class, (
        ) -> ParserUtil.parseDateTime(Optional.of(INVALID_DATETIME_DATE)));
    }

    @Test
    public void parseDateTime_invalidTime_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseDateTime(INVALID_DATETIME_TIME));
        Assert.assertThrows(IllegalValueException.class, (
        ) -> ParserUtil.parseDateTime(Optional.of(INVALID_DATETIME_TIME)));
    }

    @Test
    public void parseDateTime_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseDateTime(Optional.empty()).isPresent());
    }

    @Test
    public void parseDateTime_validValueWithoutWhitespace_returnsDateTime() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime expectedLocalDateTime = LocalDateTime.parse(VALID_DATETIME, formatter);
        assertEquals(expectedLocalDateTime, ParserUtil.parseDateTime(VALID_DATETIME));
        assertEquals(Optional.of(expectedLocalDateTime), ParserUtil.parseDateTime(Optional.of(VALID_DATETIME)));
    }

    @Test
    public void parseDateTime_validValueWithWhitespace_returnsTrimmedDateTime() throws Exception {
        String dateTimeWithWhitespace = WHITESPACE + VALID_DATETIME + WHITESPACE;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime expectedLocalDateTime = LocalDateTime.parse(VALID_DATETIME, formatter);
        assertEquals(expectedLocalDateTime, ParserUtil.parseDateTime(dateTimeWithWhitespace));
        assertEquals(Optional.of(expectedLocalDateTime), ParserUtil.parseDateTime(Optional.of(dateTimeWithWhitespace)));
    }

    @Test
    public void parseRemark_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseRemark((String) null));
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseRemark((Optional<String>) null));
    }

    @Test
    public void parseRemark_invalidValue_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseRemark(INVALID_REMARK));
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseRemark(Optional.of(INVALID_REMARK)));
    }

    @Test
    public void parseRemark_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseRemark(Optional.empty()).isPresent());
    }

    @Test
    public void parseRemark_validValueWithoutWhitespace_returnsRemark() throws Exception {
        Remark expectedRemark = new Remark(VALID_REMARK);
        assertEquals(expectedRemark, ParserUtil.parseRemark(VALID_REMARK));
        assertEquals(Optional.of(expectedRemark), ParserUtil.parseRemark(Optional.of(VALID_REMARK)));
    }

    @Test
    public void parseRemark_validValueWithWhitespace_returnsTrimmedRemark() throws Exception {
        String remarkWithWhitespace = WHITESPACE + VALID_REMARK + WHITESPACE;
        Remark expectedRemark = new Remark(VALID_REMARK);
        assertEquals(expectedRemark, ParserUtil.parseRemark(remarkWithWhitespace));
        assertEquals(Optional.of(expectedRemark), ParserUtil.parseRemark(Optional.of(remarkWithWhitespace)));
    }

    @Test
    public void parseYear_null_returnsTodayYear() throws Exception {
        Year expectedYear = Year.now();
        assertEquals(expectedYear, ParserUtil.parseYear(""));
        assertEquals(Optional.of(expectedYear), ParserUtil.parseYear(Optional.of("")));
    }

    @Test
    public void parseYear_invalidYear_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseYear(INVALID_YEAR));
        Assert.assertThrows(IllegalValueException.class, (
        ) -> ParserUtil.parseYear(Optional.of(INVALID_YEAR)));
    }

    @Test
    public void parseYear_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseYear(Optional.empty()).isPresent());
    }

    @Test
    public void parseYear_validValueWithoutWhitespace_returnsYear() throws Exception {
        Year expectedYear = Year.of(2018);
        assertEquals(expectedYear, ParserUtil.parseYear(VALID_YEAR));
        assertEquals(Optional.of(expectedYear), ParserUtil.parseYear(Optional.of(VALID_YEAR)));
    }

    @Test
    public void parseYear_validValueWithWhitespace_returnsTrimmedYear() throws Exception {
        String yearWithWhitespace = WHITESPACE + VALID_YEAR + WHITESPACE;
        Year expectedYear = Year.of(2018);
        assertEquals(expectedYear, ParserUtil.parseYear(yearWithWhitespace));
        assertEquals(Optional.of(expectedYear), ParserUtil.parseYear(Optional.of(yearWithWhitespace)));
    }

    @Test
    public void parseMonth_null_returnsTodayMonth() throws Exception {
        YearMonth expectedYearMonth = YearMonth.now();
        assertEquals(expectedYearMonth, ParserUtil.parseMonth(""));
        assertEquals(Optional.of(expectedYearMonth), ParserUtil.parseMonth(Optional.of("")));
    }

    @Test
    public void parseMonth_invalidMonth_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseMonth(INVALID_YEAR_MONTH));
        Assert.assertThrows(IllegalValueException.class, (
        ) -> ParserUtil.parseMonth(Optional.of(INVALID_YEAR_MONTH)));
    }

    @Test
    public void parseMonth_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseMonth(Optional.empty()).isPresent());
    }

    @Test
    public void parseMonth_validValueWithoutWhitespace_returnsMonth() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth expectedYearMonth = YearMonth.parse(VALID_YEAR_MONTH, formatter);
        assertEquals(expectedYearMonth, ParserUtil.parseMonth(VALID_YEAR_MONTH));
        assertEquals(Optional.of(expectedYearMonth), ParserUtil.parseMonth(Optional.of(VALID_YEAR_MONTH)));
    }

    @Test
    public void parseMonthOnly_validValueWithoutWhitespace_returnsMonth() throws Exception {
        YearMonth expectedYearMonth = YearMonth.now().withMonth(Integer.parseInt(VALID_MONTH));
        assertEquals(expectedYearMonth, ParserUtil.parseMonth(VALID_MONTH));
        assertEquals(Optional.of(expectedYearMonth), ParserUtil.parseMonth(Optional.of(VALID_MONTH)));
    }

    @Test
    public void parseMonth_validValueWithWhitespace_returnsTrimmedMonth() throws Exception {
        String yearMonthWithWhitespace = WHITESPACE + VALID_YEAR_MONTH + WHITESPACE;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth expectedYearMonth = YearMonth.parse(VALID_YEAR_MONTH, formatter);
        assertEquals(expectedYearMonth, ParserUtil.parseMonth(yearMonthWithWhitespace));
        assertEquals(Optional.of(expectedYearMonth), ParserUtil.parseMonth(Optional.of(yearMonthWithWhitespace)));
    }


    @Test
    public void parseMonthOnly_validValueWithWhitespace_returnsTrimmedMonth() throws Exception {
        String yearMonthWithWhitespace = WHITESPACE + VALID_MONTH + WHITESPACE;
        YearMonth expectedYearMonth = YearMonth.now().withMonth(Integer.parseInt(VALID_MONTH));
        assertEquals(expectedYearMonth, ParserUtil.parseMonth(yearMonthWithWhitespace));
        assertEquals(Optional.of(expectedYearMonth), ParserUtil.parseMonth(Optional.of(yearMonthWithWhitespace)));
    }

    @Test
    public void parseDay_null_returnsTodayDay() throws Exception {
        LocalDate expectedLocalDate = LocalDate.now();
        assertEquals(expectedLocalDate, ParserUtil.parseDate(""));
        assertEquals(Optional.of(expectedLocalDate), ParserUtil.parseDate(Optional.of("")));
    }

    @Test
    public void parseDay_invalidDay_throwsIllegalValueException() {
        Assert.assertThrows(IllegalValueException.class, () -> ParserUtil.parseDate(INVALID_DAY));
        Assert.assertThrows(IllegalValueException.class, (
        ) -> ParserUtil.parseYear(Optional.of(INVALID_DAY)));
    }

    @Test
    public void parseDay_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseDate(Optional.empty()).isPresent());
    }

    @Test
    public void parseDay_validValueWithoutWhitespace_returnsDay() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expectedDate = LocalDate.parse(VALID_DAY, formatter);
        assertEquals(expectedDate, ParserUtil.parseDate((VALID_DAY)));
        assertEquals(Optional.of(expectedDate), ParserUtil.parseDate(Optional.of(VALID_DAY)));
    }

    @Test
    public void parseDay_validValueWithWhitespace_returnsTrimmedDay() throws Exception {
        String dayWithWhitespace = WHITESPACE + VALID_DAY + WHITESPACE;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expectedDate = LocalDate.parse(VALID_DAY, formatter);
        assertEquals(expectedDate, ParserUtil.parseDate(dayWithWhitespace));
        assertEquals(Optional.of(expectedDate), ParserUtil.parseDate(Optional.of(dayWithWhitespace)));
    }

```
###### \java\seedu\address\model\appointment\RemarkTest.java
``` java
public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void constructor_invalidRemark_throwsIllegalArgumentException() {
        String invalidRemark = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Remark(invalidRemark));
    }

    @Test
    public void isValidRemark() {
        // null remark
        Assert.assertThrows(NullPointerException.class, () -> Remark.isValidRemark(null));

        // invalid remarks
        assertFalse(Remark.isValidRemark("")); // empty string
        assertFalse(Remark.isValidRemark(" ")); // spaces only

        // valid remarks
        assertTrue(Remark.isValidRemark("Might need a house visit."));
        assertTrue(Remark.isValidRemark("-")); // one character
        assertTrue(Remark.isValidRemark("Might need a house visit, and medication.")); // long address
    }
}
```
###### \java\seedu\address\model\appointment\UniqueAppointmentListTest.java
``` java
public class UniqueAppointmentListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueAppointmentList uniquePersonList = new UniqueAppointmentList();
        thrown.expect(UnsupportedOperationException.class);
        uniquePersonList.asObservableList().remove(0);
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedAppointmentTest.java
``` java
public class XmlAdaptedAppointmentTest {

    private static final String INVALID_OWNER_NRIC = "S012345AB";
    private static final String INVALID_PET_PATIENT_NAME = "L@osai";
    private static final String INVALID_REMARK = " ";
    private static final String INVALID_DATETIME = "MAAAY 2018 8PM";
    private static final String INVALID_APPOINTMENT_TAG = "#checkup";

    private static final String VALID_OWNER_NRIC = ALICE_APP.getOwnerNric().toString();
    private static final String VALID_PET_PATIENT_NAME = ALICE_APP.getPetPatientName().toString();
    private static final String VALID_REMARK = ALICE_APP.getRemark().toString();
    private static final String VALID_DATETIME = ALICE_APP.getFormattedLocalDateTime();
    private static final List<XmlAdaptedTag> VALID_APPOINTMENT_TAGS = ALICE_APP.getAppointmentTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validAppointmentDetails_returnsAppointment() throws Exception {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(ALICE_APP);
        assertEquals(ALICE_APP, appointment.toModelType());
    }

    @Test
    public void toModelType_invalidOwnerNric_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        INVALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = Nric.MESSAGE_NRIC_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullOwnerNric_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        null,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidPetName_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        INVALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = PetPatientName.MESSAGE_PET_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullPetName_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        null,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, PetPatientName.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidRemark_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        INVALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = Remark.MESSAGE_REMARK_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullRemark_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        null,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidDateTime_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        INVALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = "Please follow the format of yyyy-MM-dd HH:mm";
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullDateTime_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(VALID_OWNER_NRIC,
                VALID_PET_PATIENT_NAME,
                VALID_REMARK,
                null,
                VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, LocalDateTime.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_APPOINTMENT_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_APPOINTMENT_TAG));
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        invalidTags);
        Assert.assertThrows(IllegalValueException.class, appointment::toModelType);
    }

}
```
###### \java\seedu\address\storage\XmlAdaptedPersonTest.java
``` java
    @Test
    public void toModelType_invalidNric_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, INVALID_NRIC, VALID_TAGS);
        String expectedMessage = Nric.MESSAGE_NRIC_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullNric_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE,
                VALID_EMAIL, VALID_ADDRESS, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }
```
###### \java\seedu\address\testutil\AppointmentBuilder.java
``` java
/**
 * A utility class to help with building Appointment Objects.
 */
public class AppointmentBuilder {
    public static final String DEFAULT_OWNER_NRIC = "S1012341B";
    public static final String DEFAULT_PET_PATIENT_NAME = "Joker";
    public static final String DEFAULT_REMARK = "Requires home visit";
    public static final String DEFAULT_DATE = "2018-12-31 12:30";
    public static final String DEFAULT_APPOINTMENT_TAG = "surgery";

    private Nric ownerNric;
    private PetPatientName petPatientName;
    private Remark remark;
    private LocalDateTime localDateTime;
    private Set<Tag> appointmentTags;

    public AppointmentBuilder() {
        ownerNric = new Nric(DEFAULT_OWNER_NRIC);
        petPatientName = new PetPatientName(DEFAULT_PET_PATIENT_NAME);
        remark = new Remark(DEFAULT_REMARK);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        localDateTime = LocalDateTime.parse(DEFAULT_DATE, formatter);
        appointmentTags = SampleDataUtil.getTagSet(DEFAULT_APPOINTMENT_TAG);
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code appointmentToCopy}.
     */
    public AppointmentBuilder(Appointment appointmentToCopy) {
        ownerNric = appointmentToCopy.getOwnerNric();
        petPatientName = appointmentToCopy.getPetPatientName();
        remark = appointmentToCopy.getRemark();
        localDateTime = appointmentToCopy.getDateTime();
        appointmentTags = new HashSet<>(appointmentToCopy.getAppointmentTags());
    }

    /**
     * Sets the {@code Nric} of the person of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withOwnerNric(String ownerNric) {
        this.ownerNric = new Nric(ownerNric);
        return this;
    }

    /**
     * Sets the {@code PetPatientName} of the pet of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withPetPatientName(String petPatientName) {
        this.petPatientName = new PetPatientName(petPatientName);
        return this;
    }

    /**
     * Parses the {@code appointmentTags} into a {@code Set<Tag>}
     * and set it to the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withAppointmentTags(String ... appointmentTags) {
        this.appointmentTags = SampleDataUtil.getTagSet(appointmentTags);
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withRemark(String remark) {
        this.remark = new Remark(remark);
        return this;
    }

    /**
     * Sets the {@code DateTime} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDateTime(String stringDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(stringDateTime, formatter);
        this.localDateTime = dateTime;
        return this;
    }

    public Appointment build() {
        return new Appointment(ownerNric, petPatientName, remark, localDateTime, appointmentTags);
    }
}
```
###### \java\seedu\address\testutil\AppointmentUtil.java
``` java
/**
 * A utility class for Appointment.
 */
public class AppointmentUtil {
    /**
     * Returns an add command string for adding the {@code petpatient}.
     */
    public static String getAddCommand(Appointment appt, Nric ownerNric, PetPatientName petPatientName) {
        return AddCommand.COMMAND_WORD + OPTION_APPOINTMENT + " " + getAppointmentDetails(appt)
                + OPTION_OWNER + " " + PREFIX_NRIC + ownerNric.toString() + OPTION_PET + " " + petPatientName.fullName;
    }
    /**
     * Returns the part of command string for the given {@code appointment}'s details.
     */
    public static String getAppointmentDetails(Appointment appointment) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DATE + appointment.getFormattedLocalDateTime() + " ");
        sb.append(PREFIX_REMARK + appointment.getRemark().value + " ");
        appointment.getAppointmentTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
```
###### \java\seedu\address\testutil\TypicalAppointments.java
``` java
/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalAppointments {

    public static final Appointment ALICE_APP = new AppointmentBuilder()
            .withOwnerNric(TypicalPersons.ALICE.getNric().toString())
            .withPetPatientName(TypicalPetPatients.JEWEL.getName().toString())
            .withRemark("Requires Home Visit")
            .withDateTime("2018-05-28 12:30")
            .withAppointmentTags("checkup").build();
    public static final Appointment BENSON_APP = new AppointmentBuilder()
            .withOwnerNric(TypicalPersons.BENSON.getNric().toString())
            .withPetPatientName(TypicalPetPatients.JOKER.getName().toString())
            .withRemark("May require isolation")
            .withDateTime("2018-04-22 14:30")
            .withAppointmentTags("vaccination").build();
    public static final Appointment BOB_APP = new AppointmentBuilder()
            .withOwnerNric(TypicalPersons.BOB.getNric().toString())
            .withPetPatientName(TypicalPetPatients.NERO.getName().toString())
            .withRemark("May require isolation")
            .withDateTime("2018-12-22 14:30")
            .withAppointmentTags("vaccination").build();

    private TypicalAppointments() {} // prevents instantiation

    public static List<Appointment> getTypicalAppointments() {
        return new ArrayList<>(Arrays.asList(ALICE_APP, BENSON_APP));
    }
}
```
###### \java\systemtests\DeleteCommandSystemTest.java
``` java
    @Test
    public void deleteFormatTest() {
        /* ----------------------- Performing invalid delete operation for owner ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        String command = DeleteCommand.COMMAND_WORD + " -o 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_OWNER);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -o -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_OWNER);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPersonList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " -o " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -o abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_OWNER);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -o 1 abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_OWNER);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE -o 1", MESSAGE_UNKNOWN_COMMAND);

        /* ------------------ Performing invalid delete operation for appointment ----------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -a 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_APPOINTMENT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -a -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_APPOINTMENT);

        /* Case: invalid index (size + 1) -> rejected */
        outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getAppointmentList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " -a " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -a abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_APPOINTMENT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -a 1 abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_APPOINTMENT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE -a 1", MESSAGE_UNKNOWN_COMMAND);

        /* ------------------ Performing invalid delete operation for pet patient ----------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -p 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_PET_PATIENT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -p -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_PET_PATIENT);

        /* Case: invalid index (size + 1) -> rejected */
        outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPetPatientList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " -p " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -p abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_PET_PATIENT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -p 1 abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_PET_PATIENT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE -p 1", MESSAGE_UNKNOWN_COMMAND);

        /* ----------------------- Performing invalid delete operation for force owner ------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -fo 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_OWNER);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -fo -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_OWNER);

        /* Case: invalid index (size + 1) -> rejected */
        outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPersonList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " -fo " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -fo abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_OWNER);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -fo 1 abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_OWNER);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE -fo 1", MESSAGE_UNKNOWN_COMMAND);

        /* ----------------------- Performing invalid delete operation for force pet patient ------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -fp 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_PET_PATIENT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -fp -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_PET_PATIENT);

        /* Case: invalid index (size + 1) -> rejected */
        outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPersonList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " -fp " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -fp abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_PET_PATIENT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -fp 1 abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT_FORCE_PET_PATIENT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE -fp 1", MESSAGE_UNKNOWN_COMMAND);

        /* ------------------------- Performing invalid delete operation with wrong type ---------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -sha 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -fza -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPersonList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " -fup " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -fsp abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " -nafp 1 abc",
                MESSAGE_INVALID_DELETE_COMMAND_FORMAT);
    }

```
###### \java\systemtests\FindCommandSystemTest.java
``` java
    @Test
    public void findNric() {
        /* Case: find multiple persons by nric in address book, command with leading spaces and trailing spaces
         * -> 2 persons found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " -o nr/" + NRIC_KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, BENSON, DANIEL); // first names of Benson & Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 2 persons found
         */
        command = FindCommand.COMMAND_WORD + " -o nr/" + NRIC_KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find nric where person list is not displaying the person we are finding -> 1 person found */
        command = FindCommand.COMMAND_WORD + " -o nr/F2345678U";
        ModelHelper.setFilteredPersonList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple nric in address book, 2 keywords -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " -o nr/S0123456Q T0123456L";
        ModelHelper.setFilteredPersonList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple nric in address book, 2 keywords in reversed order -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " -o nr/T0123456L S0123456Q";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple nric in address book, 2 keywords with 1 repeat -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " -o nr/T0123456L S0123456Q T0123456L";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple nric in address book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindCommand.COMMAND_WORD + " -o nr/S0123456Q T0123456L S9012389E";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find same persons in address book after deleting 1 of them -> 1 person found */
        executeCommand(DeleteCommand.COMMAND_WORD + " -fo 1");
        assertFalse(getModel().getAddressBook().getPersonList().contains(BENSON));
        command = FindCommand.COMMAND_WORD + " -o nr/" + NRIC_KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find nric not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -o nr/T0014852E";
        ModelHelper.setFilteredPersonList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
    }

    @Test
    public void findPersonTag() {
        /* Case: find persons with owemoney tag in address book, command with leading spaces and trailing spaces
         * -> 1 person found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " -o t/" + OWES_MONEY_TAG + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 1 persons found
         */

        command = FindCommand.COMMAND_WORD + " -o t/" + OWES_MONEY_TAG;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person where person list changes*/
        command = FindCommand.COMMAND_WORD + " -o t/friends";
        ModelHelper.setFilteredPersonList(expectedModel, ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 keywords -> 7 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/friends owesMoney";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 keywords in reversed order -> 7 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/owesMoney friends";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 keywords with 1 repeat -> 7 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/owesMoney friends owesMoney";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindCommand.COMMAND_WORD + " -o t/owesMoney friends NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in address book, keyword is same as name but of different case -> 1 person found */
        command = FindCommand.COMMAND_WORD + " -o t/OwEsMoNey fRiEnDs";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find person in address book, keyword is substring of tag -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/OWE";
        ModelHelper.setFilteredPersonList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in address book, tag is substring of keyword -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/owesmoneys";
        ModelHelper.setFilteredPersonList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/Chicken";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of person in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/" + DANIEL.getPhone().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of person in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -o t/" + DANIEL.getAddress().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in empty address book -> 0 persons found */
        deleteAllPersons();
        command = FindCommand.COMMAND_WORD + " -o t/friends";
        expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd -o t/friends";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void findPet() {
        String command = AddCommand.COMMAND_WORD + " " + OPTION_OWNER + TAG_DESC_FRIEND
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB
                + NAME_DESC_BOB + NRIC_DESC_BOB + TAG_DESC_HUSBAND + EMAIL_DESC_BOB;
        executeCommand(command);

        command = AddCommand.COMMAND_WORD + "  " + OPTION_PET + "  " + NAME_DESC_NERO
                + "  " +  SPECIES_DESC_NERO + "  " + BREED_DESC_NERO + "  " +  COLOUR_DESC_NERO + "  "
                + BLOODTYPE_DESC_NERO + "  " + TAG_DESC_FIV + " " + OPTION_OWNER + "  " + NRIC_DESC_BOB + "  ";
        executeCommand(command);

        /* Case: find pet name with  tag in address book, command with leading spaces and trailing spaces
         * -> 1 person found, 1 pet found
         */
        command = "   " + FindCommand.COMMAND_WORD + " -p " + NAME_DESC_NERO + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, BOB);
        ModelHelper.setFilteredPetPatientList(expectedModel, NERO);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 1 persons found, 1 pet found
         */
        command = FindCommand.COMMAND_WORD + " -p " + NAME_DESC_NERO;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find name not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -p n/NEerrreo";
        ModelHelper.setFilteredPersonList(expectedModel);
        ModelHelper.setFilteredPetPatientList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find pet name with  tag in address book, command with leading spaces and trailing spaces
         * -> 1 person found, 1 pet found
         */
        command = "   " + FindCommand.COMMAND_WORD + " -p " + SPECIES_DESC_NERO + "   ";
        ModelHelper.setFilteredPersonList(expectedModel, BOB);
        ModelHelper.setFilteredPetPatientList(expectedModel, NERO);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 1 persons found, 1 pet found
         */
        command = FindCommand.COMMAND_WORD + " -p " + SPECIES_DESC_NERO;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find species not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -p s/Doggy";
        ModelHelper.setFilteredPersonList(expectedModel);
        ModelHelper.setFilteredPetPatientList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find pet name with  tag in address book, command with leading spaces and trailing spaces
         * -> 1 person found, 1 pet found
         */
        command = "   " + FindCommand.COMMAND_WORD + " -p " + BREED_DESC_NERO + "   ";
        ModelHelper.setFilteredPersonList(expectedModel, BOB);
        ModelHelper.setFilteredPetPatientList(expectedModel, NERO);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 1 persons found, 1 pet found
         */
        command = FindCommand.COMMAND_WORD + " -p " + BREED_DESC_NERO;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find breed not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -p b/breedx";
        ModelHelper.setFilteredPersonList(expectedModel);
        ModelHelper.setFilteredPetPatientList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find pet name with  tag in address book, command with leading spaces and trailing spaces
         * -> 1 person found, 1 pet found
         */
        command = "   " + FindCommand.COMMAND_WORD + " -p " + COLOUR_DESC_NERO + "   ";
        ModelHelper.setFilteredPersonList(expectedModel, BOB);
        ModelHelper.setFilteredPetPatientList(expectedModel, NERO);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 1 persons found, 1 pet found
         */
        command = FindCommand.COMMAND_WORD + " -p " + COLOUR_DESC_NERO;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find colour not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -p c/Purple";
        ModelHelper.setFilteredPersonList(expectedModel);
        ModelHelper.setFilteredPetPatientList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find pet name with  tag in address book, command with leading spaces and trailing spaces
         * -> 1 person found, 1 pet found
         */
        command = "   " + FindCommand.COMMAND_WORD + " -p " + BLOODTYPE_DESC_NERO + "   ";
        ModelHelper.setFilteredPersonList(expectedModel, BOB);
        ModelHelper.setFilteredPetPatientList(expectedModel, NERO);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 1 persons found, 1 pet found
         */
        command = FindCommand.COMMAND_WORD + " -p " + BLOODTYPE_DESC_NERO;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find blood type not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -p bt/O";
        ModelHelper.setFilteredPersonList(expectedModel);
        ModelHelper.setFilteredPetPatientList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find pet name with  tag in address book, command with leading spaces and trailing spaces
         * -> 1 person found, 1 pet found
         */
        command = "   " + FindCommand.COMMAND_WORD + " -p " + TAG_DESC_FIV + "   ";
        ModelHelper.setFilteredPersonList(expectedModel, BOB);
        ModelHelper.setFilteredPetPatientList(expectedModel, NERO);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 1 persons found, 1 pet found
         */
        command = FindCommand.COMMAND_WORD + " -p " + TAG_DESC_FIV;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tag not in address book -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " -p t/owner";
        ModelHelper.setFilteredPersonList(expectedModel);
        ModelHelper.setFilteredPetPatientList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
    }



```
###### \java\systemtests\ListAppointmentCommandSystemTest.java
``` java
public class ListAppointmentCommandSystemTest extends AddressBookSystemTest {
    @Test
    public void listAppointment() {
        /* --------------------------------- Perform listappt command success --------------------------------------- */
        //year parameters given properly
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -y 2018 ",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "year"));

        //no year given, passes with today's year
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -y ",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "year"));

        //month parameters given properly
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -m 2018-12",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "month"));

        //month given but no year, passes with today's year
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -m 12",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "month"));

        //no month given, passes with today's month
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -m ",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "month"));

        //week parameters given properly
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -w 2018-12-31 ",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "week"));

        //no week given, passes with today's week
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -w ",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "week"));

        //date parameters given properly
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -d 2018-12-31 ",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "day"));

        //no date given, passes with today's date
        assertCommandSuccess(ListAppointmentCommand.COMMAND_WORD + " -d ",
                String.format(ListAppointmentCommand.MESSAGE_SUCCESS, "day"));

        /* --------------------------------- Perform listappt command failures -------------------------------------- */

        //null, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        String expectedMessage = "You can only list past appointments if you had an appointment"
                + " in the year of the specified field!";
        //listappt past year with no appointments, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -y 2016",
                expectedMessage);

        //listappt past month of year with no appointments, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -m 2016-01",
                expectedMessage);

        //listappt past week of year with no appointments, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -w 2016-01-01",
                expectedMessage);

        //listappt past date of year with no appointments, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -d 2016-01-01",
                expectedMessage);

        //writing wrong caps, fail
        assertCommandFailure("LiStApPt",
                String.format(Messages.MESSAGE_UNKNOWN_COMMAND));

        //unknown parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -opaenuf ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //unknown year parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -y naodnnn ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //extra year parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -y 2018 3noisefn ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //unknown year-month parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -m ajebfdliua ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //extra month parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -m 12 3noisefn ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //invalid month parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -m 60 ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //unknown week parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -w opaenuf ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //extra week parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -w 2018-12-31 3noisefn ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //unknown day parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -d opuf ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));

        //extra day parameter, fail
        assertCommandFailure(ListAppointmentCommand.COMMAND_WORD + " -d 2018-12-31 3noisefn ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

    /**
     * Performs verification for command to calendarView
     */
    private void assertCommandSuccess(String command, String message) {
        executeCommand(command);
        assertEquals(getResultDisplay().getText() , message);
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
```
