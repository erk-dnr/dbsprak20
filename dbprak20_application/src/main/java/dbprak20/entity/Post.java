package dbprak20.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id")),
        @AttributeOverride(name = "creationDate", column = @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")),
        @AttributeOverride(name = "locationIP", column = @Column(name = "locationIp")),
        @AttributeOverride(name = "browserUsed", column = @Column(name = "browserUsed")),
        @AttributeOverride(name = "content", column = @Column(name = "content")),
        @AttributeOverride(name = "length", column = @Column(name = "length"))
})
@AssociationOverrides({
        @AssociationOverride(name = "creator", joinColumns = @JoinColumn(name = "creator", referencedColumnName = "id")),
        @AssociationOverride(name = "country", joinColumns = @JoinColumn(name = "place", referencedColumnName = "id"))
})
public class Post extends Message {

    @Column(name = "language")
    private String language;

    @Column(name = "imagefile")
    private String imageFile;

    @ManyToOne
    @JoinColumn(name = "forum", referencedColumnName = "id")
    private Forum forum;

    @OneToMany(mappedBy = "replyOfPost")
    private List<Comment> repliesToPost = new ArrayList<>();

    @OneToMany(mappedBy = "id.post", cascade = CascadeType.ALL)
    private Set<PersonLikesPost> personLikesOfPost = new HashSet<>();

    /* GETTERS AND SETTERS */

    public Set<PersonLikesPost> getPersonLikesOfPost() {
        return personLikesOfPost;
    }

    public void setPersonLikesOfPost(Set<PersonLikesPost> personLikesOfPost) {
        this.personLikesOfPost = personLikesOfPost;
    }

    public List<Comment> getRepliesToPost() {
        return repliesToPost;
    }

    public void setRepliesToPost(List<Comment> repliesToPost) {
        this.repliesToPost = repliesToPost;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
