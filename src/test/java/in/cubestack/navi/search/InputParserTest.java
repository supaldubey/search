package in.cubestack.navi.search;

import in.cubestack.navi.InputParser;
import in.cubestack.navi.domain.Page;
import in.cubestack.navi.engine.CrawlerService;
import in.cubestack.navi.engine.NaviSearchEngine;
import in.cubestack.navi.engine.SearchService;
import in.cubestack.navi.index.SearchIndex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// E2E Tests
public class InputParserTest {

    private static final String INPUT_FILE_LOCATION = "/input.txt";

    private SearchService searchService;
    private InputParser inputParser;
    private CountingCrawlerService crawlerService;

    @Before
    public void init() {
        searchService = new SearchService(new SearchIndex());
        NaviSearchEngine naviSearchEngine = new NaviSearchEngine(searchService);
        crawlerService = new CountingCrawlerService(searchService);
        inputParser = new InputParser(naviSearchEngine, crawlerService);
    }

    @Test
    public void testEmpty() {
        Set<Page> results = searchService.getSearchResultsFor("hello");

        Assert.assertTrue(crawlerService.parsedInputs.isEmpty());
        Assert.assertEquals(0, results.size());
    }

    @Test
    public void testInvalidInput() {
        inputParser.handleRequest("Hello line should not parse");
        Assert.assertTrue(crawlerService.parsedInputs.isEmpty());
    }

    @Test
    public void testSingle() {
        inputParser.handleRequest("P hello world");
        Optional<String> result = inputParser.handleRequest("Q hello");

        Assert.assertEquals(result.orElse(""), "Q1: P1");
        Assert.assertEquals(crawlerService.parsedInputs.get(0), "P hello world");
    }

    @Test
    public void testMultiple() {
        inputParser.handleRequest("P Ford Car Review");
        inputParser.handleRequest("P Review Car");
        Optional<String> result = inputParser.handleRequest("Q Ford");

        Assert.assertEquals(result.orElse(""), "Q1: P1");
    }

    @Test
    public void testEndToEnd() throws Exception {
        List<String> lines = Files.readAllLines(Path.of(getClass().getResource(INPUT_FILE_LOCATION).toURI()));

        List<String> outputs = lines.stream().map(inputParser::handleRequest)
                .filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());

        Assert.assertEquals(outputs.size(), 6);
        Assert.assertEquals(outputs.get(0), "Q1: P1 P3");
        Assert.assertEquals(outputs.get(1), "Q2: P6 P1 P2 P4 P5");
        Assert.assertEquals(outputs.get(2), "Q3: P2 P3 P1");
        Assert.assertEquals(outputs.get(3), "Q4: P3 P1 P2");
        Assert.assertEquals(outputs.get(4), "Q5: P1 P3 P6 P2 P4");
        Assert.assertEquals(outputs.get(5), "Q6: ");
    }
}

class CountingCrawlerService extends CrawlerService {
    public List<String> parsedInputs = new ArrayList<>();

    public CountingCrawlerService(SearchService searchService) {
        super(searchService);
    }

    @Override
    public void indexSearchResult(String input) {
        super.indexSearchResult(input);
        parsedInputs.add(input);
    }
}
