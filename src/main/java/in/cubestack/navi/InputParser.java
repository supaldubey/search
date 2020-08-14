package in.cubestack.navi;

import in.cubestack.navi.engine.CrawlerService;
import in.cubestack.navi.engine.NaviSearchEngine;
import in.cubestack.navi.search.SearchResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class handles request from external sources.
 *
 * It is the core entry point for the line by line building of search index and querying on the current index.
 */
public class InputParser {
    private final NaviSearchEngine naviSearchEngine;
    private final CrawlerService crawlerService;

    private int queryIndex = 1;

    public InputParser(NaviSearchEngine naviSearchEngine, CrawlerService crawlerService) {
        this.naviSearchEngine = naviSearchEngine;
        this.crawlerService = crawlerService;
    }

    public Optional<String> handleRequest(String input) {
        try {
            return doHandleRequest(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<String> doHandleRequest(String input) {
        if (input.toLowerCase().startsWith("p")) {
            crawlerService.indexSearchResult(input);
        } else if (input.toLowerCase().startsWith("q")) {
            return Optional.of(handleSearch(input.trim()));
        } else {
            throw new RuntimeException("Invalid input, should start with either p keyword or Q keyword");
        }
        return Optional.empty();
    }

    private String handleSearch(String input) {
        String[] query = input.split(" ");

        List<String> keywords = Arrays.asList(Arrays.copyOfRange(query, 1, query.length));
        List<SearchResult> results = naviSearchEngine.getSearchResultsFor(keywords);

        String resultPages = results.stream().map(SearchResult::getResponse).collect(Collectors.joining(" "));
        return String.format("Q%s: %s", queryIndex++, resultPages);
    }
}
