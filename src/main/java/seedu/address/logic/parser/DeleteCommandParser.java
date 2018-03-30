package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

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
