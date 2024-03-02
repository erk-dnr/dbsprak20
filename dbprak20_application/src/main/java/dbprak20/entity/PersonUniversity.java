package dbprak20.entity;

import dbprak20.key.PersonUniversityID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "studyat")
@AssociationOverrides({
        @AssociationOverride(name = "id.person", joinColumns = @JoinColumn(name = "person")),
        @AssociationOverride(name = "id.university", joinColumns = @JoinColumn(name = "university"))
})
public class PersonUniversity {

    @EmbeddedId
    private PersonUniversityID id = new PersonUniversityID();

    @Column(name = "classyear")
    @NotNull
    private int classYear;

    @ManyToOne
    @JoinColumn(name = "person", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "university", updatable = false, insertable = false)
    private University university;

    /* GETTERS AND SETTERS */

    public PersonUniversityID getId() {
        return id;
    }

    public void setId(PersonUniversityID id) {
        this.id = id;
    }

    public int getClassYear() {
        return classYear;
    }

    public void setClassYear(int classYear) {
        this.classYear = classYear;
    }

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
}
