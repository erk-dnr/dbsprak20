package dbprak20.entity;

import dbprak20.key.PersonLikesCommentID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "likescomment")
@AssociationOverrides({
        @AssociationOverride(name = "id.person", joinColumns = @JoinColumn(name = "person")),
        @AssociationOverride(name = "id.comment", joinColumns = @JoinColumn(name = "comment"))
})
public class PersonLikesComment {

    @EmbeddedId
    private PersonLikesCommentID id = new PersonLikesCommentID();

    @ManyToOne
    @JoinColumn(name = "person", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "comment", updatable = false, insertable = false)
    private Comment comment;

    @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    /* GETTERS AND SETTERS */

    public PersonLikesCommentID getId() {
        return id;
    }

    public void setId(PersonLikesCommentID id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
