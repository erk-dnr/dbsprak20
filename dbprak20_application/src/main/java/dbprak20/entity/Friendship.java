package dbprak20.entity;

import dbprak20.key.KnowsID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "knows")
@AssociationOverrides({
        @AssociationOverride(name = "id.personOne", joinColumns = @JoinColumn(name = "personOne")),
        @AssociationOverride(name = "id.personTwo", joinColumns = @JoinColumn(name = "personTwo"))
})
public class Friendship {

    @EmbeddedId
    private KnowsID id = new KnowsID();

    @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "personOne", updatable = false, insertable = false)
    private Person personOne;

    @ManyToOne
    @JoinColumn(name = "personTwo", updatable = false, insertable = false)
    private Person personTwo;

    /* GETTERS AND SETTERS */

    public KnowsID getId() {
        return id;
    }

    public void setId(KnowsID id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

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
}
