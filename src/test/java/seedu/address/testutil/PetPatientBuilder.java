package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Nric;
import seedu.address.model.petpatient.PetPatient;
import seedu.address.model.petpatient.PetPatientName;
import seedu.address.model.tag.Tag;

import seedu.address.model.util.SampleDataUtil;

//@@author chialejing
/**
 * A utility class to help with building PetPatient objects.
 */
public class PetPatientBuilder {
    public static final String DEFAULT_NAME = "Joseph";
    public static final String DEFAULT_SPECIES = "Cat";
    public static final String DEFAULT_BREED = "Persian Ragdoll";
    public static final String DEFAULT_COLOUR = "Brown";
    public static final String DEFAULT_BLOODTYPE = "AB";
    public static final String DEFAULT_OWNER = "G1234567B";
    public static final String DEFAULT_TAGS = "Injured";

    private PetPatientName name;
    private String species;
    private String breed;
    private String colour;
    private String bloodType;
    private Nric ownerNric;
    private Set<Tag> tags;

    public PetPatientBuilder() {
        name = new PetPatientName(DEFAULT_NAME);
        species = DEFAULT_SPECIES;
        breed = DEFAULT_BREED;
        colour = DEFAULT_COLOUR;
        bloodType = DEFAULT_BLOODTYPE;
        ownerNric = new Nric(DEFAULT_OWNER);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
    }

    /**
     * Initializes the PetPatientBuilder with the data of {@code petPatientToCopy}.
     */
    public PetPatientBuilder(PetPatient petPatientToCopy) {
        name = petPatientToCopy.getName();
        species = petPatientToCopy.getSpecies();
        breed = petPatientToCopy.getBreed();
        colour = petPatientToCopy.getColour();
        bloodType = petPatientToCopy.getBloodType();
        ownerNric = petPatientToCopy.getOwner();
        tags = new HashSet<>(petPatientToCopy.getTags());
    }

    /**
     * Sets the {@code PetPatientName} of the {@code PetPatient} that we are building.
     */
    public PetPatientBuilder withName(String name) {
        this.name = new PetPatientName(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code PetPatient} that we are building.
     */
    public PetPatientBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the species of the {@code PetPatient} that we are building.
     */
    public PetPatientBuilder withSpecies(String species) {
        this.species = species;
        return this;
    }

    /**
     * Sets the breed of the {@code PetPatient} that we are building.
     */
    public PetPatientBuilder withBreed(String breed) {
        this.breed = breed;
        return this;
    }

    /**
     * Sets the colour of the {@code PetPatient} that we are building.
     */
    public PetPatientBuilder withColour(String colour) {
        this.colour = colour;
        return this;
    }

    /**
     * Sets the blood type of the {@code PetPatient} that we are building.
     */
    public PetPatientBuilder withBloodType(String bloodType) {
        this.bloodType = bloodType;
        return this;
    }

    /**
     * Sets the {@code Nric} of the {@code PetPatient} that we are building.
     * @param nric
     * @return
     */
    public PetPatientBuilder withOwnerNric(String nric) {
        this.ownerNric = new Nric(nric);
        return this;
    }

    public PetPatient build() {
        return new PetPatient(name, species, breed, colour, bloodType, ownerNric, tags);
    }

}
