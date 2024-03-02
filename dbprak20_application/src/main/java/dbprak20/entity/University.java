package dbprak20.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "university")
public class University extends Organisation {

    @ManyToOne
    @JoinColumn(name = "place")
    private City city;

    @OneToMany(mappedBy = "id.university", cascade = CascadeType.ALL)
    private Set<PersonUniversity> personsOfUniversity = new HashSet<>();

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
        University university = (University) o;
        return this.getId().equals(university.getId());
    }

    /* GETTERS AND SETTERS */

    public Set<PersonUniversity> getPersonsOfUniversity() {
        return personsOfUniversity;
    }

    public void setPersonsOfUniversity(Set<PersonUniversity> personsOfUniversity) {
        this.personsOfUniversity = personsOfUniversity;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
