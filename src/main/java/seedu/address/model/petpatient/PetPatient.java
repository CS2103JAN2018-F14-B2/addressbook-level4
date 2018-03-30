package seedu.address.model.petpatient;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.person.Nric;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

//@@author chialejing
/**
 * Represents a PetPatient in the address book.
 * Guarantees: details are present, field values are validated.
 */
public class PetPatient {
    private final PetPatientName name;
    private final String species; // e.g. dogs, cats, birds, etc.
    private final String breed; // different varieties of the same species
    private final String colour;
    private final String bloodType;

    private final UniqueTagList tags;

    private final Optional<Date> dateOfBirth; // can be null
    private Nric ownerNric; // can be null (initially)
    private StringBuilder medicalHistory; // can be null (initially)

    //keep this constructor, as owner NRIC can be null initially when adding a new PetPatient
    public PetPatient(PetPatientName name,
                      String species,
                      String breed,
                      String colour,
                      String bloodType,
                      Set<Tag> tags) {
        requireAllNonNull(name, species, breed, colour, bloodType, tags);
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.bloodType = bloodType;
        this.tags = new UniqueTagList(tags);

        this.ownerNric = null;
        this.dateOfBirth = null;
        this.medicalHistory = new StringBuilder();
    }

    public PetPatient(PetPatientName name,
                      String species,
                      String breed,
                      String colour,
                      String bloodType,
                      Nric ownerNric,
                      Set<Tag> tags) {
        requireAllNonNull(name, species, breed, colour, bloodType, tags);
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.bloodType = bloodType;
        this.tags = new UniqueTagList(tags);
        this.ownerNric = ownerNric;
        this.dateOfBirth = null;
        this.medicalHistory = new StringBuilder();
    }

    //keep this constructor
    public PetPatient(PetPatientName name,
                      String species,
                      String breed,
                      String colour,
                      String bloodType,
                      Nric ownerNric,
                      Optional<Date> dateOfBirth,
                      Set<Tag> tags) {
        requireAllNonNull(name, species, breed, colour, bloodType, ownerNric, dateOfBirth, tags);
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.bloodType = bloodType;
        this.ownerNric = ownerNric;
        this.dateOfBirth = dateOfBirth;
        this.tags = new UniqueTagList(tags);
        this.medicalHistory = new StringBuilder();
    }

    public PetPatientName getName() {
        return name;
    }

    public Optional<Date> getDateOfBirth() {
        return dateOfBirth;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public String getColour() {
        return colour;
    }

    public String getBloodType() {
        return bloodType;
    }

    public Nric getOwner() {
        return ownerNric;
    }

    public void setOwnerNric(Nric ownerNric) {
        this.ownerNric = ownerNric;
    }

    public StringBuilder getMedicalHistory() {
        return medicalHistory;
    }

    public void updateMedicalHistory(String newContent) {
        this.medicalHistory.append(newContent);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PetPatient)) {
            return false;
        }

        PetPatient otherPetPatient = (PetPatient) other;
        return otherPetPatient.getName().equals(this.getName())
                && otherPetPatient.getSpecies().equals(this.getSpecies())
                && otherPetPatient.getBreed().equals(this.getBreed())
                && otherPetPatient.getColour().equals(this.getColour())
                && otherPetPatient.getBloodType().equals(this.getBloodType())
                && otherPetPatient.getOwner().equals(this.getOwner());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, species, breed, colour, bloodType, tags, ownerNric);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\t")
                .append(getName())
                .append("\tSpecies: ")
                .append(getSpecies())
                .append("\tBreed: ")
                .append(getBreed())
                .append("\tColor: ")
                .append(getColour())
                .append("\tBlood Type: ")
                .append(getBloodType())
                .append("\t\tOwner's NRIC: ")
                .append(getOwner())
                .append("\tTags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
