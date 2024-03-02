package dbprak20.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstName")
    @NotNull
    private String firstName;

    @Column(name = "lastName")
    @NotNull
    private String lastName;

    @Column(name = "gender")
    @NotNull
    private String gender;

    @Column(name = "birthday")
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "creationDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "locationIP")
    @NotNull
    private String locationIP;

    @Column(name = "browserUsed")
    @NotNull
    private String browserUsed;

    @ManyToOne
    @JoinColumn(name = "place", referencedColumnName = "id")
    private City city;

    @OneToMany(mappedBy = "id.personOne",
            cascade = CascadeType.ALL)
    private Set<KnowsBidirectionalView> friendships = new HashSet<>();

    @OneToMany(mappedBy = "id.person", cascade = CascadeType.ALL)
    private Set<ForumMember> forumsByPerson = new HashSet<>();

    @OneToMany(mappedBy = "id.person", cascade = CascadeType.ALL)
    private Set<PersonLikesComment> commentLikesOfPerson = new HashSet<>();

    @OneToMany(mappedBy = "id.person", cascade = CascadeType.ALL)
    private Set<PersonLikesPost> postLikesOfPerson = new HashSet<>();

    @OneToMany(mappedBy = "id.person", cascade = CascadeType.ALL)
    private Set<PersonUniversity> universitiesOfPerson = new HashSet<>();

    @OneToMany(mappedBy = "id.person", cascade = CascadeType.ALL)
    private Set<PersonCompany> companiesOfPerson = new HashSet<>();

    @OneToMany
    @JoinColumn(name="person")
    private List<PersonEmail> personEmails = new ArrayList<>();

    @OneToMany
    @JoinColumn(name="person")
    private List<PersonLanguage> personLanguages= new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hasinterest",
            joinColumns = @JoinColumn(name = "person"),
            inverseJoinColumns = @JoinColumn(name = "tag")
    )
    private Set<Tag> tags = new HashSet<>();

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o compared object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return this.id.equals(person.getId());
    }

    /**
     * Get all friends for current person
     *
     * @return list of all friends
     */
    public List<Person> getAllFriends() {
        List<Person> friends = new ArrayList<>();
        for (KnowsBidirectionalView kbv : this.getFriendships()){
            friends.add(kbv.getPersonTwo());
        }

        return friends;
    }

    /* GETTERS AND SETTERS */

    public List<PersonLanguage> getPersonLanguages() {
        return personLanguages;
    }

    public void setPersonLanguages(List<PersonLanguage> personLanguages) {
        this.personLanguages = personLanguages;
    }

    public List<PersonEmail> getPersonEmails() {
        return personEmails;
    }

    public void setPersonEmails(List<PersonEmail> personEmails) {
        this.personEmails = personEmails;
    }

    public Set<PersonCompany> getCompaniesOfPerson() {
        return companiesOfPerson;
    }

    public void setCompaniesOfPerson(Set<PersonCompany> companiesOfPerson) {
        this.companiesOfPerson = companiesOfPerson;
    }

    public Set<PersonUniversity> getUniversitiesOfPerson() {
        return universitiesOfPerson;
    }

    public void setUniversitiesOfPerson(Set<PersonUniversity> universitiesOfPerson) {
        this.universitiesOfPerson = universitiesOfPerson;
    }

    public Set<PersonLikesComment> getCommentLikesOfPerson() {
        return commentLikesOfPerson;
    }

    public void setCommentLikesOfPerson(Set<PersonLikesComment> commentLikesOfPerson) {
        this.commentLikesOfPerson = commentLikesOfPerson;
    }

    public Set<PersonLikesPost> getPostLikesOfPerson() {
        return postLikesOfPerson;
    }

    public void setPostLikesOfPerson(Set<PersonLikesPost> postLikesOfPerson) {
        this.postLikesOfPerson = postLikesOfPerson;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.getFirstName() + " " + getLastName();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocationIP() {
        return locationIP;
    }

    public void setLocationIP(String locationIP) {
        this.locationIP = locationIP;
    }

    public String getBrowserUsed() {
        return browserUsed;
    }

    public void setBrowserUsed(String browserUsed) {
        this.browserUsed = browserUsed;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<KnowsBidirectionalView> getFriendships() {
        return friendships;
    }

    public void setFriendships(Set<KnowsBidirectionalView> friendsOfFirstPerson) {
        this.friendships = friendsOfFirstPerson;
    }

    public Set<ForumMember> getForumsByPerson() {
        return forumsByPerson;
    }

    public void setForumsByPerson(Set<ForumMember> forumsByPerson) {
        this.forumsByPerson = forumsByPerson;
    }

}
