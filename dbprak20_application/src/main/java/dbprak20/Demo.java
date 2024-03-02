package dbprak20;

import dbprak20.api.personRelated.PersonRelatedImpl;
import dbprak20.api.statistic.StatisticImpl;
import dbprak20.entity.Person;
import dbprak20.util.HibernateUtil;
import dbprak20.util.Utility;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Demo {
    public static void main(String[] args) {
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            manageAPI(session);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (session != null)
                    session.close();
                HibernateUtil.shutdown();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * User interaction logic.
     * Choose any functionality from the APIs
     *
     * @param session current Session
     */
    public static void manageAPI(Session session) {
        long input;

        do {
            input = personOrStatistic();
            if (input == 1) {
                Person person;

                do {
                    long personID = Utility.validateInput(1, Long.MAX_VALUE, "Please type a person ID: ");
                    person = session.get(Person.class, personID);
                } while (person == null);

                PersonRelatedImpl personRelated = new PersonRelatedImpl(person, session);

                long inputPersonAPI = personApiChoice();

                if (inputPersonAPI == 0) {
                    input = 0;
                } else if (inputPersonAPI == 7) {
                    // back to API choice
                } else if (inputPersonAPI == 1) {
                    System.out.println(personRelated.getProfile());
                } else if (inputPersonAPI == 2) {
                    System.out.println(personRelated.getCommonInterestsOfMyFriends());
                } else if (inputPersonAPI == 3) {
                    Person friend = selectFriend(person, session);

                    System.out.println(personRelated.getCommonFriends(friend));
                } else if (inputPersonAPI == 4) {
                    System.out.println(personRelated.getPersonsWithMostCommonInterests());
                } else if (inputPersonAPI == 5) {
                    System.out.println(personRelated.getJobRecommendation());
                } else if (inputPersonAPI == 6) {
                    Person friend = selectFriend(person, session);

                    System.out.println(personRelated.getShortestFriendshipPath(friend));
                }

                System.out.println("\nWould you like to continue?\n1. Yes\n2. No\n");
                long exit = Utility.validateInput(1, 2, "");

                if (exit == 2)
                    input = 0;
            } else if (input == 2) {

                StatisticImpl statistic = new StatisticImpl(session);
                long inputStatisticAPI = statisticApiChoice();

                if (inputStatisticAPI == 0) {
                    input = 0;
                } else if (inputStatisticAPI == 4) {
                    // back to API choice
                } else if (inputStatisticAPI == 1) {
                        System.out.println(statistic.getTagClassHierarchy());
                } else if (inputStatisticAPI == 2) {
                        long k = Utility.validateInput(1, Long.MAX_VALUE, "Please type a numeric value for k: ");
                        System.out.println(statistic.getPopularComments(k));
                } else if (inputStatisticAPI == 3) {
                        System.out.println(statistic.getMostPostingCountry());
                }

                System.out.println("\nWould you like to continue?\n1. Yes\n2. No\n");
                long exit = Utility.validateInput(1, 2, "");

                if (exit == 2)
                    input = 0;

            }

        } while (input != 0);

        System.out.println("Goodbye!");
    }

    /**
     * Choice which functionality one would like to run in Statistic API
     *
     * @return long
     */
    private static long statisticApiChoice() {
        System.out.println("What statistics are you interested in?\n" +
                "1. Hierarchical numeration of the tag classes.\n" +
                "2. Comments with more than 'k' likes.\n" +
                "3. Country that is the origin of most posts and comments.\n" +
                "4. Back. \n" +
                "0. Exit.\n");
        return Utility.validateInput(0, 4, "");
    }

    /**
     * Choice which functionality one would like to run in Person API
     *
     * @return long
     */
    private static long personApiChoice() {
        System.out.println("What information are you interested in?\n" +
                "1. Output all personal information (name, gender, place of residence, etc).\n" +
                "2. Output of overlapping interests of the person and his friends.\n" +
                "3. Mutual friends.\n" +
                "4. Most similar interests of person and all other persons.\n" +
                "5. Job recommendations.\n" +
                "6. The shortest path regarding friendship relationships should be determined for a second person.\n" +
                "7. Back.\n" +
                "0. Exit.\n");
        return Utility.validateInput(0, 7, "");
    }

    /**
     * Choice which API one would like to run 1 - Person API, 2 - Statistic API
     *
     * @return long
     */
    private static long personOrStatistic() {
        System.out.println("Welcome to Social Network\n" +
                "What API would you like to run?\n" +
                "1. PersonRelatedAPI.\n" +
                "2. StatisticAPI.\n" +
                "0. Exit.\n");
        return Utility.validateInput(0, 2, "");
    }

    /**
     * Prompt user to enter ID of second person
     * and validate input
     *
     * @param me first person
     * @param session current session
     * @return second person
     */
    private static Person selectFriend(Person me, Session session) {
        Person friend;

        do {
            long friendID = Utility.validateInput(1, Long.MAX_VALUE, "Please type an ID for second person: ");

            if (friendID == me.getId()) {
                friend = null;
                System.out.println("The second person should be a different from first one");
            } else
                friend = session.get(Person.class, friendID);

        } while (friend == null);

        return friend;
    }
}
