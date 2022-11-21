package com.example.authmoduls.common.decorator;

import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterSortRequest<FILTER, SORT> {
    FILTER filter;
    SortRequest<SORT> sort;
    Pagination page;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SortRequest<SORT> {
        SORT sortBy;
        Sort.Direction orderBy;
    }
}
