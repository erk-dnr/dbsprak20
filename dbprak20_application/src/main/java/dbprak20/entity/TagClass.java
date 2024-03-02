package dbprak20.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tagClass")
public class TagClass {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "url")
    private String url;

    @ManyToMany(mappedBy = "tagClasses")
    private Set<Tag> tags = new HashSet<>();

    /**
     * returns all direct superclasses
     */
    // was @ManyToMany, Set<TagClass>
    // not matching UML but should be fine, because every TagClass has only one superclass in the DB
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinTable(name="issubclassof",
            joinColumns={@JoinColumn(name="tagclassone")},
            inverseJoinColumns={@JoinColumn(name="tagclasstwo")})
    private TagClass directSuperClasses;

    /**
     * returns all direct subclasses
     */
    // was @ManyToMany
    @OneToMany(mappedBy= "directSuperClasses")
    private Set<TagClass> directSubClasses = new HashSet<>();

    /* GETTERS AND SETTERS */

    // was Set<TagClass>
    public TagClass getDirectSuperClasses() {
        return directSuperClasses;
    }

    // was Set<TagClass>
    public void setDirectSuperClasses(TagClass firstTagClass) {
        this.directSuperClasses = firstTagClass;
    }

    public Set<TagClass> getDirectSubClasses() {
        return directSubClasses;
    }

    public void setDirectSubClasses(Set<TagClass> secondTagClass) {
        this.directSubClasses = secondTagClass;
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
