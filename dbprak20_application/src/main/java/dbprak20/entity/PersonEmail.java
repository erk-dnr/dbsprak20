package dbprak20.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "personemail")
public class PersonEmail implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @Id
    @Column(name = "email")
    private String email;

    /* GETTERS AND SETTERS */

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEmail that = (PersonEmail) o;
        return person.equals(that.person) &&
                email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, email);
    }
}
