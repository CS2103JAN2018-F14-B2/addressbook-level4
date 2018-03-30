package seedu.address.ui;

import java.util.HashSet;
import java.util.Set;
//import java.util.regex.Pattern;

import javafx.scene.control.TextField;
import seedu.address.logic.Logic;
import seedu.address.logic.parser.Prefix;

//@@author aquarinte
/**
 * Handles autocompletion of command input such as command word, options and prefixes.
 */
public class Autocomplete {

    /*private static final Pattern ADD_COMMAND1 = Pattern.compile("add -(a)+(?<apptInfo>.*)-(o)(?<ownerNric>.*)"
            + "-(p)+(?<petName>.*)");
    private static final Pattern ADD_COMMAND2 = Pattern.compile("add -(p)+(?<petInfo>.*)-(o)+(?<ownerNric>.*)");
    private static final Pattern ADD_COMMAND3 = Pattern.compile("add -(o)+(?<ownerInfo>.*)-(p)+(?<petInfo>.*)"
            + "-(a)+(?<apptInfo>.*)");
    private static final Pattern ADD_COMMAND4 = Pattern.compile("add -(o)+(?<ownerInfo>.*)");
    */
    private static Autocomplete instance;
    private Set<String> suggestions;

    public static Autocomplete getInstance() {
        if (instance == null) {
            instance = new Autocomplete();
        }
        return instance;
    }

    /**
     * Find suggestions for current user-input in commandTextField.
     */
    public Set<String> getSuggestions(Logic logic, TextField commandTextField) {
        suggestions = new HashSet<>();
        String userInput = commandTextField.getText().trim();
        String[] words = userInput.split(" ");
        //for (String s : words) {
        //System.out.println("Word: " + s);
        //}
        //The string to auto-complete:
        String target = words[words.length - 1];

        if (words.length == 1) {
            suggestCommandWords(logic, words[0]);
        } else {

            if (words[words.length - 2].equals("-o") && target.equals("nr/")) {
                for (String nric : logic.getAllNric()) {
                    suggestions.add(nric);
                }
            }

            if (words[words.length - 2].equals("-p") && target.equals("n/")) {
                for (String petName : logic.getAllPetPatientNames()) {
                    suggestions.add(petName);
                }
            }
            suggestOptions(logic, target);
            suggestPrefixes(logic, target);
        }
        return suggestions;
    }

    /**
     * Find suggestions for prefixes.
     */
    private void suggestPrefixes(Logic logic, String target) {
        for (Prefix p : logic.getAllPrefixes()) {
            if (p.toString().startsWith(target) && !p.toString().equals(target)) {
                suggestions.add(p.toString());
            }
        }
    }

    /**
     * Find suggestions for options.
     */
    private void suggestOptions(Logic logic, String target) {
        for (String option : logic.getAllOptions()) {
            if (option.startsWith(target) && !option.equals(target)) {
                suggestions.add(option);
            }
        }
    }

    /**
     * Find suggestions for command word.
     */
    private void suggestCommandWords(Logic logic, String word) {
        for (String command : logic.getAllCommandWords()) {
            if (command.startsWith(word) && !command.equals(word)) {
                suggestions.add(command);
            }
        }
    }
}
