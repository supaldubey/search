package in.cubestack.navi.search;

import in.cubestack.navi.engine.NaviSearchEngine;
import in.cubestack.navi.engine.SearchService;
import in.cubestack.navi.index.SearchIndex;
import in.cubestack.navi.search.SearchResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class NaviSearchEngineTest {
    private NaviSearchEngine naviSearchEngine;
    private SearchService searchService;

    @Before
    public void init() {
        searchService = new SearchService(new SearchIndex());
        naviSearchEngine = new NaviSearchEngine(searchService);
    }

    @Test
    public void testEmpty() {
        List<SearchResult> searchResults = naviSearchEngine.getSearchResultsFor(List.of("Test"));
        Assert.assertTrue(searchResults.isEmpty());
    }

    @Test
    public void testIndexed() {
        searchService.submit(1, List.of("Test"));

        List<SearchResult> searchResults = naviSearchEngine.getSearchResultsFor(List.of("Test"));
        Assert.assertFalse(searchResults.isEmpty());
    }

}
