package in.cubestack.navi.search;

import in.cubestack.navi.domain.Page;
import in.cubestack.navi.domain.PageKeyword;
import in.cubestack.navi.engine.SearchService;
import in.cubestack.navi.index.SearchIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SearchServiceTest {
    private SearchService searchService;
    private SearchIndex searchIndex;

    @Before
    public void init() {
        searchIndex = new SearchIndex();
        searchService = new SearchService(searchIndex);
    }

    @Test
    public void testEmpty() {
        searchService.submit(1, List.of());
        Assert.assertTrue(searchIndex.getResults("ANY").isEmpty());
    }

    @Test
    public void testSpaces() {
        searchService.submit(1, List.of("any  "));
        Assert.assertFalse(searchService.getSearchResultsFor(" ANY").isEmpty());
    }

    @Test
    public void testCaseInSensitive() {
        searchService.submit(1, List.of("any"));
        Assert.assertFalse(searchService.getSearchResultsFor("ANY").isEmpty());
    }

    @Test
    public void testWeight() {
        searchService.submit(1, List.of("first", "second", "Third"));

        Page result = searchService.getSearchResultsFor("first").iterator().next();
        List<PageKeyword> pageKeywords = result.getPageKeywords();

        Assert.assertEquals(pageKeywords.size(), 3);
        Assert.assertTrue(pageKeywords.stream().anyMatch(pk -> pk.getWeight() == 6));
        Assert.assertTrue(pageKeywords.stream().anyMatch(pk -> pk.getWeight() == 7));
        Assert.assertTrue(pageKeywords.stream().anyMatch(pk -> pk.getWeight() == 8));
    }

    @Test
    public void testInvalidSearch() {
        searchService.submit(1, List.of("any"));
        try {
            searchService.getSearchResultsFor("");
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertEquals(ex.getMessage(), "Search Keyword is empty.");
        }
    }

    @Test
    public void testSearchNull() {
        searchService.submit(1, List.of("any"));
        try {
            searchService.getSearchResultsFor(null);
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertEquals(ex.getMessage(), "Search Keyword is empty.");
        }
    }

    @Test
    public void testEmptyKeywords() {
        searchService.submit(1, Arrays.asList("any", "", null));
        Assert.assertFalse(searchService.getSearchResultsFor("ANY").isEmpty());
        Page result = searchService.getSearchResultsFor("ANY").iterator().next();

        Assert.assertEquals(result.getPageKeywords().size(), 1);
    }
}
