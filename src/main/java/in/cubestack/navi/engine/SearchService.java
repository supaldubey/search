package in.cubestack.navi.engine;

import in.cubestack.navi.domain.Page;
import in.cubestack.navi.index.SearchIndex;

import java.util.List;
import java.util.Set;

import static in.cubestack.navi.Config.MAX_RANK_INDEX;
import static in.cubestack.navi.util.AssertionUtils.assertThat;

/**
 * Base internal service responsible to maintain the search index.
 */
public class SearchService {
    private final SearchIndex searchIndex;

    public SearchService(SearchIndex searchIndex) {
        this.searchIndex = searchIndex;
    }

    public void submit(int pageIndex, List<String> keywords) {
        Page page = new Page(pageIndex);
        int keywordWeight = MAX_RANK_INDEX;
        for (String keyword : keywords) {
            page.addKeyword(keyword, keywordWeight--);
        }

        page.getPageKeywords().forEach(searchIndex::submit);
    }

    public Set<Page> getSearchResultsFor(String keyword) {
        assertThat(keyword != null && keyword.trim().length() > 0, "Search Keyword is empty.");
        String persistedKeyword = keyword.trim().toLowerCase();

        return searchIndex.getResults(persistedKeyword);
    }
}
