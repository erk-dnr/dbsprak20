package dbprak20.key;

import dbprak20.entity.Forum;
import dbprak20.entity.Person;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ForumMemberID implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    private Forum forum;

    /* GETTERS AND SETTERS */

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumMemberID that = (ForumMemberID) o;
        return person.equals(that.person) &&
                forum.equals(that.forum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, forum);
    }
}
