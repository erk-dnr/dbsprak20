package dbprak20.entity;

import dbprak20.key.ForumMemberID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "hasmember")
@AssociationOverrides({
        @AssociationOverride(name = "id.person", joinColumns = @JoinColumn(name = "person")),
        @AssociationOverride(name = "id.forum", joinColumns = @JoinColumn(name = "forum"))
})
public class ForumMember {

    @EmbeddedId
    private ForumMemberID id = new ForumMemberID();

    @Column(name = "joinDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date joinDate;

    @ManyToOne
    @JoinColumn(name = "person", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "forum", updatable = false, insertable = false)
    private Forum forum;

    /* GETTERS AND SETTERS */

    public ForumMemberID getId() {
        return id;
    }

    public void setId(ForumMemberID id) {
        this.id = id;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

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
}
