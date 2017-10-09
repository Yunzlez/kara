package be.zlz.zlzbin.api.util;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaginationWrapper<T> {

    private Page<T> page;

    private List<String> pageNumbers;

    public PaginationWrapper(Page<T> page) {
        this.page = page;

        pageNumbers = new ArrayList<>();

        Integer max = 6;

        if(max < 5){
            for (int i = 0; i < page.getTotalPages(); i++) {
                pageNumbers.add(String.valueOf(i));
            }
        }

        else if(page.isFirst()){
            for (int i = 0; i < max; i++) {
                if(i == Math.round(max/2)){
                    pageNumbers.add("...");
                    continue;
                }
                pageNumbers.add(String.valueOf(i));
            }
        }

        else if(page.isLast()){
            for (int i = page.getTotalPages(); i < page.getTotalPages()-max; i--) {
                if(i == page.getTotalPages() - Math.round(max/2)){
                    pageNumbers.add("...");
                    continue;
                }
                pageNumbers.add(String.valueOf(i));
            }
            Collections.reverse(pageNumbers);
        }

        else {

        }

    }


}
