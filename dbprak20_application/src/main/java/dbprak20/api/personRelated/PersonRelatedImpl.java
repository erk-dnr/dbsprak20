package dbprak20.api.personRelated;

import dbprak20.entity.*;
import dbprak20.user_types.PathArrayUserType;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;

import java.util.*;

public class PersonRelatedImpl implements PersonRelatedAPI {

    private final Person person;
    private final Session session;

    public PersonRelatedImpl(Person person, Session session) {
        this.person = person;
        this.session = session;
    }

    /**
     * Output of a person's profile, i.e. all personal information (name, gender, place of residence ...)
     *
     * @return String
     */
    @Override
    public String getProfile() {
        String result = "";
        result += "\n\nName: " + this.person.getFullName();
        result += "\nGender: " + this.person.getGender();
        result += "\nPlace of residence: " + this.person.getCity().getName() + " (" + this.person.getCity().getCountry().getName() +
                ", " + this.person.getCity().getCountry().getContinent().getName() + ")";
        result += "\nBirthday: " + this.person.getBirthday();
        result += "\nE-Mail addresses: " + this.getAllEmails(this.person.getPersonEmails());
        result += "\nLanguages: " + this.getAllLanguages(this.person.getPersonLanguages());
        result += "\nLocationIP: " + this.person.getLocationIP();
        result += "\nBrowser used: " + this.person.getBrowserUsed();
        return result;
    }

    /**
     * Get all person languages
     *
     * @param personLanguageList PersonLanguages list
     * @return String
     */
    private String getAllLanguages(List<PersonLanguage> personLanguageList) {
        List<String> languageList = new ArrayList<>();

        for (PersonLanguage pl: personLanguageList) {
            languageList.add(pl.getLanguage());
        }

        return String.join(", ", languageList);
    }

    /**
     * Get all person e-mails
     *
     * @param personEmailsList PersonEmail list
     * @return String
     */
    private String getAllEmails(List<PersonEmail> personEmailsList) {
        List<String> emailList = new ArrayList<>();

        for (PersonEmail pe: personEmailsList) {
            emailList.add(pe.getEmail());
        }

        return String.join(", ", emailList);
    }

    /**
     * Output of overlapping interests (tagID + name) of the person and his friends.
     *
     * @return String
     */
    @Override
    public String getCommonInterestsOfMyFriends() {
        StringBuilder strBuildTag = new StringBuilder();
        Map<Tag, List<Person>> interests = new HashMap<>();
        for (Tag personTag : person.getTags()) {
            List<Person> persons = new ArrayList<>();
            for (Person friend : person.getAllFriends())
                for (Tag friendTag : friend.getTags())
                    if (personTag.equals(friendTag)) {
                        persons.add(friend);
                        interests.put(personTag, persons);
                    }
        }

        for (Map.Entry<Tag, List<Person>> entry : interests.entrySet()) {
            strBuildTag.append("TAG_ID: ").append(entry.getKey().getId()).append(", ")
                    .append("TAG_Name: ").append(entry.getKey().getName()).append("\n")
                    .append("Friends with this interest:\n");

            for (Person friend : entry.getValue()) {
                strBuildTag.append("FRIEND_ID: ").append(friend.getId()).append(", ")
                        .append("FRIEND_Name: ").append(friend.getFullName()).append("\n");
            }
            strBuildTag.append("\n");
        }

        String result = strBuildTag.toString();
        if (result.isEmpty())
            result += "No overlapping interests";

        return result;
    }

    /**
     * Output the mutual friends for both persons
     *
     * @param friend Friend
     * @return String
     */
    @Override
    public String getCommonFriends(Person friend) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Person f : this.person.getAllFriends())
            if (friend.getAllFriends().contains(f))
                stringBuilder.append("ID: ").append(f.getId()).append(", ")
                        .append("Name: ").append(f.getFullName()).append("\n");

        String result = stringBuilder.toString();

        if (!result.isEmpty()) {
            result = "These are the common friends of " + this.person.getFullName() + " and " + friend.getFullName() + ":\n" + result;
        } else {
            result = "No mutual friends";
        }

        return result;
    }

    /**
     * From all persons, the person (ID + name) should be determined who has the maximum matching interests (absolute number)
     * with the person entered (whereby the input person himself should not be output).
     *
     * @return String
     */
    @Override
    public String getPersonsWithMostCommonInterests() {
        // grab all persons from DB except current person
        List<Person> allPersons = session.createQuery("from Person WHERE id!=:id", Person.class)
                .setParameter("id", this.person.getId()).getResultList();

        Map<Person, List<Tag>> resultList = new HashMap<>();
        Map<Long, List<Tag>> interests = new HashMap<>();

        for (Person p : allPersons) {
            List<Tag> tags = new ArrayList<>();
            for (Tag pt : p.getTags())
                for (Tag t : this.person.getTags())
                    if (t.equals(pt)) {
                        tags.add(t);
                        interests.put(p.getId(), tags);
                    }
        }

        int maxValueInMap = 0;
        for (Map.Entry<Long, List<Tag>> entry : interests.entrySet())
            if (maxValueInMap <= entry.getValue().size())
                maxValueInMap = entry.getValue().size();

        for (Map.Entry<Long, List<Tag>> entry : interests.entrySet())
            if (entry.getValue().size() == maxValueInMap)
                resultList.put(session.get(Person.class, entry.getKey()), entry.getValue());

        StringBuilder personsStrBuilder = new StringBuilder();

        if (!resultList.isEmpty()) {
            personsStrBuilder.append("Persons with common interests with ").append(this.person.getFullName()).append(": \n");
            int i = 1;
            for (Map.Entry<Person, List<Tag>> entry : resultList.entrySet()) {
                personsStrBuilder.append(i).append(". ").append("PERSON_ID: ").append(entry.getKey().getId()).append(", ")
                        .append("Name: ").append(entry.getKey().getFullName()).append(", ")
                        .append("Interests: ");
                for (Tag t : entry.getValue())
                    personsStrBuilder.append(t.getName()).append("(ID:").append(t.getId()).append("), ");

                personsStrBuilder.setLength(personsStrBuilder.length() - 2);
                personsStrBuilder.append(".\n");
                i++;
            }
        } else
            personsStrBuilder.append("No persons with common interests.\n");

        return personsStrBuilder.toString();
    }

    /**
     * Concatenate two lists(company and university recommendations)
     *
     * @return String
     */
    @Override
    public String getJobRecommendation() {
        return this.getCompanyRecommendation() + "\n*************************\n" + this.getUniversityRecommendations();
    }

    /**
     * A university recommendation should be generated for the person,
     * whereby the recommended university   should be in the same city where current person live and
     * at least one friend should already study there. TEST: 16492674416674
     *
     * @return String
     */
    private String getUniversityRecommendations() {
        StringBuilder universitiesStrBuilder = new StringBuilder();

        // grab all universities from DB
        List<University> allUniversities = session.createQuery("FROM University", University.class).getResultList();

        // remove from all universities where person already studies
        if (!this.person.getUniversitiesOfPerson().isEmpty())
            for (PersonUniversity pu : this.person.getUniversitiesOfPerson())
                allUniversities.remove(pu.getUniversity());

        // find all universities from a city where person live and at least one friend studies there
        // except the universities where person already studies
        for (University university : allUniversities)
            if (university.getCity().equals(this.person.getCity()) && this.isFriendStudies(university))
                universitiesStrBuilder.append(university.getName()).append(".\n");

        String universitiesResultString = universitiesStrBuilder.toString();
        if (!universitiesResultString.isEmpty())
            universitiesResultString = "\nUniversity recommendations: \n\n" + universitiesResultString;
        else
            universitiesResultString = "The person has no university recommendations";

        return universitiesResultString;
    }

    /**
     * A job recommendation should be generated for the person,
     * whereby the recommended company  should be in the same city as person company and
     * at least one friend should already work there.
     *
     * @return String
     */
    private String getCompanyRecommendation() {
        StringBuilder companiesBuilder = new StringBuilder();

        // grab all companies from DB
        List<Company> allCompanies = session.createQuery("FROM Company", Company.class).getResultList();

        // remove from all companies where person already works
        if (!this.person.getCompaniesOfPerson().isEmpty())
            for (PersonCompany pc : this.person.getCompaniesOfPerson())
                allCompanies.remove(pc.getCompany());

        // find all companies from a city where person live and at least one friend works there
        // except the companies where person already works
        for (Company company : allCompanies)
            if (company.getCountry().equals(this.person.getCity().getCountry()) && this.isFriendWorks(company))
                companiesBuilder.append(company.getName()).append(".\n");

        String companiesResultString = companiesBuilder.toString();
        if (!companiesResultString.isEmpty())
            companiesResultString = "\nCompany recommendations: \n\n" + companiesResultString;
        else
            companiesResultString = "The person has no company recommendations";

        return companiesResultString;
    }

    /**
     * Finds all friendship paths between this person and another person with the smallest distance
     *
     * @param friend person that is the target of the friendship path
     * @return String
     */
    @Override
    public String getShortestFriendshipPath(Person friend) {
        StringBuilder sb = new StringBuilder();

        // user-defined type for mapping bigint[] in PostgreSQL to Hibernate
        Type pathType = new CustomType(new PathArrayUserType());

        // get path from stored procedure
        Query procedureCall = session.createSQLQuery("SELECT path FROM shortest_friendship_path(:pid1, :pid2)")
                .setParameter("pid1", person.getId())
                .setParameter("pid2", friend.getId())
                // declare user-defined type for attribute 'path'
                .addScalar("path", pathType);

        // get person name from known person id
        Query idToNameQuery = session.createQuery("SELECT CONCAT(p.firstName, ' ', p.lastName) " +
                                                    "FROM Person p " +
                                                    "WHERE p.id = :id");

        List results = procedureCall.getResultList();
        if (results.isEmpty()) {
            return "Friendship path between ID " + person.getId() + " and ID " +
                    friend.getId() + " not found :(";

        } else { // at least one path exists
            Iterator iter = results.iterator();
            while (iter.hasNext()) {
                long[] pathArray = (long[]) iter.next();
                List<String> pathIds = new ArrayList<>();
                List<String> pathNames = new ArrayList<>();
                
                for (long id : pathArray) {
                    pathIds.add(String.valueOf(id));
                    // execute query that returns the name of the person with this id
                    String correspondingName = (String) idToNameQuery.setParameter("id", id).getSingleResult();
                    pathNames.add(correspondingName);
                }
                sb.append("\n")
                        .append(String.join(" -> ", pathIds))
                        .append("\n")
                        .append("(")
                        .append(String.join(" -> ", pathNames))
                        .append(")")
                        .append("\n");
            }
        }


        return sb.toString();
    }



    /**
     * Check if a friend studies at a recommended university
     *
     * @param university University
     * @return boolean
     */
    private boolean isFriendStudies(University university) {
        for (Person friend : this.person.getAllFriends())
            for (PersonUniversity pu : friend.getUniversitiesOfPerson())
                if (university.equals(pu.getUniversity()))
                    return true;

        return false;
    }

    /**
     * Check if a friend works at a recommended company
     *
     * @param company Company
     * @return boolean
     */
    private boolean isFriendWorks(Company company) {
        for (Person friend : this.person.getAllFriends())
            for (PersonCompany pc : friend.getCompaniesOfPerson())
                if (company.equals(pc.getCompany()))
                    return true;

        return false;
    }
}
