package in.cubestack.navi.index;

import in.cubestack.navi.domain.Page;
import in.cubestack.navi.domain.PageKeyword;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The wrapper managing the core search results.
 *
 * The class builds and maintains search indexes.
 * Uses Hash Map internally to keep a track of keyword occurrence in a given page.
 */
public class SearchIndex {
    private Map<String, Set<Page>> searchResults = new HashMap<>();

    public void submit(PageKeyword pageKeyword) {
        searchResults.putIfAbsent(pageKeyword.getKeyword(), new HashSet<>());
        searchResults.get(pageKeyword.getKeyword()).add(pageKeyword.getPage());
    }

    public Set<Page> getResults(String keyword) {
        return searchResults.getOrDefault(keyword, new HashSet<>());
    }

}
