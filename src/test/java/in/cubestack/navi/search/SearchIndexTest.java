package in.cubestack.navi.search;

import in.cubestack.navi.domain.Page;
import in.cubestack.navi.domain.PageKeyword;
import in.cubestack.navi.index.SearchIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class SearchIndexTest {
    private SearchIndex searchIndex;

    @Before
    public void init() {
        searchIndex = new SearchIndex();
    }

    @Test
    public void testEmpty() {
        Assert.assertTrue(searchIndex.getResults("OK").isEmpty());
    }

    @Test
    public void testSingleResult() {
        searchIndex.submit(new PageKeyword(new Page(1), "Ok", 1));

        Set<Page> searchResults = searchIndex.getResults("Ok");
        Assert.assertEquals(1, searchResults.size());
        Assert.assertEquals(searchResults.toArray(new Page[0])[0].getId(), 1);
    }

    @Test
    public void testSingleResultNonMatchingKeyword() {
        searchIndex.submit(new PageKeyword(new Page(1), "Ok", 1));

        Set<Page> searchResults = searchIndex.getResults("Hello");
        Assert.assertTrue(searchResults.isEmpty());
    }
}
