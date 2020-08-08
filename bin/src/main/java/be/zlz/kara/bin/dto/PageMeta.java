package be.zlz.kara.bin.dto;

public class PageMeta {

    private int page;

    private int totalItems;

    private int totalPages;

    private String nextPage;

    private String previousPage;

    public PageMeta(int page, int totalItems, int totalPages, String nextPage, String previousPage) {
        this.page = page;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
    }

    public int getPage() {
        return page;
    }

    public int getTotalItems() {
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
