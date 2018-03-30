package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.appointment.Appointment;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalAppointments {

    public static final Appointment ALICE_APP = new AppointmentBuilder()
            .withOwnerNric(TypicalPersons.ALICE.getNric().toString())
            .withPetPatientName(TypicalPetPatients.JEWEL.getName().toString())
            .withRemark("Requires Home Visit")
            .withDateTime("2018-12-31 12:30")
            .withAppointmentTags("checkup").build();
    public static final Appointment BOB_APP = new AppointmentBuilder()
            .withOwnerNric(TypicalPersons.BOB.getNric().toString())
            .withPetPatientName(TypicalPetPatients.JOKER.getName().toString())
            .withRemark("May require isolation")
            .withDateTime("2018-12-31 14:30")
            .withAppointmentTags("vaccination").build();

    private TypicalAppointments() {} // prevents instantiation

    public static List<Appointment> getTypicalAppointments() {
        return new ArrayList<>(Arrays.asList(ALICE_APP, BOB_APP));
    }
}
