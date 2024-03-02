package dbprak20.entity;

import dbprak20.key.KnowsID;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "pkp_symmetric")
@Immutable
@Table
@AssociationOverrides({
        @AssociationOverride(name = "id.personOne", joinColumns = @JoinColumn(name = "personOne")),
        @AssociationOverride(name = "id.personTwo", joinColumns = @JoinColumn(name = "personTwo"))
})
public class KnowsBidirectionalView {

    @EmbeddedId
    private KnowsID id = new KnowsID();

    @ManyToOne
    @JoinColumn(name = "personOne", updatable = false, insertable = false)
    private Person personOne;

    @ManyToOne
    @JoinColumn(name = "personTwo", updatable = false, insertable = false)
    private Person personTwo;

    @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    /* GETTERS ONLY */

    public KnowsID getId() {
        return id;
    }

    public Person getPersonOne() {
        return personOne;
    }

    public Person getPersonTwo() {
        return personTwo;
    }

    public Date getCreationDate() {
        return creationDate;
    }

}
