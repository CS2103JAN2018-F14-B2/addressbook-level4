package seedu.address.ui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.CommandBoxHandle;
import javafx.scene.input.KeyCode;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.ListCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class CommandBoxTest extends GuiUnitTest {

    private static final String COMMAND_THAT_SUCCEEDS = ListCommand.COMMAND_WORD;
    private static final String COMMAND_THAT_FAILS = "invalid command";

    private ArrayList<String> defaultStyleOfCommandBox;
    private ArrayList<String> errorStyleOfCommandBox;

    private CommandBoxHandle commandBoxHandle;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        Logic logic = new LogicManager(model);

        CommandBox commandBox = new CommandBox(logic);
        commandBoxHandle = new CommandBoxHandle(getChildNode(commandBox.getRoot(),
                CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        uiPartRule.setUiPart(commandBox);

        defaultStyleOfCommandBox = new ArrayList<>(commandBoxHandle.getStyleClass());

        errorStyleOfCommandBox = new ArrayList<>(defaultStyleOfCommandBox);
        errorStyleOfCommandBox.add(CommandBox.ERROR_STYLE_CLASS);
    }

    @Test
    public void commandBox_startingWithSuccessfulCommand() {
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
    }

    @Test
    public void commandBox_startingWithFailedCommand() {
        assertBehaviorForFailedCommand();
        assertBehaviorForSuccessfulCommand();

        // verify that style is changed correctly even after multiple consecutive failed commands
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
        assertBehaviorForFailedCommand();
    }

    @Test
    public void commandBox_handleKeyPress() {
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());
        guiRobot.push(KeyCode.ESCAPE);
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());

        guiRobot.push(KeyCode.A);
        assertEquals(defaultStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    @Test
    public void handleKeyPress_startingWithUp() {
        // empty history
        assertInputHistory(KeyCode.UP, "");
        assertInputHistory(KeyCode.DOWN, "");

        // one command
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, "");

        // two commands (latest command is failure)
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, COMMAND_THAT_FAILS);

        // insert command in the middle of retrieving previous commands
        guiRobot.push(KeyCode.UP);
        String thirdCommand = "list";
        commandBoxHandle.run(thirdCommand);
        assertInputHistory(KeyCode.UP, thirdCommand);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.DOWN, thirdCommand);
        assertInputHistory(KeyCode.DOWN, "");
    }

    @Test
    public void handleKeyPress_startingWithDown() {
        // empty history
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, "");

        // one command
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, COMMAND_THAT_SUCCEEDS);

        // two commands
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, COMMAND_THAT_FAILS);

        // insert command in the middle of retrieving previous commands
        guiRobot.push(KeyCode.UP);
        String thirdCommand = "list";
        commandBoxHandle.run(thirdCommand);
        assertInputHistory(KeyCode.DOWN, "");
        assertInputHistory(KeyCode.UP, thirdCommand);
    }

    /**
     * Runs a command that fails, then verifies that <br>
     *      - the text remains <br>
     *      - the command box's style is the same as {@code errorStyleOfCommandBox}.
     */
    private void assertBehaviorForFailedCommand() {
        commandBoxHandle.run(COMMAND_THAT_FAILS);
        assertEquals(COMMAND_THAT_FAILS, commandBoxHandle.getInput());
        assertEquals(errorStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    /**
     * Runs a command that succeeds, then verifies that <br>
     *      - the text is cleared <br>
     *      - the command box's style is the same as {@code defaultStyleOfCommandBox}.
     */
    private void assertBehaviorForSuccessfulCommand() {
        commandBoxHandle.run(COMMAND_THAT_SUCCEEDS);
        assertEquals("", commandBoxHandle.getInput());
        assertEquals(defaultStyleOfCommandBox, commandBoxHandle.getStyleClass());
    }

    /**
     * Pushes {@code keycode} and checks that the input in the {@code commandBox} equals to {@code expectedCommand}.
     */
    private void assertInputHistory(KeyCode keycode, String expectedCommand) {
        guiRobot.push(keycode);
        assertEquals(expectedCommand, commandBoxHandle.getInput());
    }

    //@@author aquarinte
    @Test
    public void commandBox_autocompleteCommandWord() {
        //add command
        commandBoxHandle.setText("a");
        selectFromContextMenu();
        assertEquals("add", commandBoxHandle.getInput());

        commandBoxHandle.setText(" ");
        selectFromContextMenu();
        assertEquals(" add", commandBoxHandle.getInput());

        //clear command
        commandBoxHandle.setText("cl");
        selectFromContextMenu();
        assertEquals("clear", commandBoxHandle.getInput());

        //delete command
        commandBoxHandle.setText("d");
        selectFromContextMenu();
        assertEquals("delete", commandBoxHandle.getInput());

        //edit command
        commandBoxHandle.setText("ed");
        selectFromContextMenu();
        assertEquals("edit", commandBoxHandle.getInput());

        //exit command
        commandBoxHandle.setText("ex");
        selectFromContextMenu();
        assertEquals("exit", commandBoxHandle.getInput());

        //help command
        commandBoxHandle.setText("h");
        selectFromContextMenu();
        assertEquals("help", commandBoxHandle.getInput());

        commandBoxHandle.setText("he");
        selectFromContextMenu();
        assertEquals("help", commandBoxHandle.getInput());

        commandBoxHandle.setText("hel");
        selectFromContextMenu();
        assertEquals("help", commandBoxHandle.getInput());

        //history command
        commandBoxHandle.setText("h");
        guiRobot.push(KeyCode.TAB);
        selectFromContextMenu();
        assertEquals("history", commandBoxHandle.getInput());

        commandBoxHandle.setText("hi");
        selectFromContextMenu();
        assertEquals("history", commandBoxHandle.getInput());

        commandBoxHandle.setText("hist");
        selectFromContextMenu();
        assertEquals("history", commandBoxHandle.getInput());

        //list command
        commandBoxHandle.setText("l");
        selectFromContextMenu();
        assertEquals("list", commandBoxHandle.getInput());

        //theme command
        commandBoxHandle.setText("t");
        selectFromContextMenu();
        assertEquals("theme", commandBoxHandle.getInput());

        //undo
        commandBoxHandle.setText("u");
        selectFromContextMenu();
        assertEquals("undo", commandBoxHandle.getInput());

        //redo
        commandBoxHandle.setText("r");
        selectFromContextMenu();
        assertEquals("redo", commandBoxHandle.getInput());
    }

    @Test
    public void commandBox_autocompleteOption() {
        //add command with -o
        commandBoxHandle.setText("add");
        commandBoxHandle.insertText(" -");
        selectFromContextMenu();
        assertEquals("add -o", commandBoxHandle.getInput());
    }

    @Test
    public void commandBox_autocompletePrefix() {
        //prefix n/
        commandBoxHandle.setText("add");
        commandBoxHandle.insertText(" n");
        selectFromContextMenu();
        assertEquals("add n/", commandBoxHandle.getInput());

        //prefix nr/
        commandBoxHandle.setText("add");
        commandBoxHandle.insertText(" n");
        guiRobot.push(KeyCode.TAB);
        selectFromContextMenu();
        assertEquals("add nr/", commandBoxHandle.getInput());

        commandBoxHandle.setText("add");
        commandBoxHandle.insertText(" nr");
        selectFromContextMenu();
        assertEquals("add nr/", commandBoxHandle.getInput());
    }

    private void selectFromContextMenu() {
        guiRobot.push(KeyCode.TAB);
        guiRobot.push(KeyCode.ENTER);
    }
}
