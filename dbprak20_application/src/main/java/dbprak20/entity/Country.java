package dbprak20.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "country")
public class Country extends Place {
    @ManyToOne
    @JoinColumn(name = "isPartOf", referencedColumnName = "id")
    @NotNull
    private Continent continent;

    @OneToMany(mappedBy = "country")
    private List<City> cities = new ArrayList<>();

    @OneToMany(mappedBy = "country", cascade=CascadeType.ALL)
    private Set<Post> postsFromCountry = new HashSet<>();

    @OneToMany(mappedBy = "country", cascade=CascadeType.ALL)
    private Set<Comment> commentsFromCountry = new HashSet<>();

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
        Country country = (Country) o;
        return this.getId().equals(country.getId());
    }

    /* GETTERS AND SETTERS */

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public Set<Post> getPostsFromCountry() {
        return postsFromCountry;
    }

    public void setPostsFromCountry(Set<Post> postsFromCountry) {
        this.postsFromCountry = postsFromCountry;
    }

    public Set<Comment> getCommentsFromCountry() {
        return commentsFromCountry;
    }

    public void setCommentsFromCountry(Set<Comment> commentsFromCountry) {
        this.commentsFromCountry = commentsFromCountry;
    }
}
