package be.zlz.kara.bin.dto;

import java.util.List;

public class PagedList<T> {

    private List<T> items;

    private PageMeta meta;

    public PagedList(List<T> items, PageMeta meta) {
        this.items = items;
        this.meta = meta;
    }

    public List<T> getItems() {
        return items;
    }

    public PageMeta getMeta() {
        return meta;
    }
}
