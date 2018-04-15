# aquarinte
###### \java\seedu\address\commons\events\ui\ChangeThemeRequestEvent.java
``` java
/**
 * Indicates a request to change Medeina's theme
 */
public class ChangeThemeRequestEvent extends BaseEvent {
    public final Theme theme;

    public ChangeThemeRequestEvent(Theme theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\address\commons\util\StringUtil.java
``` java
    /**
     * Returns the actual required value of a String. Removes description (starts with "\t:").
     */
    public static String removeDescription(String s) {
        int descriptionStart = s.indexOf("\t:");

        if (descriptionStart > 0) {
            return s.substring(0, descriptionStart);
        }

        return s;
    }

```
###### \java\seedu\address\logic\commands\AddCommand.java
``` java
/**
 * Adds a Person, Petpatient and/or Appointment to Medeina.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = "To add a new contact: "
            + COMMAND_WORD + " " + OPTION_OWNER + " " + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_NRIC + "NRIC "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "To add a new pet patient: "
            + COMMAND_WORD + " " + OPTION_PETPATIENT + " " + PREFIX_NAME + "PET_NAME "
            + PREFIX_SPECIES + "SPECIES "
            + PREFIX_BREED + "BREED "
            + PREFIX_COLOUR + "COLOUR "
            + PREFIX_BLOODTYPE + "BLOOD_TYPE "
            + "[" + PREFIX_TAG + "TAG]... " + OPTION_OWNER + " " + PREFIX_NRIC + "NRIC\n"
            + "To add a new appointment: "
            + COMMAND_WORD + " " + OPTION_APPOINTMENT + " " + PREFIX_DATE + "DATE "
            + PREFIX_REMARK + "REMARK "
            + PREFIX_TAG + "TYPE OF APPOINTMENT... " + OPTION_OWNER + " " + PREFIX_NRIC + "NRIC "
            + OPTION_PETPATIENT + " " + PREFIX_NAME + " PET_NAME\n"
            + "To add all new: " + COMMAND_WORD + " " + OPTION_OWNER + " " + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_NRIC + "NRIC "
            + "[" + PREFIX_TAG + "TAG]... " + OPTION_PETPATIENT + " " + PREFIX_NAME + "PET_NAME "
            + PREFIX_SPECIES + "SPECIES "
            + PREFIX_BREED + "BREED "
            + PREFIX_COLOUR + "COLOUR "
            + PREFIX_BLOODTYPE + "BLOOD_TYPE "
            + "[" + PREFIX_TAG + "TAG]... " + OPTION_APPOINTMENT + " "  + PREFIX_DATE + "DATE "
            + PREFIX_REMARK + "REMARK "
            + PREFIX_TAG + "TYPE OF APPOINTMENT...";

    public static final String MESSAGE_ERROR_PERSON = "option " + OPTION_OWNER + "\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_NRIC + "NRIC "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + OPTION_OWNER + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_NRIC + "S1234567Q "
            + PREFIX_TAG + "supplier";

    public static final String MESSAGE_ERROR_APPOINTMENT = "option " + OPTION_APPOINTMENT + "\n"
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_REMARK + "REMARK "
            + PREFIX_TAG + "TYPE OF APPOINTMENT...\n"
            + "Example: " + OPTION_APPOINTMENT + " "
            + PREFIX_DATE + "2018-12-31 12:30 "
            + PREFIX_REMARK + "nil "
            + PREFIX_TAG + "checkup "
            + PREFIX_TAG + "vaccination";

    public static final String MESSAGE_ERROR_PETPATIENT = "option " + OPTION_PETPATIENT + "\n"
            + "Parameters: "
            + PREFIX_NAME + "PET_NAME "
            + PREFIX_SPECIES + "SPECIES "
            + PREFIX_BREED + "BREED "
            + PREFIX_COLOUR + "COLOUR "
            + PREFIX_BLOODTYPE + "BLOOD_TYPE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + OPTION_PETPATIENT + " "
            + PREFIX_NAME + "Jewel "
            + PREFIX_SPECIES + "Cat "
            + PREFIX_BREED + "Persian Ragdoll "
            + PREFIX_COLOUR + "calico "
            + PREFIX_BLOODTYPE + "AB "
            + PREFIX_TAG + "underweight";

    public static final String MESSAGE_SUCCESS_PERSON = "New contact added: %1$s\n";
    public static final String MESSAGE_SUCCESS_PETPATIENT = "New pet patient added: %1$s \n"
            + "under contact: %2$s";
    public static final String MESSAGE_SUCCESS_APPOINTMENT = "New appointment made: %1$s\n"
            + "under contact: %2$s\n"
            + "for pet patient: %3$s";
    public static final String MESSAGE_SUCCESS_EVERYTHING = MESSAGE_SUCCESS_PERSON
            + "New pet patient added: %2$s\n"
            + "New appointment made: %3$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This contact already exists in Medeina.";
    public static final String MESSAGE_DUPLICATE_NRIC = "There is already a contact with this NRIC.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This particular appointment already exists in Medeina.";
    public static final String MESSAGE_DUPLICATE_DATETIME = "This date time is already taken by another appointment.";
    public static final String MESSAGE_DUPLICATE_PET_PATIENT = "This pet patient already exists in Medeina";
    public static final String MESSAGE_INVALID_NRIC = "The specified NRIC does not belong to anyone in Medeina."
            + " Please add a new contact.";
    public static final String MESSAGE_INVALID_PET_PATIENT = "The specified pet cannot be found under the specified "
            + "contact in Medeina. Please add a new pet patient.";
    public static final String MESSAGE_MISSING_NRIC_PREFIX = "option -o\n"
            + "Missing prefix nr/ for NRIC.";
    public static final String MESSAGE_MISSING_PET_PATIENT_NAME_PREFIX = "option -p\n"
            + "Missing prefix n/ for pet patient name.";
    public static final String MESSAGE_CONCURRENT_APPOINTMENT = "Appointment cannot be concurrent with other "
            + "appointments.";
    public static final String MESSAGE_PAST_APPOINTMENT = "Appointment cannot be created with past DateTime.";

    private Person person;
    private PetPatient petPatient;
    private Appointment appt;
    private Nric ownerNric;
    private PetPatientName petPatientName;
    private int type;

    /**
     * Creates an AddCommand to add the specified {@code Person} and {@code PetPatient} and {@code Appointment}.
     */
    public AddCommand(Person person, PetPatient petPatient, Appointment appt) {
        requireNonNull(person);
        requireNonNull(petPatient);
        requireNonNull(appt);
        this.person = person;
        this.petPatient = petPatient;
        this.appt = appt;
        type = 1;
    }

    /**
     * Creates an AddCommand to add the specified {@code Appointment} if an existing Person object getNric() is
     * equivalent to {@code Nric}, and an existing PetPatient object getOwner() is equivalent to {@code ownerNric}
     * and getName() equivalent to {@code PetPatientName}.
     */
    public AddCommand(Appointment appt, Nric ownerNric, PetPatientName petPatientName) {
        requireNonNull(appt);
        requireNonNull(ownerNric);
        requireNonNull(petPatientName);
        this.appt = appt;
        this.ownerNric = ownerNric;
        this.petPatientName = petPatientName;
        type = 2;
    }

    /**
     * Creates an AddCommand to add the specified {@code PetPatient} if an existing Person object getNric() is
     * equivalent to {@code Nric}.
     */
    public AddCommand(PetPatient petPatient, Nric ownerNric) {
        requireNonNull(petPatient);
        requireNonNull(ownerNric);
        this.petPatient = petPatient;
        this.ownerNric = ownerNric;
        type = 3;
    }

    /**
     * Creates an AddCommand to add the specified {@code Person}.
     */
    public AddCommand(Person owner) {
        requireNonNull(owner);
        person = owner;
        type = 4;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            switch (type) {
            case 1: return addAllNew();
            case 2: return addNewAppointment();
            case 3: return addNewPetPatient();
            case 4: return addNewPerson();
            default: throw new CommandException(MESSAGE_USAGE);
            }

        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (DuplicateNricException e) {
            throw new CommandException(MESSAGE_DUPLICATE_NRIC);
        } catch (DuplicatePetPatientException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PET_PATIENT);
        } catch (DuplicateAppointmentException e) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (DuplicateDateTimeException e) {
            throw new CommandException(MESSAGE_DUPLICATE_DATETIME);
        } catch (ConcurrentAppointmentException e) {
            throw new CommandException(MESSAGE_CONCURRENT_APPOINTMENT);
        } catch (PastAppointmentException e) {
            throw new CommandException(MESSAGE_PAST_APPOINTMENT);
        }
    }

    private CommandResult addNewPerson() throws DuplicatePersonException, DuplicateNricException {
        model.addPerson(person);
        return new CommandResult(String.format(MESSAGE_SUCCESS_PERSON, person));
    }

    /**
     * Adds a new pet patient under an existing person.
     */
    private CommandResult addNewPetPatient() throws DuplicatePetPatientException, CommandException {
        person = getValidOwner(ownerNric);
        model.addPetPatient(petPatient);
        return new CommandResult(String.format(MESSAGE_SUCCESS_PETPATIENT, petPatient, person));
    }

    /**
     * Adds a new appointment for an existing pet patient, under an existing person.
     */
    private CommandResult addNewAppointment() throws CommandException, DuplicateAppointmentException,
            DuplicateDateTimeException, ConcurrentAppointmentException, PastAppointmentException {
        person = getValidOwner(ownerNric);
        petPatient = getValidPetPatient(ownerNric, petPatientName);
        model.addAppointment(appt);
        return new CommandResult(String.format(MESSAGE_SUCCESS_APPOINTMENT, appt, person, petPatient));
    }

    /**
     * Adds a new appointment for a new pet patient under a new person.
     */
    private CommandResult addAllNew() throws DuplicatePersonException, DuplicateNricException,
            DuplicatePetPatientException, DuplicateAppointmentException, DuplicateDateTimeException,
        ConcurrentAppointmentException, PastAppointmentException {
        model.addPerson(person);
        model.addPetPatient(petPatient);
        model.addAppointment(appt);
        return new CommandResult(String.format(MESSAGE_SUCCESS_EVERYTHING, person, petPatient, appt));
    }

    /**
     * Returns a person object that exists in Medeina.
     */
    private Person getValidOwner(Nric ownerNric) throws CommandException {
        Person validOwner = model.getPersonWithNric(ownerNric);
        if (validOwner == null) {
            throw new CommandException(MESSAGE_INVALID_NRIC);
        }
        return validOwner;
    }

    /**
     * Returns a petpatient object that exists in Medeina.
     */
    private PetPatient getValidPetPatient(Nric ownerNric, PetPatientName petPatientName) throws CommandException {
        PetPatient validPatient  = model.getPetPatientWithNricAndName(ownerNric, petPatientName);
        if (validPatient == null) {
            throw new CommandException(MESSAGE_INVALID_PET_PATIENT);
        }
        return validPatient;
    }

    /**
     * Checks if two objects are the same for equals() method.
     *
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

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;

        boolean personSame = isTheSame(person, otherAddCommand.person);
        boolean petPatientSame = isTheSame(petPatient, otherAddCommand.petPatient);
        boolean appointmentSame = isTheSame(appt, otherAddCommand.appt);

        if (personSame && petPatientSame && appointmentSame) {
            return true;
        } else {
            return false;
        }
    }
}
```
###### \java\seedu\address\logic\commands\ChangeThemeCommand.java
``` java
/**
 * Changes the theme of Medeina.
 */
public class ChangeThemeCommand extends Command {
    public static final String COMMAND_WORD = "theme";
    public static final String COMMAND_ALIAS = "t";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Change Medeina's theme to the"
            + "specified theme name (case-insensitive)\n"
            + "Parameters: THEME NAME\n"
            + "Example: " + COMMAND_WORD + " light";

    private String result;

    private final Theme theme;

    public ChangeThemeCommand(Theme theme) {
        requireNonNull(theme);
        this.theme = theme;
        result = "Current theme: " + theme.getThemeName();
    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ChangeThemeRequestEvent(theme));
        return new CommandResult(result);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChangeThemeCommand // instanceof handles nulls
                && theme.equals(((ChangeThemeCommand) other).theme));
    }
}
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public Set<String> getAllCommandWords() {
        return cliSyntax.getCommandWords();
    }

    @Override
    public Set<String> getCommandWordsWithOptionPrefix() {
        return cliSyntax.getCommandWordsWithOptionPrefix();
    }

    @Override
    public Set<String> getAllPrefixes() {
        return cliSyntax.getPrefixes();
    }

    @Override
    public Set<String> getAllOptions() {
        return cliSyntax.getOptions();
    }

    @Override
    public Set<String> getAllNric() {
        return nricInModel;
    }

    @Override
    public Set<String> getAllPersonTags() {
        Set<String> personTags = personTagsInModel.stream()
                .map(pt -> pt.tagName)
                .collect(Collectors.toSet());
        return personTags;
    }

    @Override
    public Set<String> getAllPetPatientNames() {
        return petPatientNamesInModel;
    }

    @Override
    public Set<String> getAllPetPatientSpecies() {
        return speciesInModel;
    }

    @Override
    public Set<String> getAllPetPatientBreeds() {
        return breedsInModel;
    }

    @Override
    public Set<String> getAllPetPatientColours() {
        return coloursInModel;
    }

    @Override
    public Set<String> getAllPetPatientBloodTypes() {
        return bloodTypesInModel;
    }

    @Override
    public Set<String> getAllPetPatientTags() {
        Set<String> petPatientTags = petPatientTagsInModel.stream()
                .map(ppt -> ppt.tagName)
                .collect(Collectors.toSet());
        return petPatientTags;
    }

    @Override
    public Set<String> getAllAppointmentTags() {
        Set<String> appointmentTags = appointmentTagsInModel.stream()
                .map(a -> a.tagName)
                .collect(Collectors.toSet());
        return appointmentTags;
    }

    @Override
    public void setAttributesForPersonObjects() {
        nricInModel = new HashSet<>();
        personTagsInModel = new HashSet<>();

        for (Person p : model.getAddressBook().getPersonList()) {
            nricInModel.add(p.getNric().toString());
            personTagsInModel.addAll(p.getTags());
        }
    }

    @Override
    public void setAttributesForPetPatientObjects() {
        petPatientNamesInModel = new HashSet<>();
        speciesInModel = new HashSet<>();
        breedsInModel = new HashSet<>();
        coloursInModel = new HashSet<>();
        bloodTypesInModel = new HashSet<>();
        petPatientTagsInModel = new HashSet<>();

        for (PetPatient p : model.getAddressBook().getPetPatientList()) {
            petPatientNamesInModel.add(p.getName().toString());
            speciesInModel.add(p.getSpecies().toString());
            breedsInModel.add(p.getBreed().toString());
            coloursInModel.add(p.getColour().toString());
            bloodTypesInModel.add(p.getBloodType().toString());
            petPatientTagsInModel.addAll(p.getTags());
        }
    }

    @Override
    public void setAttributesForAppointmentObjects() {
        appointmentTagsInModel = new HashSet<>();
        for (Appointment a : model.getAddressBook().getAppointmentList()) {
            appointmentTagsInModel.addAll(a.getTag());
        }
    }

    @Override
    public Set<String> getAllTagsInModel() {
        Set<String> tagsInModel = new HashSet<>();
        for (Tag t : model.getTagList()) {
            tagsInModel.add(t.tagName);
        }
        return tagsInModel;
    }
}
```
###### \java\seedu\address\logic\parser\AddCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddCommand object.
 */
public class AddCommandParser implements Parser<AddCommand> {

    private static final Pattern ADD_COMMAND_FORMAT_ALL_NEW = Pattern.compile("-(o)+(?<ownerInfo>.*)"
        + "-(p)+(?<petInfo>.*)-(a)+(?<apptInfo>.*)");
    private static final Pattern ADD_COMMAND_FORMAT_OWNER_ONLY = Pattern.compile("-(o)+(?<ownerInfo>.*)");
    private static final Pattern ADD_COMMAND_FORMAT_NEW_PET_EXISTING_OWNER = Pattern.compile("-(p)+(?<petInfo>.*)"
        + "-(o)+(?<ownerNric>.*)");
    private static final Pattern ADD_COMMAND_FORMAT_NEW_APPT_EXISTING_OWNER_PET = Pattern.compile("-(a)+(?<apptInfo>.*)"
        + "-(o)+(?<ownerNric>.*)" + "-(p)+(?<petName>.*)");

    /**
     * Parses the given {@code String} of arguments in the context of the Person class
     * and returns an Person object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Person parsePerson(String ownerInfo) throws ParseException {
        ArgumentMultimap argMultimapOwner =
            ArgumentTokenizer.tokenize(ownerInfo, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_NRIC, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimapOwner, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_NRIC)
            || !argMultimapOwner.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_PERSON));
        }

        try {
            Name ownerName = ParserUtil.parseName(argMultimapOwner.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimapOwner.getValue(PREFIX_PHONE)).get();
            Email email = ParserUtil.parseEmail(argMultimapOwner.getValue(PREFIX_EMAIL)).get();
            Address address = ParserUtil.parseAddress(argMultimapOwner.getValue(PREFIX_ADDRESS)).get();
            Nric nric = ParserUtil.parseNric(argMultimapOwner.getValue(PREFIX_NRIC)).get();
            Set<Tag> ownerTagList = ParserUtil.parseTags(argMultimapOwner.getAllValues(PREFIX_TAG));

            Person owner = new Person(ownerName, phone, email, address, nric, ownerTagList);
            return owner;

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of the Appointment class
     * and returns an Appointment object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Appointment parseAppointment(String apptInfo) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(apptInfo, PREFIX_DATE, PREFIX_REMARK, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_REMARK, PREFIX_TAG)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_APPOINTMENT));
        }

        try {
            LocalDateTime localDateTime = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_DATE)).get();
            if (localDateTime.isBefore(LocalDateTime.now())) {
                throw new PastAppointmentException();
            }

            Remark remark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).get();
            Set<Tag> type = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            Appointment appointment = new Appointment(remark, localDateTime, type);
            return appointment;

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);

        }
    }
    /**
     * Parses the given {@code String} of arguments in the context of the PetPatient class
     * and returns an PetPatient object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public PetPatient parsePetPatient(String petInfo) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(petInfo, PREFIX_NAME, PREFIX_SPECIES, PREFIX_BREED, PREFIX_COLOUR,
                PREFIX_BLOODTYPE, PREFIX_TAG);

        if (!arePrefixesPresent(
            argMultimap, PREFIX_NAME, PREFIX_BREED, PREFIX_SPECIES, PREFIX_COLOUR, PREFIX_BLOODTYPE)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                    AddCommand.MESSAGE_ERROR_PETPATIENT));
        }

        try {
            PetPatientName name = ParserUtil.parsePetPatientName(argMultimap.getValue(PREFIX_NAME)).get();
            Species species = ParserUtil.parseSpecies(argMultimap.getValue(PREFIX_SPECIES)).get();
            Breed breed = ParserUtil.parseBreed(argMultimap.getValue(PREFIX_BREED)).get();
            Colour color = ParserUtil.parseColour(argMultimap.getValue(PREFIX_COLOUR)).get();
            BloodType bloodType = ParserUtil.parseBloodType(argMultimap.getValue(PREFIX_BLOODTYPE)).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            PetPatient petPatient = new PetPatient(name, species, breed, color, bloodType, tagList);

            return petPatient;
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of the Nric class
     * and returns a Nric object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Nric parseNric(String nric) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(nric, PREFIX_NRIC);

        if (!arePrefixesPresent(argMultimap, PREFIX_NRIC) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_MISSING_NRIC_PREFIX));
        }

        try {
            Nric validNric = ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC)).get();
            return validNric;

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of the PetPatientName class
     * and returns a PetPatientName object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public PetPatientName parsePetPatientName(String petName) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(petName, PREFIX_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_PARAMETER_FORMAT,
                AddCommand.MESSAGE_MISSING_PET_PATIENT_NAME_PREFIX));
        }

        try {
            PetPatientName petPatientName = ParserUtil.parsePetPatientName(argMultimap.getValue(PREFIX_NAME)).get();
            return petPatientName;

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of AddCommand
     * and returns an AddCommand object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    private AddCommand createNewOwnerPetAppt(String ownerInfo, String petInfo, String apptInfo)
        throws ParseException {
        Person owner = parsePerson(ownerInfo);

        PetPatient petPatient = parsePetPatient(petInfo);
        petPatient.setOwnerNric(owner.getNric());

        Appointment appt = parseAppointment(apptInfo);
        appt.setOwnerNric(owner.getNric());
        appt.setPetPatientName(petPatient.getName());

        return new AddCommand(owner, petPatient, appt);
    }

    /**
     * Parses the given {@code String} of arguments in the context of AddCommand
     * and returns an AddCommand object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    private AddCommand createNewApptforExistingOwnerAndPet(String apptInfo, String ownerNric, String petName)
        throws ParseException {
        Appointment appt = parseAppointment(apptInfo);
        Nric nric = parseNric(ownerNric);
        PetPatientName petPatientName = parsePetPatientName(petName);

        appt.setOwnerNric(nric);
        appt.setPetPatientName(petPatientName);

        return new AddCommand(appt, nric, petPatientName);
    }

    /**
     * Parses the given {@code String} of arguments in the context of AddCommand
     * and returns an AddCommand object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    private AddCommand createNewPetForExistingPerson(String petInfo, String ownerNric) throws ParseException {
        PetPatient petPatient = parsePetPatient(petInfo);
        Nric nric = parseNric(ownerNric);

        petPatient.setOwnerNric(nric);

        return new AddCommand(petPatient, nric);
    }

    /**
     * Parses the given {@code String} of arguments in the context of AddCommand
     * and returns an AddCommand object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    private AddCommand parseNewOwnerOnly(String ownerInfo) throws ParseException {
        Person owner = parsePerson(ownerInfo);
        return new AddCommand(owner);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of AddCommand
     * and returns an AddCommand object.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        //to add a new person (owner), new pet patient, and a new appointment
        final Matcher matcherForAllNew = ADD_COMMAND_FORMAT_ALL_NEW.matcher(trimmedArgs);
        if (matcherForAllNew.matches()) {
            String ownerInfo = matcherForAllNew.group("ownerInfo");
            String petInfo = matcherForAllNew.group("petInfo");
            String apptInfo = matcherForAllNew.group("apptInfo");
            return createNewOwnerPetAppt(ownerInfo, petInfo, apptInfo);
        }

        //add a new appointment for existing person and pet patient
        final Matcher matcherForNewAppt = ADD_COMMAND_FORMAT_NEW_APPT_EXISTING_OWNER_PET.matcher(trimmedArgs);
        if (matcherForNewAppt.matches()) {
            String apptInfo = matcherForNewAppt.group("apptInfo");
            String ownerNric = matcherForNewAppt.group("ownerNric");
            String petName = matcherForNewAppt.group("petName");
            return createNewApptforExistingOwnerAndPet(apptInfo, ownerNric, petName);
        }

        //add a new patient to an existing owner
        final Matcher matcherForNewPet = ADD_COMMAND_FORMAT_NEW_PET_EXISTING_OWNER.matcher(trimmedArgs);
        if (matcherForNewPet.matches()) {
            String petInfo = matcherForNewPet.group("petInfo");
            String ownerNric = matcherForNewPet.group("ownerNric");
            return createNewPetForExistingPerson(petInfo, ownerNric);
        }

        //add a new person
        final Matcher matcherForNewPerson = ADD_COMMAND_FORMAT_OWNER_ONLY.matcher(trimmedArgs);
        if (matcherForNewPerson.matches()) {
            String ownerInfo = matcherForNewPerson.group("ownerInfo");
            return parseNewOwnerOnly(ownerInfo);
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

    }

}
```
###### \java\seedu\address\logic\parser\ChangeThemeCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ChangeThemeCommand object
 */
public class ChangeThemeCommandParser implements Parser<ChangeThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ChangeThemeCommand
     * and returns a ChangeThemeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ChangeThemeCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeThemeCommand.MESSAGE_USAGE));
        }

        if (hasMoreThanOneArgument(trimmedArgs)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeThemeCommand.MESSAGE_USAGE));
        }

        if (!Theme.hasValidThemeName(trimmedArgs.toLowerCase())) {
            throw new ParseException(Theme.MESSAGE_THEME_CONSTRAINTS);
        }

        return new ChangeThemeCommand(new Theme(trimmedArgs.toLowerCase()));
    }

    /**
     * Returns true if {@code trimmedArgs} contains more than 1 argument/word (separated by space).
     */
    private boolean hasMoreThanOneArgument(String trimmedArgs) {
        String[] splitArgs = trimmedArgs.split(" ");
        if (splitArgs.length > 1) {
            return true;
        }
        return false;
    }

}
```
###### \java\seedu\address\logic\parser\CliSyntax.java
``` java
    /* Prefix with description */
    public static final String PREFIX_NAME_DESC = PREFIX_NAME.toString() + "\t: name";
    public static final String PREFIX_PHONE_DESC = PREFIX_PHONE.toString() + "\t: phone";
    public static final String PREFIX_EMAIL_DESC = PREFIX_EMAIL.toString() + "\t: email";
    public static final String PREFIX_ADDRESS_DESC = PREFIX_ADDRESS.toString() + "\t: address";
    public static final String PREFIX_NRIC_DESC = PREFIX_NRIC.toString() + "\t: NRIC";
    public static final String PREFIX_TAG_DESC = PREFIX_TAG.toString() + "\t: tag";
    public static final String PREFIX_REMARK_DESC = PREFIX_REMARK.toString() + "\t: remark";
    public static final String PREFIX_DATE_DESC = PREFIX_DATE.toString() + "\t: yyyy-mm-dd hh:mm";
    public static final String PREFIX_SPECIES_DESC = PREFIX_SPECIES.toString() + "\t: species";
    public static final String PREFIX_BREED_DESC = PREFIX_BREED.toString() + "\t: breed";
    public static final String PREFIX_COLOUR_DESC = PREFIX_COLOUR.toString() + "\t: colour";
    public static final String PREFIX_BLOODTYPE_DESC = PREFIX_BLOODTYPE.toString() + "\t: blood type";

    /* Option definitions */
    public static final String OPTION_OWNER = "-o";
    public static final String OPTION_PETPATIENT = "-p";
    public static final String OPTION_APPOINTMENT = "-a";
    public static final String OPTIONFORCE_OWNER = "-fo";
    public static final String OPTIONFORCE_PETPATIENT = "-fp";
    public static final String OPTIONFORCE_APPOINTMENT = "-fa";
    public static final String OPTION_YEAR = "-y";
    public static final String OPTION_MONTH = "-m";
    public static final String OPTION_WEEK = "-w";
    public static final String OPTION_DAY = "-d";

    /* Option with description */
    public static final String OPTION_OWNER_DESC = OPTION_OWNER + "\t: person/owner";
    public static final String OPTION_PETPATIENT_DESC = OPTION_PETPATIENT + "\t: pet patient";
    public static final String OPTION_APPOINTMENT_DESC = OPTION_APPOINTMENT + "\t: appointment";
    public static final String OPTIONFORCE_OWNER_DESC = OPTIONFORCE_OWNER + "\t: force delete person/owner";
    public static final String OPTIONFORCE_PETPATIENT_DESC = OPTIONFORCE_PETPATIENT + "\t: force delete pet patient";
    public static final String OPTIONFORCE_APPOINTMENT_DESC = OPTIONFORCE_APPOINTMENT + "\t: force delete appointment";
    public static final String OPTION_YEAR_DESC = OPTION_YEAR + "\t: calendar year view";
    public static final String OPTION_MONTH_DESC = OPTION_MONTH + "\t: calendar month view";
    public static final String OPTION_WEEK_DESC = OPTION_WEEK + "\t: calendar week view";
    public static final String OPTION_DAY_DESC = OPTION_DAY + "\t: calendar day view";

    private static CliSyntax instance;

    private static final Set<String> prefixes = Stream.of(
            PREFIX_NAME_DESC, PREFIX_PHONE_DESC, PREFIX_EMAIL_DESC, PREFIX_ADDRESS_DESC, PREFIX_NRIC_DESC,
            PREFIX_BREED_DESC, PREFIX_SPECIES_DESC, PREFIX_COLOUR_DESC, PREFIX_BLOODTYPE_DESC, PREFIX_DATE_DESC,
            PREFIX_REMARK_DESC, PREFIX_TAG_DESC)
            .collect(Collectors.toSet());

    private static final Set<String> commandWordsWithOptionPrefix = Stream.of(
            AddCommand.COMMAND_WORD, EditCommand.COMMAND_WORD, DeleteCommand.COMMAND_WORD, FindCommand.COMMAND_WORD,
            ListAppointmentCommand.COMMAND_WORD).collect(Collectors.toSet());

    private static final Set<String> commandWords = Stream.of(
            AddCommand.COMMAND_WORD, EditCommand.COMMAND_WORD, DeleteCommand.COMMAND_WORD, ListCommand.COMMAND_WORD,
            FindCommand.COMMAND_WORD, ChangeThemeCommand.COMMAND_WORD, ClearCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD, ExitCommand.COMMAND_WORD, RedoCommand.COMMAND_WORD, UndoCommand.COMMAND_WORD,
            HistoryCommand.COMMAND_WORD, ListAppointmentCommand.COMMAND_WORD).collect(Collectors.toSet());

    private static final Set<String> options = Stream.of(OPTION_OWNER_DESC, OPTION_PETPATIENT_DESC,
            OPTION_APPOINTMENT_DESC, OPTIONFORCE_OWNER_DESC, OPTIONFORCE_PETPATIENT_DESC, OPTIONFORCE_APPOINTMENT_DESC,
            OPTION_YEAR_DESC, OPTION_MONTH_DESC, OPTION_WEEK_DESC, OPTION_DAY_DESC)
            .collect(Collectors.toSet());

    public static final int MAX_SYNTAX_SIZE = Math.max(commandWords.size(), Math.max(prefixes.size(), options.size()));

    public static CliSyntax getInstance() {
        if (instance == null) {
            instance = new CliSyntax();
        }
        return instance;
    }

    public Set<String> getCommandWords() {
        return CliSyntax.commandWords;
    }

    public Set<String> getCommandWordsWithOptionPrefix() {
        return CliSyntax.commandWordsWithOptionPrefix;
    }

    public Set<String> getPrefixes() {
        return CliSyntax.prefixes;
    }

    public Set<String> getOptions() {
        return CliSyntax.options;
    }

}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     * First character of each word will be set to upper case.
     * All other characters will be set to lower case.
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        String[] wordsInName = trimmedName.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String n : wordsInName) {
            formattedName = formattedName.append(n.substring(0, 1).toUpperCase())
                    .append(n.substring(1).toLowerCase()).append(" ");
        }
        return new Name(formattedName.toString().trim());
    }
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String name} into a {@code PetPatientName}.
     * Leading and trailing whitespaces will be trimmed.
     * First character of each word will be set to upper case.
     * All other characters will be set to lower case.
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static PetPatientName parsePetPatientName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!PetPatientName.isValidName(trimmedName)) {
            throw new IllegalValueException(PetPatientName.MESSAGE_PET_NAME_CONSTRAINTS);
        }
        String[] wordsInName = trimmedName.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String n : wordsInName) {
            formattedName = formattedName.append(n.substring(0, 1).toUpperCase())
                    .append(n.substring(1).toLowerCase()).append(" ");
        }
        return new PetPatientName(formattedName.toString().trim());
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public Person getPersonWithNric(Nric ownerNric) {
        for (Person p : addressBook.getPersonList()) {
            if (p.getNric().equals(ownerNric)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public PetPatient getPetPatientWithNricAndName(Nric ownerNric, PetPatientName petPatientName) {
        for (PetPatient p : addressBook.getPetPatientList()) {
            if (p.getOwner().equals(ownerNric) && p.getName().equals(petPatientName)) {
                return p;
            }
        }
        return null;
    }

```
###### \java\seedu\address\model\theme\Theme.java
``` java
/**
 * Represents a Theme in the address book.
 */
public class Theme {

    private static String[] themes = {"dark", "light"};
    private static String[] themesLocation = {"/view/DarkTheme.css", "/view/LightTheme.css"};

    public static final String MESSAGE_THEME_CONSTRAINTS = "Please specify one of the following themes:\n"
            + Arrays.stream(themes).collect(Collectors.joining(", "));

    public final String selectedThemePath;

    /**
     * Constructs a {@code Theme}.
     *
     * @param themeName A valid theme name.
     */
    public Theme(String themeName) {
        requireNonNull(themeName);
        checkArgument(hasValidThemeName(themeName.toLowerCase()), MESSAGE_THEME_CONSTRAINTS);
        selectedThemePath = themesLocation[Arrays.asList(themes).indexOf(themeName.toLowerCase())];
    }

    /**
     * Returns true if a given string is a valid theme name.
     */
    public static boolean hasValidThemeName(String themeName) {
        boolean isValid = Arrays.stream(themes).anyMatch(themeName::equals);
        return isValid;
    }

    public String getThemeName() {
        return themes[Arrays.asList(themesLocation).indexOf(selectedThemePath)];
    }

    public String getThemePath() {
        return selectedThemePath;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Theme // instanceof handles nulls
                && this.selectedThemePath.equals(((Theme) other).selectedThemePath)); // state check
    }

    @Override
    public int hashCode() {
        return selectedThemePath.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + selectedThemePath + ']';
    }
}
```
###### \java\seedu\address\ui\Autocomplete.java
``` java
/**
 * Handles case-insensitive autocompletion of command line syntax,
 * and also some user input parameters: Nric, pet patient name, species, tags etc.
 */
public class Autocomplete {

    private static final Logger logger = LogsCenter.getLogger(Autocomplete.class);
    private static final int MAX_SUGGESTION_COUNT = CliSyntax.MAX_SYNTAX_SIZE;
    private static Autocomplete instance;
    private Logic logic;
    private String trimmedCommandInput;
    private String[] trimmedCommandInputArray;
    private String commandWord;
    private String option;
    private String targetWord;
    private Set<String> tagSet;

    public static Autocomplete getInstance() {
        if (instance == null) {
            instance = new Autocomplete();
            EventsCenter.getInstance().registerHandler(instance);
        }
        return instance;
    }

    /**
     * Initalizes or updates data required for autocomplete.
     */
    public void init(Logic logic) {
        this.logic = logic;
        logic.setAttributesForPersonObjects();
        logic.setAttributesForPetPatientObjects();
        logic.setAttributesForAppointmentObjects();
    }

    /**
     * Returns a list of suggestions for autocomplete based on user input (up to current caret position).
     *
     * @param commandTextField Command box that holds user input.
     */
    public List<String> getSuggestions(TextField commandTextField) {
        int cursorPosition = commandTextField.getCaretPosition();
        trimmedCommandInput = StringUtil.leftTrim(commandTextField.getText(0, cursorPosition));

        // split string, but retain all whitespaces in array "trimmedCommandInputArray"
        trimmedCommandInputArray = trimmedCommandInput.split("((?<= )|(?= ))", -1);

        commandWord = trimmedCommandInputArray[0];
        targetWord = trimmedCommandInputArray[trimmedCommandInputArray.length - 1].toLowerCase();
        setOption();

        if (trimmedCommandInputArray.length <= 2) {
            return getCommandWordSuggestions();
        }

        if (!targetWord.equals("") && hasOptionsAndPrefixes()) {
            if (hasAddCommandReferNric() || hasEditCommandReferNric() || hasFindCommandReferNric()) {
                return getNricSuggestions();
            }

            if (hasReferenceToExistingPetPatientNames()) {
                return getPetPatientNameSuggestions();
            }

            if (targetWord.startsWith(PREFIX_SPECIES.toString())) {
                return getPetPatientSpeciesSuggestions();
            }

            if (targetWord.startsWith(PREFIX_BREED.toString())) {
                return getPetPatientBreedSuggestions();
            }

            if (targetWord.startsWith(PREFIX_COLOUR.toString())) {
                return getPetPatientColourSuggestions();
            }

            if (targetWord.startsWith(PREFIX_BLOODTYPE.toString())) {
                return getPetPatientBloodTypeSuggestions();
            }

            if (targetWord.startsWith(PREFIX_TAG.toString())) {
                return getTagSuggestions();
            }

            if (targetWord.startsWith("-")) {
                return getOptionSuggestions();
            }

            return getPrefixSuggestions();

        } else {

            if (hasOptionsAndPrefixes()) {
                return getPrefixSuggestions();
            }
        }

        return new ArrayList<String>();
    }

    /**
     * Returns false if the command is one that does not require any options or prefixes in its syntax.
     */
    private boolean hasOptionsAndPrefixes() {
        if (logic.getCommandWordsWithOptionPrefix().contains(commandWord)) {
            return true;
        }

        return false;
    }

    /**
     * Checks if command input {@code trimmedCommandInputArray} contains the "add" command with reference to existing
     * persons' Nric, and determine if autocomplete for persons' Nric is necessary.
     *
     * Returns false if the command input is to add a new person.
     */
    private boolean hasAddCommandReferNric() {
        // adding a new owner will not have autocomplete for Nric
        if (commandWord.equals(AddCommand.COMMAND_WORD)
                && trimmedCommandInputArray[2].equals(OPTION_OWNER)) {
            return false;
        }

        if (commandWord.equals(AddCommand.COMMAND_WORD)
                && trimmedCommandInputArray[trimmedCommandInputArray.length - 3].equals(OPTION_OWNER)
                && targetWord.startsWith(PREFIX_NRIC.toString())) {
            return true;
        }
        return false;
    }

    /**
     * Checks if command input {@code trimmedCommandInputArray} contains the "edit" command with reference to existing
     * persons' Nric, and determine if autocomplete for persons' Nric is necessary.
     *
     * Returns true if editing the owner's nric of a pet patient.
     */
    private boolean hasEditCommandReferNric() {
        if (commandWord.equals(EditCommand.COMMAND_WORD)
                && option.equals(OPTION_PETPATIENT)
                && targetWord.startsWith(PREFIX_NRIC.toString())) {
            return true;
        }
        return false;
    }

    /**
     * Checks if command input {@code trimmedCommandInputArray} contains the "find" command with reference to existing
     * persons' Nric, and determine if autocomplete for persons' Nric is necessary.
     *
     * Returns true if finding a person by nric.
     */
    private boolean hasFindCommandReferNric() {
        if (commandWord.equals(FindCommand.COMMAND_WORD)
                && option.equals(OPTION_OWNER) && targetWord.startsWith(PREFIX_NRIC.toString())) {
            return true;
        }
        return false;
    }

    /**
     *Returns true if command input {@code trimmedCommandInput} is the syntax for adding a new appointment.
     */
    private boolean hasReferenceToExistingPetPatientNames() {
        final Pattern addNewAppointment = Pattern.compile(AddCommand.COMMAND_WORD + " -(a)+(?<apptInfo>.*)"
                + "-(o)+(?<ownerNric>.*)" + "-(p)+(?<petName>.*)");
        final Matcher matcherForNewAppt = addNewAppointment.matcher(trimmedCommandInput);

        if (matcherForNewAppt.matches()) {
            return true;
        }

        return false;
    }

    /**
     * Sets {@code option} based on the last option found in {@code trimmedCommandInput}.
     */
    private void setOption() {
        option = "nil";

        int o = trimmedCommandInput.lastIndexOf(OPTION_OWNER);
        int p = trimmedCommandInput.lastIndexOf(OPTION_PETPATIENT);
        int a = trimmedCommandInput.lastIndexOf(OPTION_APPOINTMENT);

        int index = (a > p) ? a : p;
        index = (index > o) ? index : o;

        if (index > -1 && (trimmedCommandInput.length() >= index + 2)) {
            option = trimmedCommandInput.substring(index, index + 2); // (inclusive, exclusive)
        }
    }

    /**
     * Returns a string that contains the parameter part of {@code targetWord}.
     */
    private String getParameter() {
        String[] splitByPrefix = targetWord.split("/");
        String parameter = splitByPrefix[1];
        return parameter;
    }

    /**
     * Returns a sorted list of suggestions for tags.
     * List size conforms to max size {@code MAX_SUGGESTION_COUNT}.
     */
    private List<String> getTagSuggestions() {
        setTagListBasedOnOption();
        if (targetWord.equals(PREFIX_TAG.toString())) {
            List<String> suggestions = tagSet
                    .stream()
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;

        } else {
            String targetTag = getParameter();
            List<String> suggestions = tagSet
                    .stream()
                    .filter(t -> t.toLowerCase().startsWith(targetTag) && !t.toLowerCase().equals(targetTag))
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;
        }
    }

    /**
     * Sets {@code tagList} based on {@code option}.
     * -o option will set elements of {@code tagList} to be persons' tags.
     * -p option will set elements of {@code tagList} to be pet patients' tags.
     * -a option will set elements of {@code tagList} to be appointments' tags.
     */
    private void setTagListBasedOnOption() {
        switch(option) {

        case OPTION_OWNER:
            tagSet = logic.getAllPersonTags();
            break;

        case OPTION_PETPATIENT:
            tagSet = logic.getAllPetPatientTags();
            break;

        case OPTION_APPOINTMENT:
            tagSet = logic.getAllAppointmentTags();
            break;

        default:
            tagSet = logic.getAllTagsInModel();
        }
    }

    /**
     * Returns a sorted list of suggestions for pet patient names.
     * List size conforms to max size {@code MAX_SUGGESTION_COUNT}.
     */
    private List<String> getPetPatientNameSuggestions() {
        if (targetWord.equals(PREFIX_NAME.toString())) {
            List<String> suggestions = logic.getAllPetPatientNames()
                    .stream()
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;

        } else {
            String targetPetName = getParameter();
            List<String> suggestions = logic.getAllPetPatientNames()
                    .stream()
                    .filter(pn -> pn.toLowerCase().startsWith(targetPetName) && !pn.toLowerCase().equals(targetPetName))
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;
        }
    }

    /**
     * Returns a sorted list of suggestions for pet patient species.
     */
    private List<String> getPetPatientSpeciesSuggestions() {
        if (targetWord.equals(PREFIX_SPECIES.toString())) {
            List<String> suggestions = logic.getAllPetPatientSpecies()
                    .stream()
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;

        } else {
            String targetSpecies = getParameter();
            List<String> suggestions = logic.getAllPetPatientSpecies()
                    .stream()
                    .filter(s -> s.toLowerCase().startsWith(targetSpecies) && !s.toLowerCase().equals(targetSpecies))
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;
        }
    }

    /**
     * Returns a sorted list of suggestions for pet patient breeds.
     * List size conforms to max size {@code MAX_SUGGESTION_COUNT}.
     */
    private List<String> getPetPatientBreedSuggestions() {
        if (targetWord.equals(PREFIX_BREED.toString())) {
            List<String> suggestions = logic.getAllPetPatientBreeds()
                    .stream()
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;

        } else {
            String targetBreed = getParameter();
            List<String> suggestions = logic.getAllPetPatientBreeds()
                    .stream()
                    .filter(b -> b.toLowerCase().startsWith(targetBreed) && !b.toLowerCase().equals(targetBreed))
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;
        }
    }

    /**
     * Returns a sorted list of suggestions for pet patient colours.
     * List size conforms to max size {@code MAX_SUGGESTION_COUNT}.
     */
    private List<String> getPetPatientColourSuggestions() {
        if (targetWord.equals(PREFIX_COLOUR.toString())) {
            List<String> suggestions = logic.getAllPetPatientColours()
                    .stream()
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;

        } else {
            String targetPetColour = getParameter();
            List<String> suggestions = logic.getAllPetPatientColours()
                    .stream()
                    .filter(c -> c.toLowerCase().startsWith(targetPetColour)
                            && !c.toLowerCase().equals(targetPetColour))
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;
        }
    }

    /**
     * Returns a sorted list of suggestions for pet patient blood types.
     * List size conforms to max size {@code MAX_SUGGESTION_COUNT}.
     */
    private List<String> getPetPatientBloodTypeSuggestions() {
        if (targetWord.equals(PREFIX_BLOODTYPE.toString())) {
            List<String> suggestions = logic.getAllPetPatientBloodTypes()
                    .stream()
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;

        } else {
            String targetPetBloodType = getParameter();
            List<String> suggestions = logic.getAllPetPatientBloodTypes()
                    .stream()
                    .filter(bt -> bt.toLowerCase().startsWith(targetPetBloodType)
                            && !bt.toLowerCase().equals(targetPetBloodType))
                    .sorted(String::compareToIgnoreCase)
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;
        }
    }

    /**
     * Returns a sorted list of suggestions for Nric.
     * List size conforms to max size {@code MAX_SUGGESTION_COUNT}.
     */
    private List<String> getNricSuggestions() {
        if (targetWord.equals(PREFIX_NRIC.toString())) {
            List<String> suggestions = logic.getAllNric()
                    .stream()
                    .sorted()
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;

        } else {
            String targetNric = getParameter().toUpperCase();
            List<String> suggestions = logic.getAllNric()
                    .stream()
                    .filter(n -> n.startsWith(targetNric) && !n.equals(targetNric))
                    .sorted()
                    .limit(MAX_SUGGESTION_COUNT)
                    .collect(Collectors.toList());
            return suggestions;
        }
    }

    /**
     * Returns a sorted list of suggestions for prefixes.
     */
    private List<String> getPrefixSuggestions() {
        List<String> suggestions = logic.getAllPrefixes()
                .stream()
                .filter(p -> p.startsWith(targetWord) && !(StringUtil.removeDescription(p).equals(targetWord)))
                .sorted()
                .collect(Collectors.toList());
        return suggestions;
    }

    /**
     * Returns a sorted list of suggestions for options.
     */
    private List<String> getOptionSuggestions() {
        List<String> suggestions = logic.getAllOptions()
                .stream()
                .filter(o -> o.startsWith(targetWord) && !(StringUtil.removeDescription(o).equals(targetWord)))
                .sorted()
                .collect(Collectors.toList());
        return suggestions;
    }

    /**
     * Returns a sorted list of suggestions for command words.
     */
    private List<String> getCommandWordSuggestions() {
        List<String> suggestions = logic.getAllCommandWords()
                .stream()
                .filter(c -> c.startsWith(targetWord) && !c.equals(targetWord))
                .sorted()
                .collect(Collectors.toList());
        return suggestions;
    }

    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent a) {
        init(this.logic);
        logger.info(LogsCenter.getEventHandlingLogMessage(a, "Local data has changed,"
                + " update autocomplete data"));
    }

}
```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Toggles autocomplete on or off.
     */
    private void toggleAutocomplete() {
        if (isAutocompleting) {
            commandTextField.textProperty().removeListener(getAutocompleteListener());
            isAutocompleting = false;
            hideSuggestionBox();
            logger.info("Autocomplete has been toggled [OFF]");
        } else {
            commandTextField.textProperty().addListener(getAutocompleteListener());
            isAutocompleting = true;
            logger.info("Autocomplete has been toggled [ON]");
        }
    }

    private void hideSuggestionBox() {
        if (suggestionBox.isShowing()) {
            suggestionBox.hide();
        }
    }

    /**
     * Calls Autocomplete class to process commandTextField's content.
     *
     * @param newValue New user input.
     */
    private void triggerAutocomplete(String newValue) {
        suggestionBox.getItems().clear();

        if (!newValue.equals("")) {

            List<String> suggestions = autocompleteLogic.getSuggestions(commandTextField);

            if (!suggestions.isEmpty()) {
                setContextMenu(suggestions);
            }
        }
    }

    /**
     * Sets the context menu {@code suggestionBox} with autocomplete suggestions.
     */
    private void setContextMenu(List<String> suggestions) {
        for (String s : suggestions) {
            MenuItem m = new MenuItem(s);
            String autocompleteValue = StringUtil.removeDescription(s);
            m.setOnAction(event -> handleAutocompleteSelection(autocompleteValue));
            suggestionBox.getItems().add(m);
        }
        suggestionBox.show(commandTextField, Side.BOTTOM, 0, 0);
    }

    /**
     * Updates text in commandTextField with autocomplete selection {@code toAdd}.
     *
     * Supports insertion of autocomplete selection in the middle of commandTextField.
     * user input: 'a', selected autocomplete 'add' --> commandTextField will show 'add' and not 'aadd'.
     * user input: 'nr/F012', selected autocomplete 'F0123456B' --> commandTextField will show 'nr/F0123456B'
     * and not 'nr/F012F0123456B'.
     */
    private void handleAutocompleteSelection(String toAdd) {
        int cursorPosition = commandTextField.getCaretPosition();
        int userInputLength = commandTextField.getText().length();

        // .split() retains all whitespaces in array.
        String[] words = commandTextField.getText(0, cursorPosition).split("((?<= )|(?= ))", -1);
        String targetWord = words[words.length - 1];
        String restOfInput = getRemainingInput(cursorPosition, userInputLength);

        if (containsPrefix(targetWord)) {
            String[] splitByPrefix = targetWord.split("/");
            words[words.length - 1] = splitByPrefix[0] + "/" + toAdd;
        } else {
            words[words.length - 1] = toAdd;
        }

        String updatedInput = String.join("", words);
        int newCursorPosition = updatedInput.length();
        commandTextField.setText(updatedInput + restOfInput);
        commandTextField.positionCaret(newCursorPosition);
    }

    /**
     * Returns remaining text in {@code commandTextField} after {@code cursorPosition}, if any.
     */
    private String getRemainingInput(int cursorPosition, int userInputLength) {
        String restOfInput = "";
        if (userInputLength > cursorPosition + 1) {
            restOfInput = commandTextField.getText(cursorPosition, commandTextField.getText().length());
        }
        return restOfInput;
    }

    /**
     * Returns true if {@code toCheck} contains a prefix or is a prefix.
     */
    private boolean containsPrefix(String toCheck) {
        if (toCheck.contains("/")) {
            return true;
        } else if (toCheck.length() > 0 && toCheck.substring(toCheck.length() - 1).equals("/")) {
            return true;
        } else {
            return false;
        }
    }

    public static ChangeListener getAutocompleteListener() {
        return autocompleteListener;
    }
}
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Sets the default theme based on user preferences.
     */
    private void setWindowDefaultTheme(UserPrefs prefs) {
        getRoot().getScene().getStylesheets().add(prefs.getGuiSettings().getCurrentTheme());
    }

    /**
     * Returns the current size, position, and theme of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        ObservableList<String> cssFiles = getRoot().getScene().getStylesheets();
        assert cssFiles.size() == 2 : "There should only be 2 stylesheets used in main Window.";

        String theme = cssFiles.stream().filter(c -> !c.contains("/view/Extensions.css")).findFirst().get();
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY(), theme);
    }

```
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Changes the theme of Medeina.
     */
    @Subscribe
    public void handleChangeThemeEvent(ChangeThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        String userSelectedTheme = event.theme.getThemePath();
        String userSelectedStyleSheet = this.getClass().getResource(userSelectedTheme).toExternalForm();
        if (!hasStyleSheet(userSelectedStyleSheet)) {
            changeStyleSheet(userSelectedStyleSheet);
        }
    }

    /**
     * Checks whether {@code theme} is already in use by the application.
     */
    public Boolean hasStyleSheet(String theme) {
        List<String> styleSheetsInUsed = getRoot().getScene().getStylesheets();
        if (styleSheetsInUsed.contains(theme)) {
            return true;
        }
        return false;
    }

    /**
     * Removes all existing stylesheets and add the given {@code theme} to style sheets.
     * Re-adds Extensions.css to style sheets.
     */
    public void changeStyleSheet(String theme) {
        String extensions = this.getClass().getResource("/view/Extensions.css").toExternalForm();
        getRoot().getScene().getStylesheets().clear();
        getRoot().getScene().getStylesheets().add(extensions); //re-add Extensions.css
        boolean isChanged = getRoot().getScene().getStylesheets().add(theme);
        assert isChanged == true : "Medeina's theme is not successfully changed.";
    }
}
```
