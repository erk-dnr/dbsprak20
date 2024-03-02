package dbprak20.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "comment")
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
public class Comment extends Message {

    @ManyToOne
    @JoinColumn(name = "replyofcomment")
    private Comment replyOfComment;

    @ManyToOne
    @JoinColumn(name = "replyofpost")
    private Post replyOfPost;

    @OneToMany(mappedBy = "replyOfComment")
    private List<Comment> repliesToComment = new ArrayList<>();

    @OneToMany(mappedBy = "id.comment", cascade = CascadeType.ALL)
    private Set<PersonLikesComment> personLikesOfComment = new HashSet<>();

    /* GETTERS AND SETTERS */

    public Set<PersonLikesComment> getPersonLikesOfComment() {
        return personLikesOfComment;
    }

    public void setPersonLikesOfComment(Set<PersonLikesComment> personLikesOfComment) {
        this.personLikesOfComment = personLikesOfComment;
    }

    public Comment getReplyOfComment() {
        return replyOfComment;
    }

    public void setReplyOfComment(Comment replyOfComment) {
        this.replyOfComment = replyOfComment;
    }

    public Post getReplyOfPost() {
        return replyOfPost;
    }

    public void setReplyOfPost(Post replyOfPost) {
        this.replyOfPost = replyOfPost;
    }

    public List<Comment> getRepliesToComment() {
        return repliesToComment;
    }

    public void setRepliesToComment(List<Comment> repliesToComment) {
        this.repliesToComment = repliesToComment;
    }
}
