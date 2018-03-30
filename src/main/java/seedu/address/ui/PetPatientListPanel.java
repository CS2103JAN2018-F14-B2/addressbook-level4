package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.PetPatientPanelSelectionChangedEvent;
import seedu.address.model.petpatient.PetPatient;

/**
 * Panel containing list of PetPatients
 */
public class PetPatientListPanel extends UiPart<Region> {
    private static final String FXML = "PetPatientListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PetPatientListPanel.class);

    @FXML
    private ListView<PetPatientCard> petPatientListView;

    public PetPatientListPanel(ObservableList<PetPatient> petPatientList) {
        super(FXML);
        setConnections(petPatientList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<PetPatient> petPatientList) {
        ObservableList<PetPatientCard> mappedList = EasyBind.map(
            petPatientList, (petPatient) -> new PetPatientCard(petPatient, petPatientList.indexOf(petPatient) + 1));
        petPatientListView.setItems(mappedList);
        petPatientListView.setCellFactory(listView -> new PetPatientListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        petPatientListView.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    logger.fine("Selection in petPatient list panel changed to : '" + newValue + "'");
                    raise(new PetPatientPanelSelectionChangedEvent(newValue));
                }
            });
    }

    /**
     * Scrolls to the {@code PetPatientCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            petPatientListView.scrollTo(index);
            petPatientListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PetPatientCard}.
     */
    class PetPatientListViewCell extends ListCell<PetPatientCard> {

        @Override
        protected void updateItem(PetPatientCard petPatient, boolean empty) {
            super.updateItem(petPatient, empty);

            if (empty || petPatient == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(petPatient.getRoot());
            }
        }
    }

}
