package seedu.address.model.petpatient;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.petpatient.exceptions.DuplicatePetPatientException;
import seedu.address.model.petpatient.exceptions.PetPatientNotFoundException;

//@@author chialejing
/**
 * A list of pet patients that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see PetPatient#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePetPatientList implements Iterable<PetPatient> {
    private final ObservableList<PetPatient> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent pet patient as the given argument.
     */
    public boolean contains(PetPatient toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a pet patient to the list.
     *
     * @throws DuplicatePetPatientException if the pet patient to add is a duplicate of an existing pet patient
     * in the list.
     */
    public void add(PetPatient toAdd) throws DuplicatePetPatientException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePetPatientException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the pet patient {@code target} in the list with {@code editedPetPatient}.
     *
     * @throws DuplicatePetPatientException if the replacement is equivalent to another existing pet patient
     * in the list.
     * @throws PetPatientNotFoundException if {@code target} could not be found in the list.
     */
    public void setPetPatient(PetPatient target, PetPatient editedPetPatient)
            throws DuplicatePetPatientException, PetPatientNotFoundException {
        requireNonNull(editedPetPatient);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PetPatientNotFoundException();
        }

        if (!target.equals(editedPetPatient) && internalList.contains(editedPetPatient)) {
            throw new DuplicatePetPatientException();
        }

        internalList.set(index, editedPetPatient);
    }

    /**
     * Removes the equivalent pet patient from the list.
     *
     * @throws PetPatientNotFoundException if no such pet patient could be found in the list.
     */
    public boolean remove(PetPatient toRemove) throws PetPatientNotFoundException {
        requireNonNull(toRemove);
        final boolean petPatientFoundAndDeleted = internalList.remove(toRemove);
        if (!petPatientFoundAndDeleted) {
            throw new PetPatientNotFoundException();
        }
        return petPatientFoundAndDeleted;
    }

    public void setPetPatients(UniquePetPatientList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPetPatients(List<PetPatient> petPatients) throws DuplicatePetPatientException {
        requireAllNonNull(petPatients);
        final UniquePetPatientList replacement = new UniquePetPatientList();
        for (final PetPatient petPatient : petPatients) {
            replacement.add(petPatient);
        }
        setPetPatients(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<PetPatient> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<PetPatient> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePetPatientList // instanceof handles nulls
                && this.internalList.equals(((UniquePetPatientList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
