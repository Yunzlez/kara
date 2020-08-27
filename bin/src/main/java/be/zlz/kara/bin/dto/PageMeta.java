package be.zlz.kara.bin.dto;

public class PageMeta {

    private final int page;

    private final long totalItems;

    private final int totalPages;

    private final String nextPage;

    private final String previousPage;

    public PageMeta(int page, long totalItems, int totalPages, String nextPage, String previousPage) {
        this.page = page;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
    }

    public int getPage() {
        return page;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getNextPage() {
        return nextPage;
    }

    public String getPreviousPage() {
        return previousPage;
    }
}
