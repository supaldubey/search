package in.cubestack.navi.domain;

import in.cubestack.navi.search.SearchKeyword;

import java.util.*;

import static in.cubestack.navi.util.AssertionUtils.assertThat;

/**
 * Responsible for holding metadata about the Page.
 * It maintains list of keywords page has, and the corresponding weight of the keyword
 */
public class Page {
    private final int id;
    private Map<String, PageKeyword> pageKeywords = new HashMap<>();

    public Page(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<PageKeyword> getPageKeywords() {
        return new ArrayList<>(pageKeywords.values());
    }

    public void addKeyword(String keyword, int weight) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }

        assertThat(weight > 0, String.format("Weight of keyword %s is less than zero", keyword));

        String persistedKeyword = keyword.trim().toLowerCase();
        pageKeywords.put(persistedKeyword, new PageKeyword(this, persistedKeyword, weight));
    }

    public int computeRankFor(List<SearchKeyword> searchKeywords) {
        int rank = 0;
        for (SearchKeyword searchKeyword : searchKeywords) {
            String keyword = searchKeyword.getKeyword();
            if (pageKeywords.containsKey(keyword)) {
                PageKeyword pageKeyword = pageKeywords.get(keyword);
                rank += pageKeyword.getWeight() * searchKeyword.getWeight();
            }
        }
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return id == page.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return String.format("P%s", id);
    }
}
