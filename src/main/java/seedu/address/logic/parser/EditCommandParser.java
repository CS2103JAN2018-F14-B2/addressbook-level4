package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditAppointmentDescriptor;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditCommand.EditPetPatientDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

//@@author chialejing
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

    /**
     * Parses the given {@code personInfo} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parsePerson(String personInfo) throws ParseException {
        requireNonNull(personInfo);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(personInfo, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                    PREFIX_ADDRESS, PREFIX_NRIC, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editPersonDescriptor::setName);
            ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).ifPresent(editPersonDescriptor::setPhone);
            ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).ifPresent(editPersonDescriptor::setEmail);
            ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).ifPresent(editPersonDescriptor::setAddress);
            ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC)).ifPresent(editPersonDescriptor::setNric);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

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
                ArgumentTokenizer.tokenize(appointmentInfo, PREFIX_DATE, PREFIX_REMARK, PREFIX_NRIC,
                        PREFIX_NAME, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditAppointmentDescriptor editAppointmentDescriptor = new EditAppointmentDescriptor();
        try {
            ParserUtil.parsePetPatientName(argMultimap.getValue(PREFIX_NAME))
                    .ifPresent(editAppointmentDescriptor::setPetPatientName);
            ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_DATE))
                    .ifPresent(editAppointmentDescriptor::setLocalDateTime);
            ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK))
                    .ifPresent(editAppointmentDescriptor::setRemark);
            ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC))
                    .ifPresent(editAppointmentDescriptor::setOwnerNric);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                    .ifPresent(editAppointmentDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editAppointmentDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editAppointmentDescriptor);    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
