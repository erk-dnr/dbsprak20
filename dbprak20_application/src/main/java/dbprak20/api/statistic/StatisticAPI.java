package dbprak20.api.statistic;

public interface StatisticAPI {

    public String getTagClassHierarchy();
    public String getPopularComments(long minLikes);
    public String getMostPostingCountry();
}
