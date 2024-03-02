package dbprak20.entity;

import dbprak20.key.PersonCompanyID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "workat")
@AssociationOverrides({
        @AssociationOverride(name = "id.person", joinColumns = @JoinColumn(name = "person")),
        @AssociationOverride(name = "id.company", joinColumns = @JoinColumn(name = "company"))
})
public class PersonCompany {

    @EmbeddedId
    private PersonCompanyID id;

    @Column(name = "workfrom")
    @NotNull
    private int workFrom;

    @ManyToOne
    @JoinColumn(name = "person", updatable = false, insertable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "company", updatable = false, insertable = false)
    private Company company;

    public PersonCompanyID getId() {
        return id;
    }

    /* GETTERS AND SETTERS */

    public void setId(PersonCompanyID id) {
        this.id = id;
    }

    public int getWorkFrom() {
        return workFrom;
    }

    public void setWorkFrom(int workFrom) {
        this.workFrom = workFrom;
    }

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
}
