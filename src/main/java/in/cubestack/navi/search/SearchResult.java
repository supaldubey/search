package in.cubestack.navi.search;

import in.cubestack.navi.domain.Page;

/**
 * Search Result class, used to keep track of weighted results.
 *
 * For given page and given keyword, this class keeps the rank of page within the results.
 */
public class SearchResult {
    private final Page page;
    private final int rank;

    public SearchResult(Page page, int rank) {
        this.page = page;
        this.rank = rank;
    }

    public Page getPage() {
        return page;
    }

    public int getRank() {
        return rank;
    }

    public String getResponse() {
        return page.getName();
    }
}
