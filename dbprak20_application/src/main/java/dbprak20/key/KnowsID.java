package dbprak20.key;

import dbprak20.entity.Person;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class KnowsID implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Person personOne;

    @ManyToOne(cascade = CascadeType.ALL)
    private Person personTwo;

    /* GETTERS AND SETTERS */

    public Person getPersonOne() {
        return personOne;
    }

    public void setPersonOne(Person personOne) {
        this.personOne = personOne;
    }

    public Person getPersonTwo() {
        return personTwo;
    }

    public void setPersonTwo(Person personTwo) {
        this.personTwo = personTwo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowsID knowsID = (KnowsID) o;
        return personOne.equals(knowsID.personOne) &&
                personTwo.equals(knowsID.personTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personOne, personTwo);
    }
}
