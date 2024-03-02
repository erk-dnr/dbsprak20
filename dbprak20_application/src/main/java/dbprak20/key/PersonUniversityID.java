package dbprak20.key;

import dbprak20.entity.Person;
import dbprak20.entity.University;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonUniversityID implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    private University university;

    /* GETTERS AND SETTERS */

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonUniversityID that = (PersonUniversityID) o;
        return person.equals(that.person) &&
                university.equals(that.university);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, university);
    }
}
