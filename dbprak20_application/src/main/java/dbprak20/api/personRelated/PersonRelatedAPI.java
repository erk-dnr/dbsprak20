package dbprak20.api.personRelated;

import dbprak20.entity.Person;

public interface PersonRelatedAPI {
    String getProfile();

    String getCommonInterestsOfMyFriends();

    String getCommonFriends(Person friend);

    String getPersonsWithMostCommonInterests();

    String getJobRecommendation();

    String getShortestFriendshipPath(Person friend);
}
