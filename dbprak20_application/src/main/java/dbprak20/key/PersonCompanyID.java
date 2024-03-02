package dbprak20.key;

import dbprak20.entity.Company;
import dbprak20.entity.Person;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonCompanyID implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    private Company company;

    /* GETTERS AND SETTERS */

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonCompanyID that = (PersonCompanyID) o;
        return person.equals(that.person) &&
                company.equals(that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, company);
    }
}
