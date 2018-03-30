package seedu.address.logic;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.petpatient.PetPatient;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;
    private final UndoRedoStack undoRedoStack;

    private CommandSyntaxWords commandSyntax;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        addressBookParser = new AddressBookParser();
        undoRedoStack = new UndoRedoStack();
        commandSyntax = CommandSyntaxWords.getInstance();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = addressBookParser.parseCommand(commandText);
            command.setData(model, history, undoRedoStack);
            CommandResult result = command.execute();
            undoRedoStack.push(command);
            return result;
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<PetPatient> getFilteredPetPatientList() {
        return model.getFilteredPetPatientList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

    //@@author aquarinte
    @Override
    public Set<String> getAllCommandWords() {
        return commandSyntax.getCommandWords();
    }

    @Override
    public Set<Prefix> getAllPrefixes() {
        return commandSyntax.getPrefixes();
    }

    @Override
    public Set<String> getAllOptions() {
        return commandSyntax.getOptions();
    }

    @Override
    public Set<String> getAllNric() {
        Set<String> allNricInModel = new HashSet<>();
        for (Person p : model.getAddressBook().getPersonList()) {
            allNricInModel.add(p.getNric().toString());
        }
        return allNricInModel;
    }

    @Override
    public Set<String> getAllPetPatientNames() {
        Set<String> allPetPatientNamesInModel = new HashSet<>();
        for (PetPatient p : model.getAddressBook().getPetPatientList()) {
            allPetPatientNamesInModel.add(p.getName().toString());
        }
        return allPetPatientNamesInModel;
    }
}
