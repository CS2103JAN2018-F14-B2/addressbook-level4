package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPetPatient;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.PetPatientCardHandle;
import seedu.address.model.petpatient.PetPatient;
import seedu.address.testutil.PetPatientBuilder;

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
