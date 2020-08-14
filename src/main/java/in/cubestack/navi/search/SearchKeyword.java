package in.cubestack.navi.search;

/**
 * Wrapper for a given search request.
 *
 * This is required to keep the track of the weightage which is attached to the keyword.
 */
public class SearchKeyword {
    private final int weight;
    private final String keyword;

    public SearchKeyword(int weight, String keyword) {
        this.weight = weight;
        this.keyword = keyword;
    }

    public int getWeight() {
        return weight;
    }

    public String getKeyword() {
        return keyword;
    }
}
