package dbprak20.entity;

import dbprak20.key.PersonLikesPostID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "likespost")
@AssociationOverrides({
        @AssociationOverride(name = "id.person", joinColumns = @JoinColumn(name = "person")),
        @AssociationOverride(name = "id.post", joinColumns = @JoinColumn(name = "post"))
})
public class PersonLikesPost {

    @EmbeddedId
    private PersonLikesPostID id = new PersonLikesPostID();

    @ManyToOne
    @JoinColumn(name = "person", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "post", updatable = false, insertable = false)
    private Post comment;

    @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    /* GETTERS AND SETTERS */

    public PersonLikesPostID getId() {
        return id;
    }

    public void setId(PersonLikesPostID id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Post getComment() {
        return comment;
    }

    public void setComment(Post comment) {
        this.comment = comment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
