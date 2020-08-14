package in.cubestack.navi;

import in.cubestack.navi.engine.CrawlerService;
import in.cubestack.navi.engine.NaviSearchEngine;
import in.cubestack.navi.engine.SearchService;
import in.cubestack.navi.index.SearchIndex;

import java.util.Optional;
import java.util.Scanner;

import static in.cubestack.navi.util.AssertionUtils.assertThat;

/**
 * This class is responsible of reading from console and writing to it.
 * It delegates all the core logic and execution to Input parser.
 */
public class Main {

    private InputParser inputParser;

    public static void main(String[] args) {
        new Main().executeCommandLine();
    }

    private void executeCommandLine() {
        initialize();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            parse(line);
        }
    }

    private void initialize() {
        SearchService searchService = new SearchService(new SearchIndex());
        NaviSearchEngine naviSearchEngine = new NaviSearchEngine(searchService);
        CrawlerService crawlerService = new CrawlerService(searchService);

        inputParser = new InputParser(naviSearchEngine, crawlerService);
    }

    private void parse(String line) {
        try {
            submitRequest(line);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void submitRequest(String line) {
        assertThat(line != null && !line.trim().isEmpty(), "No Input");
        String input = line.trim();

        Optional<String> response = inputParser.handleRequest(input);
        response.ifPresent(System.out::println);
    }
}
