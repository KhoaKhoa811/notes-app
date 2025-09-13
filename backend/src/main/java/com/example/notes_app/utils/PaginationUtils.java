package com.example.notes_app.utils;

import com.example.notes_app.config.PaginationProperties;
import com.example.notes_app.dto.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtils {
    public static Pageable createPageable(
            PaginationRequest request,
            PaginationProperties paginationProperties
    ) {
        int actualPage = request.getPage() != null ? request.getPage() : paginationProperties.getDefaultPage();
        int actualSize = request.getSize() != null ? request.getSize() : paginationProperties.getDefaultSize();
        String actualSortBy = request.getSortBy() != null ? request.getSortBy() : paginationProperties.getDefaultSortBy();
        String actualSortDir = request.getSortDir() != null ? request.getSortDir() : paginationProperties.getDefaultSortDir();

        Sort sort = actualSortDir.equalsIgnoreCase("desc") ?
                Sort.by(actualSortBy).descending() :
                Sort.by(actualSortBy).ascending();

        return PageRequest.of(actualPage, actualSize, sort);
    }
}
