package be.zlz.kara.bin.util;

import be.zlz.kara.bin.dto.PageMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PagingUtils {

    private static int maxPageSize;

    public static Pageable getPageable(Integer page, Integer size) {
        if (size == null || size > maxPageSize || size == 0) {
            size = maxPageSize;
        }
        if (page == null) {
            page = 0;
        }
        return PageRequest.of(page, size);
    }

    public static PageMeta createPageMeta(int currentPage, long totalItems, int totalPages, String currentPath, int limit) {
        return new PageMeta(
                currentPage,
                totalItems,
                totalPages,
                currentPage != totalPages-1 ? pathWithParams(currentPath,  currentPage + 1, limit) : null,
                currentPage != 0 ? pathWithParams(currentPath, currentPage - 1, limit) : null
        );
    }

    private static String pathWithParams(String path, int page, int limit) {
        return UriComponentsBuilder.fromPath(path)
                .queryParam("page", page)
                .queryParam("limit", limit)
                .toUriString();
    }

    @Value("${max.page.size}")
    public void setMaxPageSize(int size){
        maxPageSize = size;
    }
}
