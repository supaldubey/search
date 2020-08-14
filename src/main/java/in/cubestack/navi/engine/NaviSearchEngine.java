package in.cubestack.navi.engine;

import in.cubestack.navi.domain.Page;
import in.cubestack.navi.search.SearchKeyword;
import in.cubestack.navi.search.SearchResult;
import in.cubestack.navi.search.SearchResultComparators;

import java.util.*;
import java.util.stream.Collectors;

import static in.cubestack.navi.Config.MAX_RESULTS_SIZE;
import static in.cubestack.navi.Config.MAX_WORD_WEIGHT_INDEX;

/**
 * The core service API, provides the capability of providing
 * sorted results for given keywords.
 */
public class NaviSearchEngine {

    private final SearchService searchService;

    public NaviSearchEngine(SearchService searchService) {
        this.searchService = searchService;
    }

    public List<SearchResult> getSearchResultsFor(List<String> keywords) {
        int weight = MAX_WORD_WEIGHT_INDEX;
        List<SearchKeyword> searchKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            if (keyword == null || keyword.trim().isEmpty()) {
                continue;
            }

            String searchKeyword = keyword.trim().toLowerCase();
            searchKeywords.add(new SearchKeyword(weight--, searchKeyword));
        }
        return getSearchResults(searchKeywords);
    }

    private List<SearchResult> getSearchResults(List<SearchKeyword> keywords) {
        Set<Page> pages = new HashSet<>();
        for (SearchKeyword searchKeyword : keywords) {
            pages.addAll(searchService.getSearchResultsFor(searchKeyword.getKeyword()));
        }
        return mergeResults(pages, keywords);
    }

    // Use a min Queue to store max *5* results till this point.
    // If the new results weight is higher, replace
    private List<SearchResult> mergeResults(Set<Page> pages, List<SearchKeyword> keywords) {
        PriorityQueue<SearchResult> searchResultPriorityQueue = new PriorityQueue<>(MAX_RESULTS_SIZE, SearchResultComparators.findLowestRankedComparator());
        for (Page page : pages) {
            processPageSearchResult(searchResultPriorityQueue, page, keywords);
        }

        return searchResultPriorityQueue.stream()
                .sorted(SearchResultComparators.findTopRankedComparator()).collect(Collectors.toList());
    }

    private void processPageSearchResult(PriorityQueue<SearchResult> searchResultPriorityQueue, Page page, List<SearchKeyword> keywords) {
        int rank = page.computeRankFor(keywords);
        SearchResult pageResult = new SearchResult(page, rank);

        if (searchResultPriorityQueue.size() < MAX_RESULTS_SIZE) {
            searchResultPriorityQueue.add(pageResult);
        } else {
            SearchResult topResult = searchResultPriorityQueue.peek();
            boolean rankedHigher = isPageRankedHigher(pageResult, topResult);
            if (rankedHigher) {
                searchResultPriorityQueue.poll();
                searchResultPriorityQueue.add(pageResult);
            }
        }
    }

    private boolean isPageRankedHigher(SearchResult pageResult, SearchResult topResult) {
        int betterMatch = SearchResultComparators.findLowestRankedComparator()
                .compare(topResult, pageResult);
        return betterMatch < 0;
    }
}
