package in.cubestack.navi.search;

import java.util.Comparator;

public class SearchResultComparators {

    private static Comparator<SearchResult> nameComparator = Comparator.comparing((SearchResult sr) -> sr.getPage().getId()).reversed();

    public static Comparator<SearchResult> findLowestRankedComparator() {
        return Comparator.comparing(SearchResult::getRank)
                .thenComparing(nameComparator);
    }

    public static Comparator<SearchResult> findTopRankedComparator() {
        return Comparator.comparing(SearchResult::getRank).reversed()
                .thenComparing(sr -> sr.getPage().getId());
    }
}
