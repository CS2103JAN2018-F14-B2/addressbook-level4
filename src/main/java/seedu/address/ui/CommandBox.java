package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.logic.ListElementPointer;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static ChangeListener<String> autocompleteListener;

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;


    @FXML
    private TextField commandTextField;
    private ContextMenu suggestionBox;
    private Autocomplete autocompleteLogic = Autocomplete.getInstance();

    private List<String> suggestions;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        suggestionBox = new ContextMenu();
        commandTextField.setContextMenu(suggestionBox);
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        // autocomplete
        autocompleteListener = ((observable, oldValue, newValue) -> triggerAutocomplete(newValue));
        commandTextField.textProperty().addListener(autocompleteListener);

        historySnapshot = logic.getHistorySnapshot();
    }

    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();

            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        default:
            if (suggestionBox.isShowing()) {
                suggestionBox.hide();
            }
            // let JavaFx handle the keypress
        }
    }

    /**
     * Updates the text field with the previous input in {@code historySnapshot},
     * if there exists a previous input in {@code historySnapshot}
     */
    private void navigateToPreviousInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasPrevious()) {
            return;
        }

        replaceText(historySnapshot.previous());
    }

    /**
     * Updates the text field with the next input in {@code historySnapshot},
     * if there exists a next input in {@code historySnapshot}
     */
    private void navigateToNextInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasNext()) {
            return;
        }

        replaceText(historySnapshot.next());
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the end of the {@code text}.
     */
    private void replaceText(String text) {
        commandTextField.setText(text);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());
            initHistory();
            historySnapshot.next();
            // process result of the command
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException | ParseException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Retrieve a list of autocomplete suggestions and set the pop-up ContextMenu.
     */
    private void triggerAutocomplete(String newValue) {
        suggestionBox.getItems().clear();

        if (!newValue.equals("")) {

            suggestions = autocompleteLogic.getSuggestions(logic, commandTextField);

            for (String s : suggestions) {
                MenuItem m = new MenuItem(s);
                m.setOnAction(event -> handleAutocompleteSelection(m.getText()));
                suggestionBox.getItems().add(m);
            }

            suggestionBox.show(commandTextField, Side.BOTTOM, 0, 0);
        }
    }

    /**
     * Append autocomplete selection to commandTextField (do not append repeated characters).
     * user input: 'a', selected autocomplete 'add' --> commandTextField will show 'add' and not 'aadd'.
     * user input: 'nr/F012', selected autocomplete 'F0123456B' --> commandTextField will show 'nr/F0123456B'
     * and not 'nr/F012F0123456B'.
     */
    private void handleAutocompleteSelection(String toAdd) {
        String[] words = commandTextField.getText().trim().split(" ");
        String targetWord = words[words.length - 1];

        if (containsPrefix(targetWord)) {
            String[] splitByPrefix = words[words.length - 1].split("/");
            words[words.length - 1] = splitByPrefix[0] + "/" + toAdd;
            //toAdd = toAdd.replaceFirst("(?i)" + splitByPrefix[1], "");
        } else {
            toAdd = toAdd.replaceFirst(words[words.length - 1], "");
            words[words.length - 1] += toAdd;
        }

        //int cursorPos = commandTextField.getCaretPosition();
        //commandTextField.insertText(cursorPos, toAdd);
        String updateInput = String.join(" ", words);
        commandTextField.setText(updateInput);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Returns true if {@code toCheck} contains a prefix, but is not a prefix.
     */
    private boolean containsPrefix(String toCheck) {
        if (toCheck.contains("/") && !toCheck.substring(toCheck.length() - 1).equals("/")) {
            return true;
        } else {
            return false;
        }
    }

    public static ChangeListener getAutocompleteListener() {
        return autocompleteListener;
    }
}
