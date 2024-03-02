package dbprak20.key;

import dbprak20.entity.*;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonLikesCommentID implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    private Comment comment;

    /* GETTERS AND SETTERS */

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonLikesCommentID that = (PersonLikesCommentID) o;
        return person.equals(that.person) &&
                comment.equals(that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, comment);
    }
}
