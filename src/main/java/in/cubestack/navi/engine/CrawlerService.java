package in.cubestack.navi.engine;

import java.util.Arrays;
import java.util.List;

import static in.cubestack.navi.util.AssertionUtils.assertThat;

/**
 * The external API search engine crawler, submits the crawled results to service to index.
 */
public class CrawlerService {
    private int pageIndex = 1;

    private final SearchService searchService;

    public CrawlerService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void indexSearchResult(String input) {
        assertThat(input != null, "No input provided");
        String[] inputs = input.split(" ");
        assertThat(inputs[0].equalsIgnoreCase("P"), "Invalid input, page index should start with P. Provided " + input);

        List<String> inputKeywords = Arrays.asList(Arrays.copyOfRange(inputs, 1, inputs.length));
        searchService.submit(pageIndex++, inputKeywords);
    }
}
