package dbprak20.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company extends Organisation {

    @ManyToOne
    @JoinColumn(name = "place")
    private Country country;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<PersonCompany> personsOfCompany = new HashSet<>();

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
        Company company = (Company) o;
        return this.getId().equals(company.getId());
    }

    /* GETTERS AND SETTERS */

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<PersonCompany> getPersonsOfCompany() {
        return personsOfCompany;
    }

    public void setPersonsOfCompany(Set<PersonCompany> personsOfCompany) {
        this.personsOfCompany = personsOfCompany;
    }
}
