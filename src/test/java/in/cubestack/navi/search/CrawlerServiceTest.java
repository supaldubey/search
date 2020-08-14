package in.cubestack.navi.search;

import in.cubestack.navi.domain.Page;
import in.cubestack.navi.engine.CrawlerService;
import in.cubestack.navi.engine.SearchService;
import in.cubestack.navi.index.SearchIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

public class CrawlerServiceTest {

    private CrawlerService crawlerService;
    private SearchService searchService;

    @Before
    public void init() {
        searchService = new SearchService(new SearchIndex());
        crawlerService = new CrawlerService(searchService);
    }

    @Test
    public void testEmpty() {
        try {
            crawlerService.indexSearchResult("");
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertEquals(ex.getMessage(), "Invalid input, page index should start with P. Provided ");
        }
    }

    @Test
    public void testNull() {
        try {
            crawlerService.indexSearchResult(null);
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertEquals(ex.getMessage(), "No input provided");
        }
    }

    @Test
    public void testSinglePage() {
        crawlerService.indexSearchResult("P Amazon FlipkaRT ");
        Set<Page> pageSearchResults = searchService.getSearchResultsFor("AMAZON");

        Assert.assertEquals(pageSearchResults.size(), 1);
        Page page = pageSearchResults.iterator().next();

        Assert.assertEquals(page.getPageKeywords().size(), 2);
        Assert.assertEquals(page.getId(), 1);
    }

    @Test
    public void testTooManyKeywords() {
        try {
            crawlerService.indexSearchResult("P a b c d e f g h i");
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertEquals(ex.getMessage(), "Weight of keyword i is less than zero");
        }
    }

    @Test
    public void testMultiplePages() {
        crawlerService.indexSearchResult("P Amazon FlipkaRT PayTm");
        crawlerService.indexSearchResult("P ZoomCar DriveZy");
        crawlerService.indexSearchResult("P SwiGGy DUnzo PayTm");

        Set<Page> pageSearchResults = searchService.getSearchResultsFor("PayTm");

        Iterator<Page> pageIterator = pageSearchResults.iterator();
        Assert.assertEquals(pageSearchResults.size(), 2);
        Page page = pageIterator.next();

        Assert.assertEquals(page.getPageKeywords().size(), 3);
        Assert.assertEquals(page.getId(), 1);

        Page nextPage = pageIterator.next();
        Assert.assertEquals(nextPage.getPageKeywords().size(), 3);
        Assert.assertEquals(nextPage.getId(), 3);
    }
}
