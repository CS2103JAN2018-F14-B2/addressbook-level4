package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * Provides a handle to a petPatient card in the petPatient list panel
 */

public class PetPatientCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String NAME_FIELD_ID = "#name";
    private static final String SPECIES_FIELD_ID = "#species";
    private static final String BREED_FIELD_ID = "#breed";
    private static final String COLOUR_FIELD_ID = "#colour";
    private static final String BLOODTYPE_FIELD_ID = "#bloodType";
    private static final String OWNERNRIC_FIELD_ID = "#ownerNric";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label nameLabel;
    private final Label speciesLabel;
    private final Label breedLabel;
    private final Label colourLabel;
    private final Label bloodTypeLabel;
    private final Label ownerNricLabel;
    private final List<Label> tagLabels;

    public PetPatientCardHandle(Node cardNode) {
        super(cardNode);

        this.idLabel = getChildNode(ID_FIELD_ID);
        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.speciesLabel = getChildNode(SPECIES_FIELD_ID);
        this.breedLabel = getChildNode(BREED_FIELD_ID);
        this.colourLabel = getChildNode(COLOUR_FIELD_ID);
        this.bloodTypeLabel = getChildNode(BLOODTYPE_FIELD_ID);
        this.ownerNricLabel = getChildNode(OWNERNRIC_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        this.tagLabels = tagsContainer
            .getChildrenUnmodifiable()
            .stream()
            .map(Label.class::cast)
            .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getSpecies() {
        return speciesLabel.getText();
    }

    public String getBreed() {
        return breedLabel.getText();
    }

    public String getColour() {
        return colourLabel.getText();
    }

    public String getBloodType() {
        return  bloodTypeLabel.getText();
    }

    public String getOwnerNric() {
        return ownerNricLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
            .stream()
            .map(Label::getText)
            .collect(Collectors.toList());
    }

    public List<String> getTagStyleClasses(String tag) {
        return tagLabels
            .stream()
            .filter(label -> label.getText().equals(tag))
            .map(Label::getStyleClass)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No such tag."));
    }

}
