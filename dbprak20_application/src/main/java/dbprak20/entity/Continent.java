package dbprak20.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "continent")
public class Continent extends Place {
    @OneToMany(mappedBy = "continent")
    private List<Country> countries = new ArrayList<>();

    /* GETTERS AND SETTERS */

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
