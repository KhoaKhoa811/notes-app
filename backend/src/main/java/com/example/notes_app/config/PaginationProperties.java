package com.example.notes_app.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "pagination")
public class PaginationProperties {
    private int defaultPage;
    private int defaultSize;
    private String defaultSortBy;
    private String defaultSortDir;
}
