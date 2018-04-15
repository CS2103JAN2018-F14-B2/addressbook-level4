# Robert-Peng
###### \java\guitests\guihandles\CalendarPanelHandle.java
``` java
/**
 *
 */
public class CalendarPanelHandle extends NodeHandle<Node> {

    public static final String CALENDARPANEL_ID = "#calendarPlaceholder";

    protected CalendarPanelHandle(Node calendarPanelNode) {
        super(calendarPanelNode);
    }
}
```
###### \java\guitests\guihandles\MainWindowHandle.java
``` java
    public CalendarPanelHandle getCalendarPanel() {
        return calendarPanel;
    }


    public PetPatientListPanelHandle getPetPatientListPanel() {
        return petPatientListPanel;
    }
```
###### \java\guitests\guihandles\PetPatientCardHandle.java
``` java
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
```
###### \java\guitests\guihandles\PetPatientListPanelHandle.java
``` java
/**
 * Provides a handle for {@code PetPatientListPanel} containing the list of {@code PetPatientCard}.
 */
public class PetPatientListPanelHandle extends NodeHandle<ListView<PetPatientCard>> {
    public static final String PETPATIENT_LIST_VIEW_ID = "#petPatientListView";

    private Optional<PetPatientCard> lastRememberedSelectedPetPatientCard;

    public PetPatientListPanelHandle(ListView<PetPatientCard> petPatientListPanelNode) {
        super(petPatientListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code PetPatientCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public PetPatientCardHandle getHandleToSelectedCard() {
        List<PetPatientCard> petPatientList = getRootNode().getSelectionModel().getSelectedItems();

        if (petPatientList.size() != 1) {
            throw new AssertionError("PetPatient list size expected 1.");
        }

        return new PetPatientCardHandle(petPatientList.get(0).getRoot());
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<PetPatientCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and select the petPatient.
     */
    public void navigateToCard(PetPatient petPatient) {
        List<PetPatientCard> cards = getRootNode().getItems();
        Optional<PetPatientCard> matchingCard = cards.stream().filter(
            card -> card.petPatient.equals(petPatient)).findFirst();

        if (!matchingCard.isPresent()) {
            throw new IllegalArgumentException("PetPatient does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingCard.get());
            getRootNode().getSelectionModel().select(matchingCard.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the petPatient card handle of a petPatient associated with the {@code index} in the list.
     */
    public PetPatientCardHandle getPetPatientCardHandle(int index) {
        return getPetPatientCardHandle(getRootNode().getItems().get(index).petPatient);
    }

    /**
     * Returns the {@code PetPatientCardHandle} of the specified {@code petPatient} in the list.
     */
    public PetPatientCardHandle getPetPatientCardHandle(PetPatient petPatient) {
        Optional<PetPatientCardHandle> handle = getRootNode().getItems().stream()
            .filter(card -> card.petPatient.equals(petPatient))
            .map(card -> new PetPatientCardHandle(card.getRoot()))
            .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("PetPatient does not exist."));
    }

    /**
     * Selects the {@code PetPatientCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code PetPatientCard} in the list.
     */
    public void rememberSelectedPetPatientCard() {
        List<PetPatientCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedPetPatientCard = Optional.empty();
        } else {
            lastRememberedSelectedPetPatientCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code PetPatientCard} is different from the value remembered by the most recent
     * {@code rememberSelectedPetPatientCard()} call.
     */
    public boolean isSelectedPetPatientCardChanged() {
        List<PetPatientCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedPetPatientCard.isPresent();
        } else {
            return !lastRememberedSelectedPetPatientCard.isPresent()
                || !lastRememberedSelectedPetPatientCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }

}
```
###### \java\seedu\address\ui\PetPatientCardTest.java
``` java
/**
 * Test class for PetPatientCard
 */
public class PetPatientCardTest extends GuiUnitTest {

    private PetPatient petPatient;
    private PetPatientCard petPatientCard;

    @Before
    public void setUp() throws Exception {
        petPatient = new PetPatientBuilder().build();
        petPatientCard = new PetPatientCard(petPatient, 1);
    }

    @Test
    public void display_checkDetails_displayedCorrectly() {
        uiPartRule.setUiPart(petPatientCard);
        assertCardDisplay(petPatientCard, petPatient, 1);
    }

    @Test
    public void equals_sameNameSameIndex_returnTrue() {
        PetPatientCard copy = new PetPatientCard(petPatient, 1);
        assertTrue(petPatientCard.equals(copy));
    }

    @Test
    public void equals_checkNull_returnFalse() {
        assertFalse(petPatientCard.equals(null));
    }

    @Test
    public void equals_samePetPatientCard_returnTrue() {
        assertTrue(petPatientCard.equals(petPatientCard));
    }

    @Test
    public void equals_differentPetPatientSameIndex_returnFalse() {
        PetPatient differentPetPatient = new PetPatientBuilder().withName("differentName").build();
        assertFalse(petPatientCard.equals((new PetPatientCard(differentPetPatient, 1))));
    }

    @Test
    public void equals_samePetPatientDifferentIndex_returnFalse() {
        assertFalse(petPatientCard.equals(new PetPatientCard(petPatient, 2)));
    }

    /**
      * Asserts that {@code petpatientCard} displays the details of {@code expectedpetPatient} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PetPatientCard petPatientCard, PetPatient expectedPetPatient, int expectedId) {
        guiRobot.pauseForHuman();

        PetPatientCardHandle petPatientCardHandle = new PetPatientCardHandle(petPatientCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", petPatientCardHandle.getId());

        // verify petpatient details are displayed correctly
        assertCardDisplaysPetPatient(expectedPetPatient, petPatientCardHandle);
    }

}
```
###### \java\seedu\address\ui\PetPatientListPanelTest.java
``` java
/**
 * Test class for PetPatientListPanel
 */
public class PetPatientListPanelTest extends GuiUnitTest {
    private static final ObservableList<PetPatient> TYPICAL_PETPATIENTS =
                    FXCollections.observableList(getTypicalPetPatients());

    private PetPatientListPanelHandle petPatientListPanelHandle;

    @Before
    public void setUp() {
        PetPatientListPanel petPatientListPanel = new PetPatientListPanel(TYPICAL_PETPATIENTS);
        uiPartRule.setUiPart(petPatientListPanel);

        petPatientListPanelHandle = new PetPatientListPanelHandle(getChildNode(petPatientListPanel.getRoot(),
            PetPatientListPanelHandle.PETPATIENT_LIST_VIEW_ID));
    }

    @Test
    public void display_cardMatches_returnTrue() {
        for (int i = 0; i < TYPICAL_PETPATIENTS.size(); i++) {
            petPatientListPanelHandle.navigateToCard(TYPICAL_PETPATIENTS.get(i));
            PetPatient expectedPetPatient = TYPICAL_PETPATIENTS.get(i);
            PetPatientCardHandle actualCard = petPatientListPanelHandle.getPetPatientCardHandle(i);

            assertCardDisplaysPetPatient(expectedPetPatient, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

}
```
###### \java\seedu\address\ui\testutil\GuiTestAssert.java
``` java
    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedPetPatient}.
     */
    public static void assertCardDisplaysPetPatient(PetPatient expectedPetPatient, PetPatientCardHandle actualCard) {
        assertEquals(expectedPetPatient.getName().toString(), actualCard.getName());
        assertEquals("Species:\t\t" + expectedPetPatient.getSpecies().toString(), actualCard.getSpecies());
        assertEquals("Breed:\t\t" + expectedPetPatient.getBreed().toString(), actualCard.getBreed());
        assertEquals("Colour:\t\t" + expectedPetPatient.getColour().toString(), actualCard.getColour());
        assertEquals("Blood Type:\t" + expectedPetPatient.getBloodType().toString(),
                actualCard.getBloodType());
        assertEquals("Owner NRIC:\t" + expectedPetPatient.getOwner().toString(), actualCard.getOwnerNric());

        //assertTagsEqual(expectedPetPatient, actualCard);
    }
```
###### \java\seedu\address\ui\testutil\GuiTestAssert.java
``` java
    /**
     * Asserts that the tags in {@code actualCard} matches all the tags in {@code expectedPerson} with the correct
     * color.
     */
    private static void assertTagsEqual(PetPatient expectedPetPatient, PetPatientCardHandle actualCard) {
        List<String> expectedTags = expectedPetPatient.getTags().stream()
            .map(tag -> tag.tagName).collect(Collectors.toList());
        assertEquals(expectedTags, actualCard.getTags());
        expectedTags.forEach(tag ->
            assertEquals(Arrays.asList(LABEL_DEFAULT_STYLE, getTagColorStyleFor(tag)),
                actualCard.getTagStyleClasses(tag)));
    }
```
###### \java\seedu\address\ui\testutil\GuiTestAssert.java
``` java
    /**
     * Asserts that the list in {@code petPatientListPanelHandle} displays the details of {@code petPatient} correctly
     * and in the correct order.
     */
    public static void assertListMatching(PetPatientListPanelHandle petPatientListPanelHandle,
                                          PetPatient... petPatient) {
        for (int i = 0; i < petPatient.length; i++) {
            assertCardDisplaysPetPatient(petPatient[i], petPatientListPanelHandle.getPetPatientCardHandle(i));
        }
    }
```
###### \java\systemtests\AddCommandSystemTest.java
``` java
        /* Case: missing nric -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_OWNER + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_PARAMETER_FORMAT, AddCommand.MESSAGE_ERROR_PERSON));
```
###### \java\systemtests\AddCommandSystemTest.java
``` java
        /* Case: invalid nric -> rejected */
        command = AddCommand.COMMAND_WORD + " " + OPTION_OWNER + NAME_DESC_AMY + PHONE_DESC_AMY
            + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + INVALID_NRIC_DESC;
        assertCommandFailure(command, Nric.MESSAGE_NRIC_CONSTRAINTS);
```
###### \java\systemtests\AddressBookSystemTest.java
``` java
    public PetPatientListPanelHandle getPetPatientListPanel() {
        return mainWindowHandle.getPetPatientListPanel();
    }
```
###### \java\systemtests\AddressBookSystemTest.java
``` java
    public CalendarPanelHandle getCalendarPanel() {
        return mainWindowHandle.getCalendarPanel();
    }
```
###### \java\systemtests\EditCommandSystemTest.java
``` java
        /* Case: invalid nric -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + OPTION_OWNER + " "
                        + INDEX_FIRST_PERSON.getOneBased() + INVALID_NRIC_DESC,
                Nric.MESSAGE_NRIC_CONSTRAINTS);
```
