package dbprak20.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "city")
public class City extends Place {
    @ManyToOne
    @JoinColumn(name = "ispartof", referencedColumnName = "id")
    @NotNull
    private Country country;

    @OneToMany(mappedBy = "city")
    private List<Person> persons = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        City city = (City) o;
        return this.getId().equals(city.getId());
    }

    /* GETTERS AND SETTERS */

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
