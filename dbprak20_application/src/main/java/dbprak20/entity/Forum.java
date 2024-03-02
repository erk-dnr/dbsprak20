package dbprak20.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "forum")
public class Forum {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToOne
    @JoinColumn(name = "moderator", referencedColumnName = "id")
    private Person moderator;

    @OneToMany(mappedBy = "id.forum", cascade = CascadeType.ALL)
    private Set<ForumMember> personsByForum = new HashSet<>();

    @OneToMany(mappedBy = "forum")
    private List<Post> posts = new ArrayList<>();

    /* GETTERS AND SETTERS */

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Person getModerator() {
        return moderator;
    }

    public void setModerator(Person moderator) {
        this.moderator = moderator;
    }

    public Set<ForumMember> getPersonsByForum() {
        return personsByForum;
    }

    public void setPersonsByForum(Set<ForumMember> personsByForum) {
        this.personsByForum = personsByForum;
    }
}
