package dbprak20.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import dbprak20.pojo.Language;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "personspeaks")
public class PersonLanguage implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @Id
    @Column(name = "language")
    private String language;

    @Transient
    private final String url = "https://gist.githubusercontent.com/jrnk/8eb57b065ea0b098d571/raw/97efa7bc28c72bacfebce73e28e35cdfea34db63/ISO-639-1-language.json";

    /* GETTERS AND SETTERS */

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getLanguage() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // JSON file to Java objects
            List<Language> ls = mapper.readValue(new URL(url), mapper.getTypeFactory().constructCollectionType(List.class, Language.class));

            for (Language l : ls) {
                if (l.getCode().equals(this.language)) {
                    return l.getName().toLowerCase();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonLanguage that = (PersonLanguage) o;
        return person.equals(that.person) &&
                language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, language);
    }
}
