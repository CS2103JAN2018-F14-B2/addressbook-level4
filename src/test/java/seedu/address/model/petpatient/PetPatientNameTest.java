package seedu.address.model.petpatient;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author chialejing
public class PetPatientNameTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PetPatientName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PetPatientName(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        Assert.assertThrows(NullPointerException.class, () -> PetPatientName.isValidName(null));

        // invalid name
        assertFalse(PetPatientName.isValidName("")); // empty string
        assertFalse(PetPatientName.isValidName(" ")); // spaces only
        assertFalse(PetPatientName.isValidName("^")); // only non-alphanumeric characters
        assertFalse(PetPatientName.isValidName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(PetPatientName.isValidName("joker the second")); // alphabets only
        assertTrue(PetPatientName.isValidName("12345")); // numbers only
        assertTrue(PetPatientName.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(PetPatientName.isValidName("Aye Captain")); // with capital letters
        assertTrue(PetPatientName.isValidName("Aye Captain Howdy There Jr 2nd")); // long names
    }
}
