package seedu.address.logic;

import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.petpatient.PetPatient;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered list of petPatients */
    ObservableList<PetPatient> getFilteredPetPatientList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();

    /** Returns a set of all command words */
    Set<String> getAllCommandWords();

    /** Returns a set of all prefixes */
    Set<Prefix> getAllPrefixes();

    /** Returns a set of all options used in command syntax */
    Set<String> getAllOptions();

    /** Returns a set of all Nric found in model*/
    Set<String> getAllNric();

    /** Returns a set of all pet patient names found in model*/
    Set<String> getAllPetPatientNames();
}
