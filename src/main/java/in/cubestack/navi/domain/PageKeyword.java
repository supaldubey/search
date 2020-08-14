package in.cubestack.navi.domain;

/**
 * Maintains mapping between page and the keyword along with weight.
 * Can be used to further add more metadata in future.
 */
public class PageKeyword {
    private final Page page;
    private final String keyword;
    private final int weight;

    public PageKeyword(Page page, String keyword, int weight) {
        this.page = page;
        this.keyword = keyword;
        this.weight = weight;
    }

    public Page getPage() {
        return page;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getWeight() {
        return weight;
    }
}
