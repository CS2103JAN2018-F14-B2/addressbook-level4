package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.PetPatientCard;

/**
 * Represents a selection change in the PetPatient list Panel
 */
public class PetPatientPanelSelectionChangedEvent extends BaseEvent {
    private final PetPatientCard newSelection;

    public PetPatientPanelSelectionChangedEvent(PetPatientCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public PetPatientCard getNewSelection() {
        return newSelection;
    }
}
