# wynonaK-reused
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes all {@code tag}s not used by anyone in this {@code AddressBook}.
     * Reused from https://github.com/se-edu/addressbook-level4/pull/790/files with minor modifications
     */
    private void removeUselessTags() {
        Set<Tag> personTags =
                persons.asObservableList()
                        .stream()
                        .map(Person::getTags)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());
        Set<Tag> appointmentTags =
                appointments.asObservableList()
                        .stream()
                        .map(Appointment::getAppointmentTags)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());
        Set<Tag> petPatientTags =
                petPatients.asObservableList()
                        .stream()
                        .map(PetPatient::getTags)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());

        personTags.addAll(appointmentTags);
        personTags.addAll(petPatientTags);
        tags.setTags(personTags);
    }

    /**
     * Removes {@code tag} from {@code person} with that tag this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if {@code person} is not found in this {@code AddressBook}.
     *                                 Reused from https://github.com/se-edu/addressbook-level4/
     *                                 pull/790/files with minor modifications
     */
    private void removeTagParticular(Tag tag, Person person) throws PersonNotFoundException {
        Set<Tag> tagList = new HashSet<>(person.getTags()); //gets all the tags from a person

        if (tagList.remove(tag)) {
            Person updatedPerson =
                    new Person(person.getName(), person.getPhone(), person.getEmail(),
                        person.getAddress(), person.getNric(), tagList);

            try {
                updatePerson(person, updatedPerson);
            } catch (DuplicatePersonException dpe) {
                throw new AssertionError("Modifying tag only should not result in duplicate contact.");
            }
        } else {
            return;
        }
    }

    /**
     * Removes {@code tag} from all person with that tag this {@code AddressBook}.
     * Reused from https://github.com/se-edu/addressbook-level4/pull/790/files with minor modifications
     */
    public void removeTag(Tag tag) {
        try {
            for (Person currPerson : persons) {
                removeTagParticular(tag, currPerson);
            }
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Impossible as obtained from Medeina.");
        }
    }

    //// util methods

```
