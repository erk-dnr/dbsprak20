package dbprak20.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "url")
    private String url;

    @ManyToMany(mappedBy = "tags")
    private Set<Person> persons = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL )
    @JoinTable(
            name = "hastype",
            joinColumns = { @JoinColumn(name = "tag") },
            inverseJoinColumns = { @JoinColumn(name = "tagclass") }
    )
    private Set<TagClass> tagClasses = new HashSet<>();


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
        Tag tag = (Tag) o;
        return this.id.equals(tag.getId());
    }

    /* GETTERS AND SETTERS */

    public Set<TagClass> getTagClasses() {
        return tagClasses;
    }

    public void setTagClasses(Set<TagClass> tagClasses) {
        this.tagClasses = tagClasses;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
