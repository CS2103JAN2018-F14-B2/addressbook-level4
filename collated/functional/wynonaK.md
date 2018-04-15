# wynonaK
###### \java\seedu\address\commons\events\ui\ChangeDayViewRequestEvent.java
``` java
/**
 * Indicates a request to change to the day view of the date requested.
 */
public class ChangeDayViewRequestEvent extends BaseEvent {

    public final LocalDate date;

    public ChangeDayViewRequestEvent(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\events\ui\ChangeMonthViewRequestEvent.java
``` java
/**
 * Indicates a request to change to the yearmonth view of the yearmonth requested.
 */
public class ChangeMonthViewRequestEvent extends BaseEvent {

    public final YearMonth yearMonth;

    public ChangeMonthViewRequestEvent(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

```
###### \java\seedu\address\commons\events\ui\ChangeWeekViewRequestEvent.java
``` java
/**
 * Indicates a request to change to the week view of the date requested.
 */
public class ChangeWeekViewRequestEvent extends BaseEvent {

    public final LocalDate date;

    public ChangeWeekViewRequestEvent(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\events\ui\ChangeYearViewRequestEvent.java
``` java
/**
 * Indicates a request to change to the year view of the year requested.
 */
public class ChangeYearViewRequestEvent extends BaseEvent {

    public final Year year;

    public ChangeYearViewRequestEvent(Year year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\logic\commands\DeleteCommand.java
``` java
/**
 * Deletes a person, pet patient or appointment from the address book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " -[f]o/-[f]p/-a"
            + ": Deletes the contact/pet/appointment identified by the index number used in the latest listing.\n"
            + "Additional -[f] options indicates forcefully deleting object all of its related dependencies.\n"
            + "Parameters: INDEX (must be a positive integer, must not be invalid)\n"
            + "Example: " + COMMAND_WORD + " -o 1";

    public static final String MESSAGE_USAGE_OWNER = COMMAND_WORD
            + " -o"
            + ": Deletes the contact identified by the index number used in the latest contact listing.\n"
            + "Parameters: INDEX (must be a positive integer, must not be invalid)\n"
            + "Example: " + COMMAND_WORD + " -o 1";

    public static final String MESSAGE_USAGE_PET_PATIENT = COMMAND_WORD
            + " -p"
            + ": Deletes the pet patient identified by the index number used in the latest pet patient listing.\n"
            + "Parameters: INDEX (must be a positive integer, must not be invalid)\n"
            + "Example: " + COMMAND_WORD + " -p 1";

    public static final String MESSAGE_USAGE_APPOINTMENT = COMMAND_WORD
            + " -a"
            + ": Deletes the appointment identified by the index number used in the latest appointment listing.\n"
            + "Parameters: INDEX (must be a positive integer, must not be invalid)\n"
            + "Example: " + COMMAND_WORD + " -a 1";

    public static final String MESSAGE_USAGE_FORCE_OWNER = COMMAND_WORD
            + " -fo"
            + ": Forcefully deletes a contact and all related dependencies "
            + "identified by the index number used in the latest contact listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " -fo 1";

    public static final String MESSAGE_USAGE_FORCE_PET_PATIENT = COMMAND_WORD
            + " -fp"
            + ": Forcefully deletes a pet patient and all related dependencies "
            + "identified by the index number used in the latest pet patient listing.\n"
            + "Parameters: INDEX (must be a positive integer, must not be invalid)\n"
            + "Example: " + COMMAND_WORD + " -fp 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Contact: %1$s";
    public static final String MESSAGE_DELETE_PET_PATIENT_SUCCESS = "Deleted Pet Patient: %1$s";
    public static final String MESSAGE_DELETE_APPOINTMENT_SUCCESS = "Deleted Appointment: %1$s";

    private final Index targetIndex;
    private final int type;

    private Person personToDelete;
    private PetPatient petPatientToDelete;
    private Appointment appointmentToDelete;

    public DeleteCommand(int type, Index targetIndex) {
        this.type = type;
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            switch (type) {
            case 1: return deletePerson();
            case 2: return deletePetPatient();
            case 3: return deleteAppointment();
            case 4: return deleteForcePerson();
            case 5: return deleteForcePetPatient();
            default:
                throw new CommandException(MESSAGE_USAGE);
            }
        } catch (PetDependencyNotEmptyException e) {
            throw new CommandException(Messages.MESSAGE_DEPENDENCIES_EXIST);
        } catch (AppointmentDependencyNotEmptyException e) {
            throw new CommandException(Messages.MESSAGE_DEPENDENCIES_EXIST);
        }
    }
    /**
     * Deletes {@code personToDelete} from the address book.
     */
    private CommandResult deletePerson() throws PetDependencyNotEmptyException {
        try {
            requireNonNull(personToDelete);
            model.deletePerson(personToDelete);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target contact cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    private void getPersonToDelete() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        personToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Deletes the pet patient {@code petPatientToDelete} from the address book.
     */
    private CommandResult deletePetPatient() throws AppointmentDependencyNotEmptyException {
        try {
            requireNonNull(petPatientToDelete);
            model.deletePetPatient(petPatientToDelete);
        } catch (PetPatientNotFoundException ppnfe) {
            throw new AssertionError("The target pet patient cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PET_PATIENT_SUCCESS, petPatientToDelete));
    }

    private void getPetPatientToDelete() throws CommandException {
        List<PetPatient> lastShownList = model.getFilteredPetPatientList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        petPatientToDelete = lastShownList.get(targetIndex.getZeroBased());
    }
    /**
     * Deletes the appointment {@code appointmentToDelete} from the address book.
     */
    private CommandResult deleteAppointment() {
        try {
            requireNonNull(appointmentToDelete);
            model.deleteAppointment(appointmentToDelete);
        } catch (AppointmentNotFoundException anfe) {
            throw new AssertionError("The target appointment cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS, appointmentToDelete));
    }

    private void getAppointmentToDelete() throws CommandException {
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }

        appointmentToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    /**
     * Forcefully deletes {@code personToDelete} from the address book.
     * All related dependencies (pet patients, appointments) will be deleted as well.
     */
    private CommandResult deleteForcePerson() {
        String deleteDependenciesList = "";

        try {
            requireNonNull(personToDelete);
            List<PetPatient> petPatientsDeleted = model.deletePetPatientDependencies(personToDelete);
            List<Appointment> appointmentsDeleted = new ArrayList<>();
            for (PetPatient pp : petPatientsDeleted) {
                appointmentsDeleted.addAll(model.deleteAppointmentDependencies(pp));
                deleteDependenciesList += "\n" + (String.format(MESSAGE_DELETE_PET_PATIENT_SUCCESS, pp));
            }
            for (Appointment appointment : appointmentsDeleted) {
                deleteDependenciesList += "\n" + (String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS, appointment));
            }
            model.deletePerson(personToDelete);
        } catch (PersonNotFoundException e) {
            throw new AssertionError("The target contact cannot be missing");
        } catch (PetDependencyNotEmptyException e) {
            throw new AssertionError("Pet dependencies still exist!");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete)
                + deleteDependenciesList);
    }

    /**
     * Forcefully deletes {@code petPatientToDelete} from the address book.
     * All related dependencies (appointments) will be deleted as well.
     */
    private CommandResult deleteForcePetPatient() {
        String deleteDependenciesList = "";

        try {
            requireNonNull(petPatientToDelete);
            List<Appointment> appointmentDependenciesDeleted = model.deleteAppointmentDependencies(petPatientToDelete);
            for (Appointment appointment : appointmentDependenciesDeleted) {
                deleteDependenciesList += "\n" + (String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS, appointment));
            }
            model.deletePetPatient(petPatientToDelete);
        } catch (PetPatientNotFoundException ppnfe) {
            throw new AssertionError("The target pet patient cannot be missing");
        }  catch (AppointmentDependencyNotEmptyException e) {
            throw new AssertionError("Appointment dependencies still exist!");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PET_PATIENT_SUCCESS, petPatientToDelete)
                + deleteDependenciesList);
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        try {
            switch (type) {
            case 1: getPersonToDelete();
                break;
            case 2: getPetPatientToDelete();
                break;
            case 3: getAppointmentToDelete();
                break;
            case 4: getPersonToDelete();
                break;
            case 5: getPetPatientToDelete();
                break;
            default:
                throw new CommandException(MESSAGE_USAGE);
            }
        } catch (CommandException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteCommand) other).targetIndex) // state check
                && Objects.equals(this.personToDelete, ((DeleteCommand) other).personToDelete));
    }
}
```
###### \java\seedu\address\logic\commands\FindCommand.java
``` java
/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "f";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all fields that matches any of"
            + " the specified option, prefixes & keywords (case-sensitive)"
            + " and displays them as a list with index numbers.\n"
            + "Parameters: OPTION PREFIX/KEYWORD [MORE_PREFIX/MORE_KEYWORDS]...\n"
            + "Accepted Options: -o (CONTACT-RELATED), -p (PET-PATIENT-RELATED)\n"
            + "Accepted Prefixes for Contacts: n/NAME, nr/NRIC, t/TAG\n"
            + "Accepted Prefixes for Pet Patient: n/NAME, s/SPECIES, b/BREED, c/COLOUR, bt/BLOODTYPE, t/TAG\n"
            + "Example: " + COMMAND_WORD + " -o n/alice bob charlie";

    private HashMap<String, String[]> hashMap;
    private int type = 0;

    public FindCommand(HashMap<String, String[]> hashMap) {
        this.hashMap = hashMap;
        if (hashMap.containsKey("ownerName")
                || hashMap.containsKey("ownerNric")
                || hashMap.containsKey("ownerTag")) {
            type = 1;
        } else if (hashMap.containsKey("petName")
                || hashMap.containsKey("petSpecies")
                || hashMap.containsKey("petBreed")
                || hashMap.containsKey("petColour")
                || hashMap.containsKey("petBloodType")
                || hashMap.containsKey("petTag")) {
            type = 2;
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        switch (type) {
        case 1:
            return findOwner();
        case 2:
            return findPetPatient();
        default:
            throw new CommandException(MESSAGE_USAGE);
        }
    }

    /**
     * Finds owners with given {@code predicate} in this {@code addressbook}.
     */
    private CommandResult findOwner() {
        Predicate<Person> finalPredicate = null;

        if (hashMap.containsKey("ownerName")) {
            String[] nameKeywords = hashMap.get("ownerName");
            Predicate<Person> namePredicate =  person -> Arrays.stream(nameKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword));
            finalPredicate = namePredicate;
        }

        if (hashMap.containsKey("ownerNric")) {
            String[] nricKeywords = hashMap.get("ownerNric");
            Predicate<Person>  nricPredicate = person -> Arrays.stream(nricKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getNric().toString(), keyword));
            if (finalPredicate == null) {
                finalPredicate = nricPredicate;
            } else {
                finalPredicate = finalPredicate.and(nricPredicate);
            }
        }

        if (hashMap.containsKey("ownerTag")) {
            String[] tagKeywords = hashMap.get("ownerTag");
            Predicate<Person> tagPredicate = person -> Arrays.stream(tagKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getTagString(), keyword));
            if (finalPredicate == null) {
                finalPredicate = tagPredicate;
            } else {
                finalPredicate = finalPredicate.and(tagPredicate);
            }
        }

        model.updateFilteredPersonList(finalPredicate);
        updatePetListForOwner();
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size())
                + "\n"
                + getMessageForPetPatientListShownSummary(model.getFilteredPetPatientList().size()));
    }

    /**
     * Finds owners with given {@code predicate} in this {@code addressbook}.
     */
    private CommandResult findPetPatient() {
        Predicate<PetPatient> finalPredicate = null;

        if (hashMap.containsKey("petName")) {
            String[] nameKeywords = hashMap.get("petName");
            Predicate<PetPatient> namePredicate =  petPatient -> Arrays.stream(nameKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(petPatient.getName().fullName, keyword));
            finalPredicate = namePredicate;
        }

        if (hashMap.containsKey("petSpecies")) {
            String[] speciesKeywords = hashMap.get("petSpecies");
            Predicate<PetPatient> speciesPredicate =  petPatient -> Arrays.stream(speciesKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(petPatient.getSpecies().species, keyword));
            if (finalPredicate == null) {
                finalPredicate = speciesPredicate;
            } else {
                finalPredicate = finalPredicate.and(speciesPredicate);
            }
        }

        if (hashMap.containsKey("petBreed")) {
            String[] breedKeywords = hashMap.get("petBreed");
            Predicate<PetPatient> breedPredicate =  petPatient -> Arrays.stream(breedKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(petPatient.getBreed().breed, keyword));
            if (finalPredicate == null) {
                finalPredicate = breedPredicate;
            } else {
                finalPredicate = finalPredicate.and(breedPredicate);
            }
        }

        if (hashMap.containsKey("petColour")) {
            String[] colourKeywords = hashMap.get("petColour");
            Predicate<PetPatient> colourPredicate =  petPatient -> Arrays.stream(colourKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(petPatient.getColour().colour, keyword));
            if (finalPredicate == null) {
                finalPredicate = colourPredicate;
            } else {
                finalPredicate = finalPredicate.and(colourPredicate);
            }
        }

        if (hashMap.containsKey("petBloodType")) {
            String[] bloodTypeKeywords = hashMap.get("petBloodType");
            Predicate<PetPatient> bloodTypePredicate =  petPatient -> Arrays.stream(bloodTypeKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(
                            petPatient.getBloodType().bloodType, keyword));
            if (finalPredicate == null) {
                finalPredicate = bloodTypePredicate;
            } else {
                finalPredicate = finalPredicate.and(bloodTypePredicate);
            }
        }

        if (hashMap.containsKey("petTag")) {
            String[] tagKeywords = hashMap.get("petTag");
            Predicate<PetPatient> tagPredicate = petPatient -> Arrays.stream(tagKeywords)
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(petPatient.getTagString(), keyword));
            if (finalPredicate == null) {
                finalPredicate = tagPredicate;
            } else {
                finalPredicate = finalPredicate.and(tagPredicate);
            }
        }

        model.updateFilteredPetPatientList(finalPredicate);
        updateOwnerListForPets();
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size())
                + "\n"
                + getMessageForPetPatientListShownSummary(model.getFilteredPetPatientList().size()));
    }

    /**
     * Updates the filtered pet list with the changed owners in this {@code addressbook}.
     */
    private void updatePetListForOwner() {
        List<String> nricKeywordsForPets = new ArrayList<>();
        for (Person person : model.getFilteredPersonList()) {
            nricKeywordsForPets.add(person.getNric().toString());
        }
        Predicate<PetPatient> petPatientNricPredicate =  petPatient -> nricKeywordsForPets.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(petPatient.getOwner().toString(), keyword));
        model.updateFilteredPetPatientList(petPatientNricPredicate);
    }

    /**
     * Updates the filtered person list with the changed pets in this {@code addressbook}.
     */
    private void updateOwnerListForPets() {
        List<String> nricKeywordsForOwner = new ArrayList<>();
        for (PetPatient petPatient : model.getFilteredPetPatientList()) {
            if (!nricKeywordsForOwner.contains(petPatient.getOwner().toString())) {
                nricKeywordsForOwner.add(petPatient.getOwner().toString());
            }
        }
        Predicate<Person> ownerNricPredicate =  person -> nricKeywordsForOwner.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getNric().toString(), keyword));
        model.updateFilteredPersonList(ownerNricPredicate);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.hashMap.equals(((FindCommand) other).hashMap)); // state check
    }
}
```
###### \java\seedu\address\logic\commands\ListAppointmentCommand.java
``` java
/**
 * Lists appointments based on the specified year, month, week or day.
 */
public class ListAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "listappt";
    public static final String COMMAND_ALIAS = "la";

    public static final String MESSAGE_SUCCESS = "Successfully listed appointments in the %1$s view requested.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": To handle all appointment related listings.\n"
            + "Parameters: OPTION FIELD\n"
            + "Accepted Options: -y (YEAR VIEW), -m (MONTH VIEW), -w (WEEK VIEW), -d (DAY VIEW)\n"
            + "YEAR VIEW accepts a year field in the format of yyyy.\n"
            + "MONTH VIEW accepts a year and month field in the format of yyyy-MM"
            + " or just a month field in the format of MM, of which the year will be defaulted to this current year.\n"
            + "WEEK VIEW accepts a date field in the format of yyyy-MM-dd.\n"
            + "DAY VIEW accepts a date field in the format of yyyy-MM-dd.\n"
            + "If nothing is given as a FIELD, it will return the specified view of the current date.\n"
            + "You can only list past appointments if you had an appointment in the year of the specified field.";

    private int type = 0; //year = 1, month = 2, week = 3, day = 4.
    private Year year = null;
    private YearMonth yearMonth = null;
    private LocalDate date = null;

    public ListAppointmentCommand(int type, Year year) {
        this.type = type;
        this.year = year;
    }

    public ListAppointmentCommand(int type, YearMonth yearMonth) {
        this.type = type;
        this.yearMonth = yearMonth;
    }

    public ListAppointmentCommand(int type, LocalDate date) {
        this.type = type;
        this.date = date;
    }


    private CommandResult getYearView() throws NoAppointmentInYearException {
        if (year.isBefore(Year.now())) {
            if (!checkPastAppointment(year.getValue())) {
                throw new NoAppointmentInYearException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
            }
        }

        EventsCenter.getInstance().post(new ChangeYearViewRequestEvent(year));
        return new CommandResult(String.format(MESSAGE_SUCCESS, "year"));
    }

    private CommandResult getMonthView() throws NoAppointmentInYearException {
        if (yearMonth.isBefore(YearMonth.now())) {
            if (!checkPastAppointment(yearMonth.getYear())) {
                throw new NoAppointmentInYearException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
            }
        }

        EventsCenter.getInstance().post(new ChangeMonthViewRequestEvent(yearMonth));
        return new CommandResult(String.format(MESSAGE_SUCCESS, "month"));
    }

    private CommandResult getWeekView() throws NoAppointmentInYearException {
        if (date.isBefore(LocalDate.now())) {
            if (!checkPastAppointment(date.getYear())) {
                throw new NoAppointmentInYearException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
            }
        }

        EventsCenter.getInstance().post(new ChangeWeekViewRequestEvent(date));
        return new CommandResult(String.format(MESSAGE_SUCCESS, "week"));
    }

    private CommandResult getDayView() throws NoAppointmentInYearException {
        if (date.isBefore(LocalDate.now())) {
            if (!checkPastAppointment(date.getYear())) {
                throw new NoAppointmentInYearException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
            }
        }

        EventsCenter.getInstance().post(new ChangeDayViewRequestEvent(date));
        return new CommandResult(String.format(MESSAGE_SUCCESS, "day"));
    }

    /**
     * Check if there exists a past appointment with in the {@code model} with the {@code year} specified.
     */
    private boolean checkPastAppointment(int year) {
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        List<Appointment> appointmentList = model.getFilteredAppointmentList();
        for (Appointment appointment : appointmentList) {
            if  (appointment.getDateTime().getYear() == year)  {
                return true;
            }
        }
        return false;
    }

    @Override
    public CommandResult execute() throws CommandException {
        try {
            switch (type) {
            case 1:
                return getYearView();
            case 2:
                return getMonthView();
            case 3:
                return getWeekView();
            case 4:
                return getDayView();
            default:
                throw new CommandException(MESSAGE_USAGE);
            }
        } catch (NoAppointmentInYearException e) {
            throw new CommandException("You can only list past appointments if you had an appointment"
                    + " in the year of the specified field!");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListAppointmentCommand)) {
            return false;
        }

        ListAppointmentCommand otherListAppointmentCommand = (ListAppointmentCommand) other;

        boolean yearSame = isTheSame(year, otherListAppointmentCommand.year);
        boolean yearMonthSame = isTheSame(yearMonth, otherListAppointmentCommand.yearMonth);
        boolean dateSame = isTheSame(date, otherListAppointmentCommand.date);

        if (yearSame || yearMonthSame || dateSame) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if both objects are the same.
     * Returns true if both objects are equivalent.
     * Returns true if both objects are null.
     */
    public boolean isTheSame(Object one, Object two) {
        if (one != null && two != null) {
            if (one.equals(two)) {
                return true;
            }
        }

        if (one == null && two == null) {
            return true;
        }

        return false;
    }
}
```
###### \java\seedu\address\logic\parser\DeleteCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private static final Pattern DELETE_COMMAND_FORMAT_OWNER = Pattern.compile("-(o)+(?<index>.*)");
    private static final Pattern DELETE_COMMAND_FORMAT_PET_PATIENT = Pattern.compile("-(p)+(?<index>.*)");
    private static final Pattern DELETE_COMMAND_FORMAT_APPOINTMENT = Pattern.compile("-(a)+(?<index>.*)");
    private static final Pattern DELETE_COMMAND_FORMAT_FORCE_OWNER = Pattern.compile("-(fo)+(?<index>.*)");
    private static final Pattern DELETE_COMMAND_FORMAT_FORCE_PET_PATIENT = Pattern.compile("-(fp)+(?<index>.*)");
    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * type changes depending on what pattern it matches
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        final Matcher matcherForOwner = DELETE_COMMAND_FORMAT_OWNER.matcher(trimmedArgs);
        if (matcherForOwner.matches()) {
            try {
                int type = 1;
                Index index = ParserUtil.parseIndex(matcherForOwner.group("index"));
                return new DeleteCommand(type, index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE_OWNER));
            }
        }

        final Matcher matcherForPetPatient = DELETE_COMMAND_FORMAT_PET_PATIENT.matcher(trimmedArgs);
        if (matcherForPetPatient.matches()) {
            try {
                int type = 2;
                Index index = ParserUtil.parseIndex(matcherForPetPatient.group("index"));
                return new DeleteCommand(type, index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE_PET_PATIENT));
            }
        }

        final Matcher matcherForAppointment = DELETE_COMMAND_FORMAT_APPOINTMENT.matcher(trimmedArgs);
        if (matcherForAppointment.matches()) {
            try {
                int type = 3;
                Index index = ParserUtil.parseIndex(matcherForAppointment.group("index"));
                return new DeleteCommand(type, index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE_APPOINTMENT));
            }
        }

        final Matcher matcherForForceOwner = DELETE_COMMAND_FORMAT_FORCE_OWNER.matcher(trimmedArgs);
        if (matcherForForceOwner.matches()) {
            try {
                int type = 4;
                Index index = ParserUtil.parseIndex(matcherForForceOwner.group("index"));
                return new DeleteCommand(type, index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE_FORCE_OWNER));
            }
        }

        final Matcher matcherForForcePetPatient = DELETE_COMMAND_FORMAT_FORCE_PET_PATIENT.matcher(trimmedArgs);
        if (matcherForForcePetPatient.matches()) {
            try {
                int type = 5;
                Index index = ParserUtil.parseIndex(matcherForForcePetPatient.group("index"));
                return new DeleteCommand(type, index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE_FORCE_PET_PATIENT));
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\logic\parser\ListAppointmentCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ListAppointmentCommand object
 */
public class ListAppointmentCommandParser implements Parser<ListAppointmentCommand> {

    private static final Pattern LIST_APPOINTMENT_COMMAND_FORMAT_YEAR = Pattern.compile("-(y)+(?<info>.*)");
    private static final Pattern LIST_APPOINTMENT_COMMAND_FORMAT_MONTH = Pattern.compile("-(m)+(?<info>.*)");
    private static final Pattern LIST_APPOINTMENT_COMMAND_FORMAT_WEEK = Pattern.compile("-(w)+(?<info>.*)");
    private static final Pattern LIST_APPOINTMENT_COMMAND_FORMAT_DAY = Pattern.compile("-(d)+(?<info>.*)");

    /**
     * Parses the given {@code String} of arguments in the context of the ListAppointmentCommand
     * and returns an ListAppointmentCommand object for execution.
     * type changes depending on what pattern it matches
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ListAppointmentCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        final Matcher matcherForYear = LIST_APPOINTMENT_COMMAND_FORMAT_YEAR.matcher(trimmedArgs);
        if (matcherForYear.matches()) {
            int type = 1;

            try {
                Year year = ParserUtil.parseYear(matcherForYear.group("info"));
                return new ListAppointmentCommand(type, year);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
            }
        }

        final Matcher matcherForMonth = LIST_APPOINTMENT_COMMAND_FORMAT_MONTH.matcher(trimmedArgs);
        if (matcherForMonth.matches()) {
            try {
                int type = 2;
                YearMonth yearMonth = ParserUtil.parseMonth(matcherForMonth.group("info"));
                return new ListAppointmentCommand(type, yearMonth);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
            }
        }

        final Matcher matcherForWeek = LIST_APPOINTMENT_COMMAND_FORMAT_WEEK.matcher(trimmedArgs);
        if (matcherForWeek.matches()) {
            try {
                int type = 3;
                LocalDate date = ParserUtil.parseDate(matcherForWeek.group("info"));
                return new ListAppointmentCommand(type, date);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
            }
        }

        final Matcher matcherForDay = LIST_APPOINTMENT_COMMAND_FORMAT_DAY.matcher(trimmedArgs);
        if (matcherForDay.matches()) {
            try {
                int type = 4;
                LocalDate date = ParserUtil.parseDate(matcherForDay.group("info"));
                return new ListAppointmentCommand(type, date);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String remark} into an {@code Remark}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code remark} is invalid.
     */
    public static Remark parseRemark(String remark) throws IllegalValueException {
        requireNonNull(remark);
        String trimmedRemark = remark.trim();
        if (!Remark.isValidRemark(trimmedRemark)) {
            throw new IllegalValueException(Remark.MESSAGE_REMARK_CONSTRAINTS);
        }
        return new Remark(trimmedRemark);
    }

    /**
     * Parses a {@code Optional<String> remark} into an {@code Optional<Remark>} if {@code remark} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Remark> parseRemark(Optional<String> remark) throws IllegalValueException {
        requireNonNull(remark);
        return remark.isPresent() ? Optional.of(parseRemark(remark.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String dateTime} into an {@code LocalDateTime} object.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code dateTime} is invalid.
     */
    public static LocalDateTime parseDateTime(String dateTime) throws IllegalValueException {
        requireNonNull(dateTime);

        dateTime = dateTime.trim();

        try {
            String[] dateTimeArray = dateTime.split("\\s+");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(dateTimeArray[0]);
        } catch (ParseException e) {
            throw new IllegalValueException("Please give a valid date based on the format yyyy-MM-dd!");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("Please ensure all fields are valid "
                    + "and follow the format of yyyy-MM-dd HH:mm!");
        }

        return localDateTime;
    }

    /**
     * Parses {@code Optional<String> dateTime} into an {@code Optional<LocalDateTime>} if {@code dateTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<LocalDateTime> parseDateTime(Optional<String> dateTime) throws IllegalValueException {
        requireNonNull(dateTime);
        return dateTime.isPresent() ? Optional.of(parseDateTime(dateTime.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String date} into an {@code LocalDate} object.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code dateTime} is invalid.
     */
    public static LocalDate parseDate(String date) throws IllegalValueException {
        LocalDate localDate = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        date = date.trim();

        if (date.isEmpty()) {
            localDate = LocalDate.now();
            return localDate;
        }

        try {
            df.setLenient(false);
            df.parse(date);
            localDate = LocalDate.parse(date, formatter);
        } catch (ParseException | DateTimeParseException e) {
            throw new IllegalValueException("Please give a valid date based on the format yyyy-MM-dd!");
        }

        return localDate;
    }

    /**
     * Parses {@code Optional<String> date} into an {@code Optional<LocalDate>} if {@code date} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<LocalDate> parseDate(Optional<String> date) throws IllegalValueException {
        requireNonNull(date);
        return date.isPresent() ? Optional.of(parseDate(date.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String stringYear} into an {@code Year} object.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code stringYear} is invalid.
     */
    public static Year parseYear(String stringYear) throws IllegalValueException {
        Year year = null;
        DateFormat df = new SimpleDateFormat("yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        stringYear = stringYear.trim();

        if (stringYear.isEmpty()) {
            year = Year.now();
            return year;
        }

        try {
            df.setLenient(false);
            df.parse(stringYear);
            year = Year.parse(stringYear, formatter);
        } catch (ParseException | DateTimeParseException e) {
            throw new IllegalValueException("Please give a valid year based on the format yyyy!");
        }

        return year;
    }

    /**
     * Parses {@code Optional<String> month} into an {@code Optional<Year>} if {@code year} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Year> parseYear(Optional<String> year) throws IllegalValueException {
        requireNonNull(year);
        return year.isPresent() ? Optional.of(parseYear(year.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String stringMonth} into an {@code YearMonth} object.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code stringMonth} is invalid.
     */
    public static YearMonth parseMonth(String stringMonth) throws IllegalValueException {
        YearMonth yearMonth = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        stringMonth = stringMonth.trim();

        if (stringMonth.isEmpty()) {
            yearMonth = YearMonth.now();
            return yearMonth;
        }

        try {
            if (stringMonth.length() == 2) {
                int month = Integer.parseInt(stringMonth);
                yearMonth = YearMonth.now().withMonth(month);
                return yearMonth;
            }

            df.setLenient(false);
            df.parse(stringMonth);
            yearMonth = YearMonth.parse(stringMonth, formatter);
        } catch (ParseException e) {
            throw new IllegalValueException("Please give a valid year and month based on the format yyyy-MM!");
        } catch (NumberFormatException nfe) {
            throw new IllegalValueException("Please input integer for month in the format MM!");
        } catch (DateTimeException dte) {
            throw new IllegalValueException("Please give a valid month based on the format MM!");
        }

        return yearMonth;
    }

    /**
     * Parses {@code Optional<String> month} into an {@code Optional<YearMonth>} if {@code month} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<YearMonth> parseMonth(Optional<String> month) throws IllegalValueException {
        requireNonNull(month);
        return month.isPresent() ? Optional.of(parseMonth(month.get())) : Optional.empty();
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Updates the master tag list to include tags in {@code appointment} that are not in the list.
     *
     * @return a copy of this {@code appointment} such that every tag in this appointment
     * points to a Tag object in the master list.
     */
    private Appointment syncWithMasterTagList(Appointment appointment) {
        final UniqueTagList appointmentTags = new UniqueTagList(appointment.getAppointmentTags());
        tags.mergeFrom(appointmentTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        appointmentTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Appointment(
                appointment.getOwnerNric(),
                appointment.getPetPatientName(),
                appointment.getRemark(),
                appointment.getDateTime(),
                correctTagReferences);
    }

    /**
     * Adds an appointment.
     * Also checks the new appointment's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the appointment to point to those in {@link #tags}.
     *
     * @throws DuplicateAppointmentException if an equivalent person already exists.
     */
    public void addAppointment(Appointment a) throws DuplicateAppointmentException, DuplicateDateTimeException,
        ConcurrentAppointmentException, PastAppointmentException {
        Appointment appointment = syncWithMasterTagList(a);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any appointment
        // in the appointment list.
        appointments.add(appointment);
    }

    ////Delete operations

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     * @throws PetDependencyNotEmptyException if the {@code key} still contains pet patients it is tied to.
     */
    public boolean removePerson(Person key) throws PersonNotFoundException, PetDependencyNotEmptyException {
        petPatientDependenciesExist(key);

        if (persons.remove(key)) {
            removeUselessTags();
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Removes pet patient {@code key} from this {@code AddressBook}.
     *
     * @throws PetPatientNotFoundException if the {@code key} is not in this {@code AddressBook}.
     * @throws AppointmentDependencyNotEmptyException if the {@code key} still contains appointments it is tied to.
     */
    public boolean removePetPatient(PetPatient key)
            throws PetPatientNotFoundException, AppointmentDependencyNotEmptyException {
        appointmentDependenciesExist(key);

        if (petPatients.remove(key)) {
            removeUselessTags();
            return true;
        } else {
            throw new PetPatientNotFoundException();
        }
    }

    /**
     * Forcefully removes all pet patients dependencies on {@code key} from this {@code AddressBook}.
     */

    public List<PetPatient> removeAllPetPatientDependencies(Person key) {
        Iterator<PetPatient> petPatientIterator = petPatients.iterator();
        List<PetPatient> petPatientsDeleted = new ArrayList<>();
        while (petPatientIterator.hasNext()) {
            PetPatient petPatient = petPatientIterator.next();

            if (petPatient.getOwner().equals(key.getNric())) {
                petPatientsDeleted.add(petPatient);
                petPatientIterator.remove();
            }
        }
        return petPatientsDeleted;
    }

    /**
     * @throws AppointmentDependencyNotEmptyException if appointment dependencies of {@code key}
     * still exists in {@code AddressBook}.
     */
    private void appointmentDependenciesExist(PetPatient key) throws AppointmentDependencyNotEmptyException {
        for (Appointment appointment : appointments) {
            if (appointment.getPetPatientName().equals(key.getName())
                    && appointment.getOwnerNric().equals(key.getOwner())) {
                throw new AppointmentDependencyNotEmptyException("Appointment dependency still exist!");
            }
        }
    }

    /**
     * @throws PetDependencyNotEmptyException if pet dependencies of {@code key} still exists in {@code AddressBook}.
     */
    private void petPatientDependenciesExist(Person key) throws PetDependencyNotEmptyException {
        for (PetPatient petPatient : petPatients) {
            if (petPatient.getOwner().equals(key.getNric())) {
                throw new PetDependencyNotEmptyException("Pet Patient dependency still exist!");
            }
        }
    }

    /**
     * Forcefully removes all dependencies relying on pet patient {@code key} from this {@code AddressBook}.
     *
     */
    public List<Appointment> removeAllAppointmentDependencies(PetPatient key) {
        List<Appointment> appointmentsDeleted = new ArrayList<>();
        Iterator<Appointment> appointmentIterator = appointments.iterator();

        while (appointmentIterator.hasNext()) {
            Appointment appointment = appointmentIterator.next();

            if (appointment.getPetPatientName().equals(key.getName())
                    && appointment.getOwnerNric().equals(key.getOwner())) {
                appointmentsDeleted.add(appointment);
                appointmentIterator.remove();
            }
        }

        return appointmentsDeleted;
    }

    /**
     * Removes appointment {@code key} from this {@code AddressBook}.
     *
     * @throws AppointmentNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removeAppointment(Appointment key) throws AppointmentNotFoundException {
        if (appointments.remove(key)) {
            removeUselessTags();
            return true;
        } else {
            throw new AppointmentNotFoundException();
        }
    }

    //// tag operations

```
###### \java\seedu\address\model\AddressBook.java
``` java
    @Override
    public ObservableList<Appointment> getAppointmentList() {
        return appointments.asObservableList();
    }

```
###### \java\seedu\address\model\appointment\Appointment.java
``` java
/**
 * Represents an Appointment.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Appointment {
    private Nric ownerNric;
    private PetPatientName petPatientName;
    private Remark remark; //remarks
    private LocalDateTime localDateTime; //date of appointment

    private UniqueTagList appointmentTags; //type of appointment

    /**
     * Every field must be present and not null.
     */
    public Appointment(Nric ownerNric, PetPatientName petPatientName, Remark remark,
                       LocalDateTime localDateTime, Set<Tag> appointmentTags) {
        requireAllNonNull(ownerNric, petPatientName, remark, localDateTime, appointmentTags);
        this.ownerNric = ownerNric;
        this.petPatientName = petPatientName;
        this.remark = remark;
        this.localDateTime = localDateTime;
        // protect internal tags from changes in the arg list
        this.appointmentTags = new UniqueTagList(appointmentTags);
    }

    /**
     * ownerNric and petName can be set later using setter methods.
     */
    public Appointment(Remark remark, LocalDateTime localDateTime, Set<Tag> type) {
        requireAllNonNull(remark, localDateTime, type);
        this.remark = remark;
        this.localDateTime = localDateTime;
        // protect internal tags from changes in the arg list
        this.appointmentTags = new UniqueTagList(type);
    }

    public Nric getOwnerNric() {
        return ownerNric;
    }

    public void setOwnerNric(Nric ownerNric) {
        this.ownerNric = ownerNric;
    }

    public PetPatientName getPetPatientName() {
        return petPatientName;
    }

    public void setPetPatientName(PetPatientName petPatientName) {
        this.petPatientName = petPatientName;
    }

    public Remark getRemark() {
        return remark;
    }

    public LocalDateTime getDateTime() {
        return localDateTime;
    }

    public String getFormattedLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getAppointmentTags() {
        return Collections.unmodifiableSet(appointmentTags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return otherAppointment.getOwnerNric().equals(this.getOwnerNric())
                && otherAppointment.getPetPatientName().equals((this.getPetPatientName()))
                && otherAppointment.getRemark().equals(this.getRemark())
                && otherAppointment.getDateTime().equals(this.getDateTime());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(ownerNric, petPatientName, remark, localDateTime, appointmentTags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("    ")
                .append(getFormattedLocalDateTime())
                .append("    Remarks: ")
                .append(getRemark())
                .append("    Type(s): ");
        getAppointmentTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTag() {
        return Collections.unmodifiableSet(appointmentTags.toSet());
    }

```
###### \java\seedu\address\model\appointment\exceptions\AppointmentDependencyNotEmptyException.java
``` java
/**
 * Signals that the operation is unable to continue because there are still appointments dependent.
 */

public class AppointmentDependencyNotEmptyException extends Exception {
    public AppointmentDependencyNotEmptyException(String message) {
        super(message);
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\AppointmentNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified appointment.
 */
public class AppointmentNotFoundException extends Exception {
}
```
###### \java\seedu\address\model\appointment\exceptions\DuplicateAppointmentException.java
``` java
/**
 * Signals that the operation will result in duplicate Appointment objects.
 */
public class DuplicateAppointmentException extends DuplicateDataException {
    public DuplicateAppointmentException() {
        super("Operation would result in duplicate appointments");
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\DuplicateDateTimeException.java
``` java
/**
 * Signals that the operation will result in double booking.
 */
public class DuplicateDateTimeException extends DuplicateDataException {
    public DuplicateDateTimeException() {
        super("Operation would result in multiple bookings in the same time slot");
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\NoAppointmentInYearException.java
``` java
/**
 * Signals that the operation cannot be done as there is no appointments in said year.
 */
public class NoAppointmentInYearException extends Exception {
    public NoAppointmentInYearException(String message) {
        super(message);
    }
}
```
###### \java\seedu\address\model\appointment\Remark.java
``` java
/**
 * Represents a Appointment's remarks.
 * Guarantees: is valid as declared in {@link #isValidRemark(String)}
 */
public class Remark {

    public static final String MESSAGE_REMARK_CONSTRAINTS =
            "Remarks can take any values, and it should not be blank. Leave \"nil\" for no remarks.";

    /*
     * The first character of the remark must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String REMARK_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code Remark}.
     *
     * @param remark A valid address.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        checkArgument(isValidRemark(remark), MESSAGE_REMARK_CONSTRAINTS);
        this.value = remark;
    }

    /**
     * Returns true if a given string is a valid remark.
     */
    public static boolean isValidRemark(String test) {
        return test.matches(REMARK_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\appointment\UniqueAppointmentList.java
``` java
/**
 * A list of appointments that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Appointment#equals(Object)
 */
public class UniqueAppointmentList implements Iterable<Appointment> {

    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent appointment as the given argument.
     */
    public boolean contains(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an appointment to the list.
     *
     * @throws DuplicateAppointmentException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Appointment toAdd) throws DuplicateAppointmentException, DuplicateDateTimeException,
        PastAppointmentException, ConcurrentAppointmentException {
        requireNonNull(toAdd);

        if (contains(toAdd)) {
            throw new DuplicateAppointmentException();
        }

        ArrayList<LocalDateTime> timeList = new ArrayList<>();

        for (Appointment a : internalList) {
            timeList.add(a.getDateTime());
            if (a.getDateTime().equals(toAdd.getDateTime())) {
                throw new DuplicateDateTimeException();
            }
        }

        for (LocalDateTime dateTime : timeList) {
            if (toAdd.getDateTime().isAfter(dateTime) && toAdd.getDateTime().isBefore(dateTime.plusMinutes(30))) {
                throw new ConcurrentAppointmentException();
            }
            if (toAdd.getDateTime().isBefore(dateTime) && toAdd.getDateTime().plusMinutes(30).isAfter(dateTime)) {
                throw new ConcurrentAppointmentException();
            }
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the appointment {@code target} in the list with {@code editedAppointment}.
     *
     * @throws DuplicateAppointmentException if the replacement is equivalent to
     * another existing appointment in the list.
     * @throws AppointmentNotFoundException if {@code target} could not be found in the list.
     */
    public void setAppointment(Appointment target, Appointment editedAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireNonNull(editedAppointment);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new AppointmentNotFoundException();
        }

        if (!target.equals(editedAppointment) && internalList.contains(editedAppointment)) {
            throw new DuplicateAppointmentException();
        }

        internalList.set(index, editedAppointment);
    }

    /**
     * Removes the equivalent pet patient from the list.
     *
     * @throws AppointmentNotFoundException if no such pet patient could be found in the list.
     */
    public boolean remove(Appointment toRemove) throws AppointmentNotFoundException {
        requireNonNull(toRemove);
        final boolean appointmentFoundAndDeleted = internalList.remove(toRemove);
        if (!appointmentFoundAndDeleted) {
            throw new AppointmentNotFoundException();
        }
        return appointmentFoundAndDeleted;
    }

    public void setAppointments(UniqueAppointmentList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setAppointments(List<Appointment> appointments)
            throws DuplicateAppointmentException, DuplicateDateTimeException,
        ConcurrentAppointmentException, PastAppointmentException {
        requireAllNonNull(appointments);
        final UniqueAppointmentList replacement = new UniqueAppointmentList();
        for (final Appointment appointment : appointments) {
            replacement.add(appointment);
        }
        setAppointments(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Appointment> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Appointment> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueAppointmentList // instanceof handles nulls
                && this.internalList.equals(((UniqueAppointmentList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deletePetPatient(PetPatient target)
            throws PetPatientNotFoundException, AppointmentDependencyNotEmptyException {
        addressBook.removePetPatient(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized List<PetPatient> deletePetPatientDependencies(Person target) {
        List<PetPatient> petPatients = addressBook.removeAllPetPatientDependencies(target);
        indicateAddressBookChanged();
        return petPatients;
    }

    @Override
    public synchronized List<Appointment> deleteAppointmentDependencies(PetPatient target) {
        List<Appointment> dependenciesDeleted = addressBook.removeAllAppointmentDependencies(target);
        indicateAddressBookChanged();
        return dependenciesDeleted;
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteAppointment(Appointment target) throws AppointmentNotFoundException {
        addressBook.removeAppointment(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addAppointment(Appointment appointment)
            throws DuplicateAppointmentException, DuplicateDateTimeException,
        ConcurrentAppointmentException, PastAppointmentException {
        addressBook.addAppointment(appointment);
        updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        indicateAddressBookChanged();
    }


    @Override
    public void deleteTag(Tag tag) {
        addressBook.removeTag(tag);
    }

    //=========== Filtered Person List Accessors =============================================================

```
###### \java\seedu\address\model\ModelManager.java
``` java
    /**
     * Returns an unmodifiable view of the list of {@code Appointment} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Appointment> getFilteredAppointmentList() {
        return FXCollections.unmodifiableObservableList(filteredAppointments);
    }

    @Override
    public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
        requireNonNull(predicate);
        filteredAppointments.setPredicate(predicate);
    }

    //=========== Filtered Pet Patient List Accessors =============================================================

```
###### \java\seedu\address\model\petpatient\exceptions\PetDependencyNotEmptyException.java
``` java
/**
 * Signals that the operation is unable to continue because there are still pets dependent.
 */
public class PetDependencyNotEmptyException extends Exception {
    public PetDependencyNotEmptyException(String message) {
        super(message);
    }
}
```
###### \java\seedu\address\model\util\SampleDataUtil.java
``` java
/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Nric("S0123456B"),
                getTagSet("owner")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Nric("T0123456C"),
                getTagSet("owner")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Nric("G0123456A"),
                getTagSet("owner")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Nric("F0123456B"),
                getTagSet("owner")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), new Nric("S0163456E"),
                getTagSet("owner")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Nric("F0123056T"),
                getTagSet("owner")),
            new Person(new Name("Alexia Tan"), new Phone("67321372"), new Email("alexia@example.com"),
                new Address("260 Orchard Road, The Heeren ,04-30/31 238855, Singapore"), new Nric("S1199380Z"),
                getTagSet("owner")),
            new Person(new Name("Bernard Yeong"), new Phone("65457582"), new Email("bernardyeong@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Nric("S8267808E"),
                getTagSet("owner")),
            new Person(new Name("John Cena"), new Phone("93282203"), new Email("johncena@example.com"),
                new Address("5 Airport Cargo Road #452A Core H 4th Storey, 819462, Singapore"), new Nric("S6654649G"),
                getTagSet("owner")),
            new Person(new Name("Rick Sanchez"), new Phone("62653105"), new Email("ricksanchez@example.com"),
                new Address("15 Kian Teck Road 628770, Singapore"), new Nric("S5985945E"),
                getTagSet("owner")),
            new Person(new Name("Lee Tze Ting"), new Phone("63392060"), new Email("tzeting@example.com"),
                new Address("73 Bras Basah, 07-01 470765, Singapore"), new Nric("S1209036F"),
                getTagSet("volunteer")),
            new Person(new Name("Lee Yan Hwa"), new Phone("68845060"), new Email("yanhwa@example.com"),
                new Address("69 Mohamed Sultan Raod, 239015, Singapore"), new Nric("S3643153I"),
                getTagSet("volunteer")),
            new Person(new Name("Yuuri Katsuki"), new Phone("63353388"), new Email("yuuriviktor@example.com"),
                new Address("180 Clemenceau Avenue #06-01 Haw Par Centre, 239922, Singapore"), new Nric("S4176809F"),
                getTagSet("supplier")),
            new Person(new Name("Lu Li Ming"), new Phone("62255154"), new Email("liming@example.com"),
                new Address("69 Choa Chu Kang Loop #02-12, 689672, Singapore"), new Nric("S2557566J"),
                getTagSet("owner", "spca")),
            new Person(new Name("Eileen Yeo"), new Phone("67797976"), new Email("eileen@example.com"),
                new Address("Block 51 Ayer Rajah Crescent 02-15/16 Singapore 139948, Singapore"), new Nric("S9408343E"),
                getTagSet("volunteer", "owner")),
            new Person(new Name("Liew Chin Chuan"), new Phone("63921480"), new Email("chinchuan@example.com"),
                new Address("71 Sultan Gate, 198496, Singapore"), new Nric("S2330718I"),
                getTagSet("owner", "volunteer")),
            new Person(new Name("Samson Yeow"), new Phone("63488686"), new Email("samson@example.com"),
                new Address("86 East Coast Road, 428788, Singapore"), new Nric("S7165937B"),
                getTagSet("owner", "spca")),
            new Person(new Name("Codee Ong"), new Phone("63488686"), new Email("codeeo@example.com"),
                new Address("35 Changi North Crescent, 499641, Singapore"), new Nric("S1317219F"),
                getTagSet("owner")),
            new Person(new Name("Fuji Syuusuke"), new Phone("90245123"), new Email("fujis@example.com"),
                new Address("Blk 106 Bukit Purmei Street 10, #20-20"), new Nric("S9015638A"),
                getTagSet("supplier", "owner")),
            new Person(new Name("Tezuka Kunimitsu"), new Phone("92247377"), new Email("teuzkak@example.com"),
                new Address("Blk 106 Bukit Purmei Street 10, #20-20"), new Nric("S2012044D"),
                getTagSet("supplier", "owner"))
        };
    }

    public static PetPatient[] getSamplePetPatients() {
        return new PetPatient[] {
            new PetPatient(new PetPatientName("Ane"), new Species("Cat"), new Breed("Siamese"),
                new Colour("brown"), new BloodType("A"), new Nric("S0123456B"),
                getTagSet("hostile")),
            new PetPatient(new PetPatientName("Bei"), new Species("Cat"), new Breed("British Shorthair"),
                new Colour("grey"), new BloodType("B"), new Nric("T0123456C"),
                getTagSet("depression")),
            new PetPatient(new PetPatientName("Nei"), new Species("Cat"), new Breed("Maine Coon"),
                new Colour("black"), new BloodType("AB"), new Nric("T0123456C"),
                getTagSet("aggressive")),
            new PetPatient(new PetPatientName("Chae"), new Species("Cat"), new Breed("Russian Blue"),
                new Colour("grey"), new BloodType("A"), new Nric("G0123456A"),
                getTagSet("fiv")),
            new PetPatient(new PetPatientName("Don"), new Species("Dog"), new Breed("German Shepherd"),
                new Colour("brown"), new BloodType("DEA 4+"), new Nric("F0123456B"),
                getTagSet("aggressive")),
            new PetPatient(new PetPatientName("Este"), new Species("Dog"), new Breed("Golden Retriever"),
                new Colour("golden"), new BloodType("DEA 6+"), new Nric("S0163456E"),
                getTagSet("microchipped")),
            new PetPatient(new PetPatientName("Famm"), new Species("Dog"), new Breed("Pug"),
                new Colour("golden"), new BloodType("DEA 1.1-"), new Nric("F0123056T"),
                getTagSet("3legged")),
            new PetPatient(new PetPatientName("Plan"), new Species("Dog"), new Breed("Siberian Husky"),
                new Colour("white"), new BloodType("DEA 1.1+"), new Nric("F0123056T"),
                getTagSet("hostile", "newborn")),
            new PetPatient(new PetPatientName("Blu"), new Species("Cat"), new Breed("Burmese"),
                new Colour("brown"), new BloodType("A"), new Nric("S1199380Z"),
                getTagSet("hostile", "fiv")),
            new PetPatient(new PetPatientName("Red"), new Species("Cat"), new Breed("Cornish Rex"),
                new Colour("white"), new BloodType("B"), new Nric("S8267808E"),
                getTagSet("fiv")),
            new PetPatient(new PetPatientName("Fluffy"), new Species("Cat"), new Breed("Birman"),
                new Colour("white"), new BloodType("AB"), new Nric("S6654649G"),
                getTagSet("aggressive")),
            new PetPatient(new PetPatientName("Scooby"), new Species("Cat"), new Breed("Ocicat"),
                new Colour("white"), new BloodType("A"), new Nric("S5985945E"),
                getTagSet("hostile", "newborn")),
            new PetPatient(new PetPatientName("Snowball"), new Species("Dog"), new Breed("Rottweiler"),
                new Colour("brown and black"), new BloodType("DEA 4+"), new Nric("S2557566J"),
                getTagSet("aggressive", "microchipped")),
            new PetPatient(new PetPatientName("Wabbit"), new Species("Dog"), new Breed("Beagle"),
                new Colour("brown and white"), new BloodType("DEA 6+"), new Nric("S2557566J"),
                getTagSet("microchipped")),
            new PetPatient(new PetPatientName("Oreo"), new Species("Dog"), new Breed("Dalmation"),
                new Colour("black and white"), new BloodType("DEA 1.1+"), new Nric("S9408343E"),
                getTagSet("3legged")),
            new PetPatient(new PetPatientName("Milkshake"), new Species("Bird"), new Breed("Black Throated Sparrow"),
                new Colour("black and white"), new BloodType("NIL"), new Nric("S2330718I"),
                getTagSet("newborn", "missing")),
            new PetPatient(new PetPatientName("Ginger"), new Species("Bird"), new Breed("Amazon Parrot"),
                new Colour("green"), new BloodType("NIL"), new Nric("S2330718I"),
                getTagSet("hostile")),
            new PetPatient(new PetPatientName("Juniper"), new Species("Chinchilla"), new Breed("Lanigera Chinchilla"),
                new Colour("grey"), new BloodType("NIL"), new Nric("S2330718I"),
                getTagSet("newborn")),
            new PetPatient(new PetPatientName("Baron"), new Species("Chinchilla"), new Breed("Brevicaudata Chinchilla"),
                new Colour("black"), new BloodType("NIL"), new Nric("S7165937B"),
                getTagSet("microchipped", "allergy")),
            new PetPatient(new PetPatientName("Sting"), new Species("Guinea Pig"), new Breed("Abyssinian Guinea Pig"),
                new Colour("white"), new BloodType("B RH-"), new Nric("S7165937B"),
                getTagSet("newborn", "hostile")),
            new PetPatient(new PetPatientName("Riddle"), new Species("Guinea Pig"), new Breed("Skinny Pig"),
                new Colour("black and white"), new BloodType("A RH+"), new Nric("S7165937B"),
                getTagSet("aggressive", "allergy")),
            new PetPatient(new PetPatientName("Tiki"), new Species("Guinea Pig"), new Breed("Teddy Guinea Pig"),
                new Colour("golden"), new BloodType("AB RH+"), new Nric("S0163456E"),
                getTagSet("drooling", "newborn")),
            new PetPatient(new PetPatientName("Hero"), new Species("Dog"), new Breed("German Shepherd"),
                new Colour("black"), new BloodType("DEA 1.1+"), new Nric("S2012044D"),
                getTagSet("newborn")),
            new PetPatient(new PetPatientName("Thorn"), new Species("Cat"), new Breed("Chinchilla Persian"),
                new Colour("white"), new BloodType("AB"), new Nric("S9015638A"),
                getTagSet("newborn")),
            new PetPatient(new PetPatientName("Alpha"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("black and white"), new BloodType("DEA 4+"), new Nric("S1317219F"),
                getTagSet("aggressive", "newborn")),
            new PetPatient(new PetPatientName("Beta"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("brown and white"), new BloodType("DEA 6+"), new Nric("S1317219F"),
                getTagSet("microchipped", "hostile")),
            new PetPatient(new PetPatientName("Gamma"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("red and white"), new BloodType("DEA 1.1-"), new Nric("S1317219F"),
                getTagSet("microchipped")),
            new PetPatient(new PetPatientName("Delta"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("brown and white"), new BloodType("DEA 1.1+"), new Nric("S1317219F"),
                getTagSet("microchipped")),
            new PetPatient(new PetPatientName("Epsilon"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("seal and white"), new BloodType("DEA 4+"), new Nric("S1317219F"),
                getTagSet("microchipped", "aggressive")),
            new PetPatient(new PetPatientName("Zeta"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("sable and white"), new BloodType("DEA 4-"), new Nric("F0123056T"),
                getTagSet("microchipped", "senior")),
            new PetPatient(new PetPatientName("Eta"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("brown and white"), new BloodType("DEA 6+"), new Nric("S1317219F"),
                getTagSet("microchipped")),
            new PetPatient(new PetPatientName("Theta"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("gray and white"), new BloodType("DEA 1.1+"), new Nric("S1317219F"),
                getTagSet("microchipped", "senior")),
            new PetPatient(new PetPatientName("Iota"), new Species("Dog"), new Breed("Alaskan Malamute"),
                new Colour("black and white"), new BloodType("DEA 1.1+"), new Nric("S1317219F"),
                getTagSet("microchipped", "arthritis"))
        };
    }


    public static Appointment[] getSampleAppointments() {
        return new Appointment[] {
            new Appointment(new Nric("S0123456B"), new PetPatientName("Ane"), new Remark("nil"),
                    getLocalDateTime("2018-10-01 10:30"), getTagSet("checkup")),
            new Appointment(new Nric("T0123456C"), new PetPatientName("Bei"), new Remark("nil"),
                    getLocalDateTime("2018-10-02 10:30"), getTagSet("presurgery")),
            new Appointment(new Nric("F0123056T"), new PetPatientName("Famm"), new Remark("Home visit"),
                    getLocalDateTime("2018-10-03 10:30"), getTagSet("vaccination")),
            new Appointment(new Nric("F0123056T"), new PetPatientName("Plan"), new Remark("Home visit"),
                    getLocalDateTime("2018-10-03 11:00"), getTagSet("vaccination")),
            new Appointment(new Nric("T0123456C"), new PetPatientName("Bei"), new Remark("nil"),
                    getLocalDateTime("2018-10-06 10:30"), getTagSet("surgery")),
            new Appointment(new Nric("G0123456A"), new PetPatientName("Chae"), new Remark("nil"),
                    getLocalDateTime("2018-10-07 09:30"), getTagSet("checkup")),
            new Appointment(new Nric("F0123456B"), new PetPatientName("Don"), new Remark("nil"),
                    getLocalDateTime("2018-10-07 15:30"), getTagSet("microchipping")),
            new Appointment(new Nric("T0123456C"), new PetPatientName("Bei"), new Remark("nil"),
                    getLocalDateTime("2018-10-09 15:30"), getTagSet("postsurgery")),
            new Appointment(new Nric("T0123456C"), new PetPatientName("Nei"), new Remark("nil"),
                    getLocalDateTime("2018-10-09 16:00"), getTagSet("checkup")),
            new Appointment(new Nric("S1199380Z"), new PetPatientName("Blu"), new Remark("nil"),
                    getLocalDateTime("2018-06-01 10:30"), getTagSet("vaccination")),
            new Appointment(new Nric("S8267808E"), new PetPatientName("Red"), new Remark("Home visit"),
                    getLocalDateTime("2018-06-01 11:30"), getTagSet("checkup")),
            new Appointment(new Nric("S6654649G"), new PetPatientName("Fluffy"), new Remark("nil"),
                    getLocalDateTime("2018-06-02 10:30"), getTagSet("vaccination")),
            new Appointment(new Nric("S9408343E"), new PetPatientName("Oreo"), new Remark("nil"),
                    getLocalDateTime("2018-06-02 11:00"), getTagSet("microchipping")),
            new Appointment(new Nric("S2557566J"), new PetPatientName("Wabbit"), new Remark("nil"),
                    getLocalDateTime("2018-06-03 10:30"), getTagSet("sterilisation")),
            new Appointment(new Nric("S2330718I"), new PetPatientName("Ginger"), new Remark("nil"),
                    getLocalDateTime("2018-06-03 09:30"), getTagSet("checkup")),
            new Appointment(new Nric("F0123456B"), new PetPatientName("Juniper"), new Remark("Might require stay"),
                    getLocalDateTime("2018-06-04 15:30"), getTagSet("sterilisation")),
            new Appointment(new Nric("S7165937B"), new PetPatientName("Baron"), new Remark("nil"),
                    getLocalDateTime("2018-06-04 16:30"), getTagSet("checkup")),
            new Appointment(new Nric("S7165937B"), new PetPatientName("Sting"), new Remark("Home visit"),
                    getLocalDateTime("2018-06-05 16:00"), getTagSet("vaccination")),
            new Appointment(new Nric("S7165937B"), new PetPatientName("Riddle"), new Remark("Might require stay"),
                    getLocalDateTime("2018-06-06 15:30"), getTagSet("sterilisation")),
            new Appointment(new Nric("S0163456E"), new PetPatientName("Tiki"), new Remark("nil"),
                    getLocalDateTime("2018-06-07 16:30"), getTagSet("checkup")),
            new Appointment(new Nric("S2012044D"), new PetPatientName("Hero"), new Remark("Might require stay"),
                    getLocalDateTime("2018-06-08 16:00"), getTagSet("sterilisation")),
            new Appointment(new Nric("S9015638A"), new PetPatientName("Thorn"), new Remark("Might require stay"),
                    getLocalDateTime("2018-06-08 18:00"), getTagSet("sterilisation")),
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        try {
            AddressBook sampleAb = new AddressBook();
            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }
            for (PetPatient petPatient : getSamplePetPatients()) {
                sampleAb.addPetPatient(petPatient);
            }
            for (Appointment appointment : getSampleAppointments()) {
                sampleAb.addAppointment(appointment);
            }
            return sampleAb;
        } catch (DuplicatePersonException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        } catch (DuplicateNricException e) {
            throw new AssertionError("sample data cannot contain duplicate NRIC values", e);
        } catch (DuplicatePetPatientException e) {
            throw new AssertionError("sample data cannot contain duplicate pet patients", e);
        } catch (DuplicateDateTimeException e) {
            throw new AssertionError("sample data cannot contain double booked appointments", e);
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("sample data cannot contain duplicate appointments", e);
        } catch (ConcurrentAppointmentException cae) {
            throw new AssertionError("AddressBook should not add appointments to on-going appointment slots");
        } catch (PastAppointmentException pae) {
            throw new AssertionError("AddressBook should not add appointments with past DateTime");
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

    /**
     * Returns a LocalDateTime object of the given string.
     */
    private static LocalDateTime getLocalDateTime(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return LocalDateTime.parse(string, formatter);
    }

}
```
###### \java\seedu\address\storage\XmlAdaptedAppointment.java
``` java
/**
 * JAXB-friendly version of an Appointment.
 */
public class XmlAdaptedAppointment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Appointment %s field is missing!";

    @XmlElement(required = true)
    private String ownerNric;
    @XmlElement(required = true)
    private String petPatientName;
    @XmlElement(required = true)
    private String remark;
    @XmlElement(required = true)
    private String dateTime;

    @XmlElement
    private List<XmlAdaptedTag> appointmentTagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedAppointment.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAppointment() {}

    /**
     * Constructs an {@code XmlAdaptedAppointment} with the given appointment details.
     */
    public XmlAdaptedAppointment(String ownerNric, String petPatientName, String remark,
                                 String dateTime, List<XmlAdaptedTag> appointmentTagged) {
        this.ownerNric = ownerNric;
        this.petPatientName = petPatientName;
        this.remark = remark;
        this.dateTime = dateTime;
        if (appointmentTagged != null) {
            this.appointmentTagged = new ArrayList<>(appointmentTagged);
        }
    }

    /**
     * Converts a given Appointment into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedAppointment
     */
    public XmlAdaptedAppointment(Appointment source) {
        ownerNric = source.getOwnerNric().toString();
        petPatientName = source.getPetPatientName().toString();
        remark = source.getRemark().value;
        dateTime = source.getFormattedLocalDateTime();
        appointmentTagged = new ArrayList<>();
        for (Tag tag : source.getAppointmentTags()) {
            appointmentTagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted appointment object into the model's Appointment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted appointment
     */
    public Appointment toModelType() throws IllegalValueException {
        final List<Tag> appointmentTags = new ArrayList<>();
        for (XmlAdaptedTag tag : appointmentTagged) {
            appointmentTags.add(tag.toModelType());
        }

        if (this.ownerNric == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName()));
        }
        if (!Nric.isValidNric(this.ownerNric)) {
            throw new IllegalValueException(Nric.MESSAGE_NRIC_CONSTRAINTS);
        }
        final Nric ownerNric = new Nric(this.ownerNric);

        if (this.petPatientName == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, PetPatientName.class.getSimpleName()));
        }
        if (!PetPatientName.isValidName(this.petPatientName)) {
            throw new IllegalValueException(PetPatientName.MESSAGE_PET_NAME_CONSTRAINTS);
        }
        final PetPatientName petPatientName = new PetPatientName(this.petPatientName);

        if (this.remark == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName()));
        }
        if (!Remark.isValidRemark(this.remark)) {
            throw new IllegalValueException(Remark.MESSAGE_REMARK_CONSTRAINTS);
        }

        final Remark remark = new Remark(this.remark);

        if (this.dateTime == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, LocalDateTime.class.getSimpleName()));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = null;

        try {
            localDateTime = LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("Please follow the format of yyyy-MM-dd HH:mm");
        }

        final LocalDateTime dateTime = localDateTime;


        final Set<Tag> thisAppointmentTags = new HashSet<>(appointmentTags);
        return new Appointment(ownerNric, petPatientName, remark, dateTime, thisAppointmentTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAppointment)) {
            return false;
        }

        XmlAdaptedAppointment otherAppointment = (XmlAdaptedAppointment) other;
        return Objects.equals(ownerNric, otherAppointment.ownerNric)
                && Objects.equals(petPatientName, otherAppointment.petPatientName)
                && Objects.equals(remark, otherAppointment.remark)
                && Objects.equals(dateTime, otherAppointment.dateTime)
                && appointmentTagged.equals(otherAppointment.appointmentTagged);
    }
}
```
###### \java\seedu\address\ui\CalendarWindow.java
``` java
    private void changeYearView(Year year) {
        calendarView.showYear(year);
    }

    private void changeMonthView(YearMonth yearMonth) {
        calendarView.showYearMonth(yearMonth);
    }

    /**
     * changes the week view based on {@code date}.
     * Does some particular checks (as weekFields give diff result from calendarFX),
     * to ensure that it runs smoothly.
     */
    private void changeWeekView(LocalDate date) {
        WeekFields weekFields = WeekFields.SUNDAY_START;
        int week = date.get(weekFields.weekOfWeekBasedYear()) - 1;

        if (week == 0 && date.getMonthValue() == 12) {
            //wraparound
            week = 52;
            calendarView.showWeek(Year.of(date.getYear()), week);
        } else if (week == 0 && date.getMonthValue() == 1) {
            //wraparound
            LocalDate dateOfFirstJan = LocalDate.of(date.getYear(), 1, 1);
            if (dateOfFirstJan.getDayOfWeek().getValue() != 7) {
                week = 52;
                calendarView.showWeek(Year.of(date.getYear() - 1), week);
            } else {
                week = 53;
                calendarView.showWeek(Year.of(date.getYear() - 1), week);
            }
        } else {
            calendarView.showWeek(Year.of(date.getYear()), week);
        }
    }

    private void changeDayView(LocalDate date) {
        calendarView.showDate(date);
    }

    @Subscribe
    private void handleChangeYearView(ChangeYearViewRequestEvent event) {
        Year year = event.year;
        Platform.runLater(() -> changeYearView(year));
    }

    @Subscribe
    private void handleChangeMonthView(ChangeMonthViewRequestEvent event) {
        YearMonth yearMonth = event.yearMonth;
        Platform.runLater(() -> changeMonthView(yearMonth));
    }

    @Subscribe
    private void handleChangeWeekView(ChangeWeekViewRequestEvent event) {
        LocalDate date = event.date;
        Platform.runLater(() -> changeWeekView(date));
    }

    @Subscribe
    private void handleChangeDayView(ChangeDayViewRequestEvent event) {
        LocalDate date = event.date;
        Platform.runLater(() -> changeDayView(date));
    }

}


```
