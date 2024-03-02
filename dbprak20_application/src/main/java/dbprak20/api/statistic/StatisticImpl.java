package dbprak20.api.statistic;

import dbprak20.entity.Country;
import dbprak20.entity.TagClass;
import org.hibernate.Session;

import java.util.*;

public class StatisticImpl implements StatisticAPI {

    private final Session session;

    public StatisticImpl(Session session) {
        this.session = session;
    }

    /**
     * Calculates the hierarchical order of tag classes and
     * ouputs it with numbering
     *
     * @return String
     */
    @Override
    public String getTagClassHierarchy() {
        StringBuilder sb = new StringBuilder();
        // heading
        sb.append("\nTAG_CLASS HIERARCHY\n")
                .append("--------------------------\n");

        // retrieve all tag classes from DB
        List<TagClass> allTagClasses = session.createQuery("FROM TagClass", TagClass.class).getResultList();
        TagClass baseClass = null;
        // determine superclass of all tag classes
        for (TagClass tagClass: allTagClasses) {
            if (tagClass.getDirectSuperClasses() == null) {
                baseClass = tagClass;
                break;
            }
        }
        assert baseClass != null;
        // add base class on top of hierarchical order
        sb.append("0 ")
                .append(baseClass.getName())
                .append("\n");

        // will contain important details for every TagClass except base class
        Map<TagClass, int[]> tagClassMap = new LinkedHashMap<>();
        // key = TagClass object
        // value = int[] -> [0] = level of subclass, [1] index in hierarchy level
        tagClassDepthSearch(baseClass, 1, tagClassMap);


        for (Map.Entry<TagClass, int[]> tagClassEntry : tagClassMap.entrySet()) {
            TagClass tagClass = tagClassEntry.getKey();
            // get all index numbers of following superclasses -> form the numbering
            List<Integer> numbers = resolveIndexNumbers(tagClass, tagClassMap);
            // transform list of numbers to numbering String
            String numberingString = toHierarchicalNumbering(numbers);
            sb.append(numberingString)
                    .append(" ")
                    .append(tagClass.getName())
                    .append("\n");
        }

        return sb.toString();
    }

    /**
     * Finds all comments with at least <code>minLikes</code> likes
     * and outputs comment_id, creator name and number of likes
     *
     * @param minLikes minimum number of likes
     * @return String
     */
    @Override
    public String getPopularComments(long minLikes) {
        StringBuilder sb = new StringBuilder();
        // heading
        StringBuilder header = new StringBuilder();
        header.append("COMMENT_ID | CREATOR_NAME | #LIKES\n")
                .append("----------------------------------\n");

        // query that returns all commentIDs with its number of likes
        List results = session.createQuery("SELECT lc.comment.id AS id, COUNT(*) AS num_likes " +
                                                "FROM PersonLikesComment lc " +
                                                "GROUP BY id " +
                                                "HAVING COUNT(*) >= :k " +
                                                "ORDER BY num_likes DESC, id ASC")
                .setParameter("k", minLikes).getResultList();

        for (Object result : results) {
            // because the query selects multiple fields, the iterator contains an array of objects
            Object[] array = (Object[]) result;
            Long id = (Long) array[0];
            Long numLikes = (Long) array[1];
            // query that returns the name of the person who created this comment
            String creatorName = (String) session.createQuery("SELECT CONCAT(c.creator.firstName, ' ', c.creator.lastName) " +
                                                                "FROM Comment c " +
                                                                "WHERE c.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();

            // construct the output for every entry
            sb.append(id)
                    .append(" | ")
                    .append(creatorName)
                    .append(" | ")
                    .append(numLikes)
                    .append("\n");
        }

        String resultString = sb.toString();
        if (!resultString.isEmpty()) {
            resultString = "These are the most popular comments with a minimum of " + minLikes + " likes:\n\n" +
                    header.toString() +
                    resultString;
        } else {
            resultString = "Unfortunately, there are no comments matching your criteria :(\n";
        }

        return resultString;

    }

    /**
     * Finds the country, where the most messages (union of comments and posts) are from
     * and outputs the country name and the number of messages
     *
     * @return String
     */
    @Override
    public String getMostPostingCountry() {
        StringBuilder sb = new StringBuilder();

        int maxMessages = 0;
        Country mostPostingCountry = null;

        // retrieves all countries from DB
        List<Country> allCountries = session.createQuery("FROM Country", Country.class).getResultList();
        
        // find country where the most messages come from
        for (Country country: allCountries) {
            int numPosts = country.getPostsFromCountry().size();
            int numComments = country.getCommentsFromCountry().size();
            int numMessages = numPosts + numComments;
            if (numMessages > maxMessages) {
                maxMessages = numMessages;
                mostPostingCountry = country;
            }
        }

        assert mostPostingCountry != null;
        sb.append("COUNTRY: ")
                .append(mostPostingCountry.getName())
                .append(" #MESSAGES: ")
                .append(maxMessages);

        return sb.toString();
    }


    /**
     * Generates hierarchical structure using recursive iterations over all subclasses, starting at base class (depth search).
     * Out parameter <code>outputMap</code> contains all TagClasses and related information (depth in hierarchy, index number in that depth level)
     *
     * @param base calculated base/origin class of hierarchy
     * @param level depth in hierarchy
     * @param outputMap the returned map (necessary because of recursion)
     */
    private void tagClassDepthSearch(TagClass base, int level, Map<TagClass, int[]> outputMap) {
        int index = 0;
        // for every direct subclass of the base class
        for (TagClass subclass: base.getDirectSubClasses()) {
            index += 1;
            int[] value = new int[2];
            // store information about level of recursion
            value[0] = level;
            // store index of TagClass in this level
            value[1] = index;
            // add indexed TagClass to map
            outputMap.put(subclass, value);

            if (!subclass.getDirectSubClasses().isEmpty()) {
                // invoke recursive call with subclass as root
                tagClassDepthSearch(subclass, level + 1, outputMap);
            }

        }
    }


    /**
     * Finds all super classes of the current TagClass on the way to the baseClass (bottom-up).
     * Collects the indexes of all super classes in a list
     * @param current tag class
     * @param map generated by <code>getTagClassStructure(...)</code>
     * @return List
     */
    private List<Integer> resolveIndexNumbers(TagClass current, Map<TagClass, int[]> map) {
        List<Integer> result = new ArrayList<>();

        result.add(map.get(current)[1]);
        int currentLevel = map.get(current)[0];
        for (int i = currentLevel; i > 1; i--) {
            // add index of following superclass to result list
            current = current.getDirectSuperClasses();
            result.add(map.get(current)[1]);
        }

        return result;
    }

    /**
     * Transforms a list of integers to a numbering String
     * @param numbers generated by <code>resolveHierarchy(...)</code>
     * @return String representing the hierarchical numbering
     */
    private String toHierarchicalNumbering(List<Integer> numbers) {
        // reverses items in list in-place
        Collections.reverse(numbers);
        StringBuilder sb = new StringBuilder("0");
        for (int i = 0; i < numbers.size(); i++) {
            sb.append(".").append(numbers.get(i));
        }

        return sb.toString();
    }
}
