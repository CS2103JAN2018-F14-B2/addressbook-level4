package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;
import seedu.address.model.petpatient.PetPatient;
import seedu.address.ui.PetPatientCard;


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
