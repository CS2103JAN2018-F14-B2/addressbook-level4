# Robert-Peng
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String nric} into a {@code NRIC}.
     * Leading and trailing whitespaces will be trimmed.
     * @param nric
     * @return
     * @throws IllegalValueException
     */
    public static Nric parseNric(String nric) throws IllegalValueException {
        requireNonNull(nric);
        String trimmedNric = nric.trim();
        if (!Nric.isValidNric(trimmedNric)) {
            throw new IllegalValueException(Nric.MESSAGE_NRIC_CONSTRAINTS);
        }
        return new Nric(trimmedNric);
    }

    /**
     * Parses a {@code Optional<String> nric} into an {@code Optional<NRIC>} if {@code nric} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     * @param nric
     * @return
     * @throws IllegalValueException
     */
    public static Optional<Nric> parseNric(Optional<String> nric) throws IllegalValueException {
        requireNonNull(nric);
        return nric.isPresent() ? Optional.of(parseNric(nric.get())) : Optional.empty();
    }

```
###### \java\seedu\address\model\appointment\Appointment.java
``` java
    /**
     * Returns a list of tags as a string
     */
    public String getTagString() {
        StringBuilder tagString = new StringBuilder();
        Set<Tag> tagSet = Collections.unmodifiableSet(appointmentTags.toSet());
        Iterator iterator = tagSet.iterator();
        Tag tag = (Tag) iterator.next();
        while (iterator.hasNext()) {
            tagString.append(tag.tagName);
            tagString.append(", ");
            tag = (Tag) iterator.next();
        }
        tagString.append(tag.tagName);
        return tagString.toString().trim();
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\ConcurrentAppointmentException.java
``` java

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents an error message when user attempt to add new appointment that
 * interfere with other appointment time interval
 */
public class ConcurrentAppointmentException extends IllegalValueException {

    public ConcurrentAppointmentException () {
        super("Medeina should not add appointments to on-going appointment slots");
    }

}

```
###### \java\seedu\address\model\appointment\exceptions\PastAppointmentException.java
``` java

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents an error message when adding appointment with a past date
 */
public class PastAppointmentException extends IllegalValueException {

    public PastAppointmentException() {
        super("Medeina should not add appointments with past DateTime");
    }
}

```
###### \java\seedu\address\model\person\Nric.java
``` java
/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNRIC(String)}
 */
public class Nric {
    public static final String MESSAGE_NRIC_CONSTRAINTS = "Contact NRIC should be of the format #0000000@ "
        + "where # is a letter that can be S T F or G,\n"
        + "0000000 represents 7 digits which can be any number from 0-9,\n"
        + "@ can be any alphabet A-Z.\n"
        + "Both # and @ must be in upper case.";

    private static final String FIRST_CHAR_REGEX = "[STFG]";
    private static final String MIDDLE_NUM_REGEX = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
    private static final String LAST_CHAR_REGEX = "[A-Z]";
    public static final String NRIC_VALIDATION_REGEX = FIRST_CHAR_REGEX + MIDDLE_NUM_REGEX
        + LAST_CHAR_REGEX;
    public final String value;

    /**
     * Constructs a NRIC.
     *
     * @param nric A valid NRIC number
     */
    public Nric(String nric) {
        requireNonNull(nric);
        checkArgument(isValidNric(nric), MESSAGE_NRIC_CONSTRAINTS);
        this.value = nric;
    }

    /**
     * Returns if a given String is a valid NRIC
     * @param test
     * @return
     */
    public static boolean isValidNric(String test) {
        return test.matches(NRIC_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof Nric // instanceof handles nulls
            && this.value.equals(((Nric) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\ui\CalendarWindow.java
``` java
/**
 * Implement CalendarView from CalendarFX to show appointments
 */
public class CalendarWindow extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "CalendarPanel.fxml";

    private  ObservableList<Appointment> appointmentList;
    private Calendar calendar;

    @FXML
    private CalendarView calendarView;
    private DayView dayView;
    private WeekView weekView;

    /**
     *
     * @param OwnerList
     */
    public CalendarWindow(ObservableList<Appointment> appointmentList) {
        super(DEFAULT_PAGE);

        this.appointmentList = appointmentList;
        calendarView = new CalendarView();


        setView();
        setTime();
        setCalendar();
        disableViews();
        registerAsAnEventHandler(this);

    }

    private void setView() {
        this.dayView = calendarView.getDayPage().getDetailedDayView().getDayView();
        dayView.setHoursLayoutStrategy(DayViewBase.HoursLayoutStrategy.FIXED_HOUR_HEIGHT);
        dayView.setHourHeight(250);

        this.weekView = calendarView.getWeekPage().getDetailedWeekView().getWeekView();
        weekView.setHoursLayoutStrategy(DayViewBase.HoursLayoutStrategy.FIXED_HOUR_HEIGHT);
        weekView.setHourHeight(250);
    }

    private void setTime() {
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setToday(LocalDate.now());
        calendarView.setTime(LocalTime.now());
    }

    /**
     * Creates a new a calendar
     */
    private void setCalendar() {
        setTime();
        calendarView.getCalendarSources().clear();
        CalendarSource calendarSource = new CalendarSource("Appointments");
        int styleNumber = 0;
        int appointmentCounter = 0;

        for (Appointment appointment : appointmentList) {

            Calendar calendar = createCalendar(styleNumber, appointment);
            calendar.setReadOnly(true);
            calendarSource.getCalendars().add(calendar);

            LocalDateTime ldt = appointment.getDateTime();
            appointmentCounter++;

            Entry entry = new Entry (buildAppointment(appointment, appointmentCounter).toString());

            entry.setInterval(new Interval(ldt, ldt.plusMinutes(30)));

            styleNumber++;
            styleNumber = styleNumber % 7;

            calendar.addEntry(entry);

        }
        calendarView.getCalendarSources().add(calendarSource);
    }

    /**
     *
     * @param appointment
     * @param appointmentCounter
     * @return
     */
    private StringBuilder buildAppointment (Appointment appointment, int appointmentCounter) {
        final StringBuilder builder = new StringBuilder();
        builder.append(appointmentCounter)
            .append(". ")
            .append(appointment.getPetPatientName().toString() + "\n")
            .append("Contact Nric: " + appointment.getOwnerNric() + "\n")
            .append("Appointment type: " + appointment.getTagString());

        builder.append("\n");
        builder.append("Remarks: " + appointment.getRemark().toString());
        return builder;
    }

    /**
     *
     * @param styleNumber
     * @param appointment
     * @return a calendar with given info and corresponding style
     */
    private Calendar createCalendar(int styleNumber, Appointment appointment) {
        Calendar calendar = new Calendar(appointment.getPetPatientName().toString());
        calendar.setStyle(Calendar.Style.getStyle(styleNumber));
        calendar.setLookAheadDuration(Duration.ofDays(365));
        calendar.setLookBackDuration(Duration.ofDays(365));
        return calendar;
    }

    /**
     * close unwanted UI components
     */

    private void disableViews() {
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowSearchResultsTray(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowSourceTrayButton(false);
        calendarView.setShowPageSwitcher(false);
        calendarView.setShowToolBar(false);
        calendarView.showDayPage();
        calendarView.setShowSourceTray(false);
        calendarView.setShowPageToolBarControls(false);
    }

    public CalendarView getRoot() {
        return this.calendarView;
    }

    @Subscribe
    private void handleNewAppointmentEvent(AddressBookChangedEvent event) {
        appointmentList = event.data.getAppointmentList();
        Platform.runLater(
                this::setCalendar
        );

    }

```
###### \java\seedu\address\ui\PersonCard.java
``` java
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
     * Creates the tag labels for {@code person}.
     */
    private void createTags(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyleFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }
```
###### \java\seedu\address\ui\PetPatientCard.java
``` java
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
        species.setText("Species:\t\t" + petPatient.getSpecies().toString());
        breed.setText("Breed:\t\t" + petPatient.getBreed().toString());
        colour.setText("Colour:\t\t" + petPatient.getColour().toString());
        bloodType.setText("Blood Type:\t" + petPatient.getBloodType().toString());
        ownerNric.setText("Owner NRIC:\t" + petPatient.getOwner().toString());
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
```
###### \java\seedu\address\ui\PetPatientListPanel.java
``` java
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
```
###### \resources\view\CalendarPanel.fxml
``` fxml
<?import java.lang.*?>
<?import java.util.*?>
<?import javafx.scene.*?>
<?import javafx.scene.control.*?>

<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.BorderPane?>
<?import javafx.scene.web.WebView?>

<StackPane xmlns = "http://javafx.com/javafx/">
    <children>
        <BorderPane prefHeight="200.0" prefWidth="200.0" />
    </children>
</StackPane>
```
###### \resources\view\DarkTheme.css
``` css

.tab-pane .tab-header-area .tab-background{
-fx-opacity: 0;
}

#owner-Tab {
    -fx-background-color: A9A9A9;
    -fx-text-base-color: black;
}

#petPatient-Tab {
    -fx-background-color: A9A9A9;
    -fx-text-base-color: black;
}

#owner-pane {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-base: null;
    -fx-border-color: null;
    -fx-background-color: derive(#1d1d1d, 20%);

}

#petPatient-pane {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-base: null;
    -fx-border-color: null;
    -fx-background-color: derive(#1d1d1d, 20%);
}

```
###### \resources\view\DarkTheme.css
``` css

#personListView {
    -fx-background-color: derive(#1d1d1d, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: white;
}

#personListView .list-cell:filled:odd {
    -fx-background-color: #515658;
}

#petPatientListView {
    -fx-background-color: derive(#1d1d1d, 20%);
}

#petPatientListView .list-cell:filled:odd {
    -fx-background-color: #515658
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-cell:filled:even {
    -fx-background-color: transparent;
}

.list-cell:filled:odd {
    -fx-background-color: transparent;
}
```
###### \resources\view\LightTheme.css
``` css

#personListView {
    -fx-background-color: derive(white, 20%);
}


.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: white;
}

#personListView .list-cell:filled:odd {
    -fx-background-color: white;
}

#personListView .list-cell:filled:even {
    -fx-background-color: #f7f1dc;
}

#petPatientListView {
    -fx-background-color: derive(white, 20%);
}

#petPatientListView .list-cell:filled:odd {
    -fx-background-color: white;
}

#petPatientListView .list-cell:filled:even {
    -fx-background-color: #f7f1dc;
}


.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-cell:filled:even {
    -fx-background-color: white;
}

.list-cell:filled:odd {
    -fx-background-color: white;
}

```
###### \resources\view\PetPatientListCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="105" GridPane.columnIndex="0">
            <padding>
                <Insets bottom="5" left="15" right="5" top="5" />
            </padding>
            <HBox alignment="CENTER_LEFT" spacing="5">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="name" styleClass="cell_big_label" text="\$first" />
            </HBox>
            <FlowPane fx:id="tags" />
            <Label fx:id="species" styleClass="cell_small_label" text="\$species" />
            <Label fx:id="breed" styleClass="cell_small_label" text="\$breed" />
            <Label fx:id="colour" styleClass="cell_small_label" text="\$colour" />
            <Label fx:id="bloodType" styleClass="cell_small_label" text="\$bloodType" />
            <Label fx:id="ownerNric" styleClass="cell_small_label" text="\$ownerNric" />
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\PetPatientListPanel.fxml
``` fxml
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.Tab?>
<?import javafx.scene.control.TabPane?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <TabPane id="petPatient-pane" prefHeight="42.0" prefWidth="200.0" tabClosingPolicy="UNAVAILABLE" VBox.vgrow="NEVER">
        <tabs>
            <Tab id="petPatient-Tab" text=" Pet Patients " >
                <content>
                    <AnchorPane  minHeight="0.0" minWidth="0.0" prefHeight="170.0" prefWidth="200.0" />
                </content>
            </Tab>
        </tabs>
    </TabPane>
    <ListView fx:id="petPatientListView" VBox.vgrow="ALWAYS" />
</VBox>
```
