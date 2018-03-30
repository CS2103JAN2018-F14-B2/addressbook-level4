package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.petpatient.PetPatient;

/**
 * AN UI component that displays the information of a {@code PetPatient}
 */
public class PetPatientCard extends UiPart<Region> {
    private static final String FXML = "PetPatientListCard.fxml";

    private static final String[] TAG_COLOR_STYLES =
        {"teal", "red", "yellow", "blue", "orange", "brown", "green", "pink",
            "black", "grey"};

    public final PetPatient petPatient;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label species;
    @FXML
    private Label breed;
    @FXML
    private Label colour;
    @FXML
    private Label bloodType;
    @FXML
    private Label ownerNric;
    @FXML
    private FlowPane tags;

    public PetPatientCard(PetPatient petPatient, int displayedIndex) {
        super(FXML);
        this.petPatient = petPatient;
        id.setText(displayedIndex + ". ");
        name.setText(petPatient.getName().toString());
        species.setText(petPatient.getSpecies());
        breed.setText(petPatient.getBreed());
        colour.setText(petPatient.getColour());
        bloodType.setText(petPatient.getBloodType());
        ownerNric.setText(petPatient.getOwner().toString());
        createTags(petPatient);
    }

    /**
     * Returns the color style for {@code tagName}'s label.
     * Solution below adopted from :
     * https://github.com/se-edu/addressbook-level4/pull/798/commits/167b3d0b4f7ad34296d2fbf505f9ae71f983f53c
     */
    private String getTagColorStyleFor(String tagName) {
        // we use the hash code of the tag name to generate a random color, so that the color remain consistent
        // between different runs of the program while still making it random enough between tags.
        return TAG_COLOR_STYLES[Math.abs(tagName.hashCode()) % TAG_COLOR_STYLES.length];
    }

    /**
     * Creates the tag labels for {@code PetPatient}.
     */
    private void createTags(PetPatient petPatient) {
        petPatient.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyleFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PetPatientCard)) {
            return false;
        }

        // state check
        PetPatientCard card = (PetPatientCard) other;
        return id.getText().equals(card.id.getText())
            && petPatient.equals(card.petPatient);
    }
}
