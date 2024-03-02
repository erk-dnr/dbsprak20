package dbprak20.key;

import dbprak20.entity.Person;
import dbprak20.entity.Post;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonLikesPostID implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;

    /* GETTERS AND SETTERS */

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonLikesPostID that = (PersonLikesPostID) o;
        return person.equals(that.person) &&
                post.equals(that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, post);
    }
}
