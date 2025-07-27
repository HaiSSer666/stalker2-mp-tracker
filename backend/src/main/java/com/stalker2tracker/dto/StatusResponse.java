package com.stalker2tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    
    private LocalDate stalker2ReleaseDate;
    private boolean mpReleased;
    private LocalDate mpReleaseDate;
    private long daysSinceStalkerRelease;
    private LocalDateTime lastChecked;
    private List<NewsItem> latestNews;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsItem {
        private String title;
        private String contentSnippet;
        private String url;
        private LocalDate publishedDate;
    }
}