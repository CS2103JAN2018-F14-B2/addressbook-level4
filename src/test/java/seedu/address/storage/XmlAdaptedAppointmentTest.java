package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedAppointment.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalAppointments.ALICE_APP;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Remark;
import seedu.address.model.person.Nric;
import seedu.address.model.petpatient.PetPatientName;
import seedu.address.testutil.Assert;


public class XmlAdaptedAppointmentTest {

    private static final String INVALID_OWNER_NRIC = "S012345AB";
    private static final String INVALID_PET_PATIENT_NAME = "L@osai";
    private static final String INVALID_REMARK = " ";
    private static final String INVALID_DATETIME = "MAAAY 2018 8PM";
    private static final String INVALID_APPOINTMENT_TAG = "#checkup";

    private static final String VALID_OWNER_NRIC = ALICE_APP.getOwnerNric().toString();
    private static final String VALID_PET_PATIENT_NAME = ALICE_APP.getPetPatientName().toString();
    private static final String VALID_REMARK = ALICE_APP.getRemark().toString();
    private static final String VALID_DATETIME = ALICE_APP.getFormattedLocalDateTime();
    private static final List<XmlAdaptedTag> VALID_APPOINTMENT_TAGS = ALICE_APP.getAppointmentTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validAppointmentDetails_returnsAppointment() throws Exception {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(ALICE_APP);
        assertEquals(ALICE_APP, appointment.toModelType());
    }

    @Test
    public void toModelType_invalidOwnerNric_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        INVALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = Nric.MESSAGE_NRIC_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullOwnerNric_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        null,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidPetName_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        INVALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = PetPatientName.MESSAGE_PET_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullPetName_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        null,
                        VALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, PetPatientName.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidRemark_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        INVALID_REMARK,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = Remark.MESSAGE_REMARK_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullRemark_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(
                        VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        null,
                        VALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidDateTime_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        INVALID_DATETIME,
                        VALID_APPOINTMENT_TAGS);
        String expectedMessage = "Please follow the format of yyyy-MM-dd HH:mm";
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_nullDateTime_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(VALID_OWNER_NRIC,
                VALID_PET_PATIENT_NAME,
                VALID_REMARK,
                null,
                VALID_APPOINTMENT_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, LocalDateTime.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_APPOINTMENT_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_APPOINTMENT_TAG));
        XmlAdaptedAppointment appointment =
                new XmlAdaptedAppointment(VALID_OWNER_NRIC,
                        VALID_PET_PATIENT_NAME,
                        VALID_REMARK,
                        VALID_DATETIME,
                        invalidTags);
        Assert.assertThrows(IllegalValueException.class, appointment::toModelType);
    }

}
