package be.zlz.kara.bin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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

    @Value("${max.page.size}")
    public void setMaxPageSize(int size){
        maxPageSize = size;
    }
}
