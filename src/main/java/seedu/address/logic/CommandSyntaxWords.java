package seedu.address.logic;

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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ChangeThemeCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.Prefix;

//@@author aquarinte
/**
 * Stores all command syntax used in Medeina: command words, prefixes and options.
 */
public class CommandSyntaxWords {

    private static CommandSyntaxWords instance;

    private static final Set<String> commandWords = Stream.of(
            AddCommand.COMMAND_WORD, EditCommand.COMMAND_WORD, DeleteCommand.COMMAND_WORD, ListCommand.COMMAND_WORD,
            FindCommand.COMMAND_WORD, ChangeThemeCommand.COMMAND_WORD, ClearCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD, ExitCommand.COMMAND_WORD, RedoCommand.COMMAND_WORD, UndoCommand.COMMAND_WORD,
            HistoryCommand.COMMAND_WORD).collect(Collectors.toSet());

    private static final Set<Prefix> prefixes = Stream.of(
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_NRIC, PREFIX_BREED, PREFIX_SPECIES,
            PREFIX_COLOUR, PREFIX_BLOODTYPE, PREFIX_DATE, PREFIX_REMARK, PREFIX_TAG).collect(Collectors.toSet());

    private static final Set<String> options = Stream.of("-o", "-p", "-a").collect(Collectors.toSet());

    public static CommandSyntaxWords getInstance() {
        if (instance == null) {
            instance = new CommandSyntaxWords();
        }
        return instance;
    }

    public Set<String> getCommandWords() {
        return commandWords;
    }

    public Set<Prefix> getPrefixes() {
        return prefixes;
    }

    public Set<String> getOptions() {
        return options;
    }
}
