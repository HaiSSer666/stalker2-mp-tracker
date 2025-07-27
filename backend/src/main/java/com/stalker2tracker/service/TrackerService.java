package com.stalker2tracker.service;

import com.stalker2tracker.dto.StatusResponse;
import com.stalker2tracker.entity.MpNews;
import com.stalker2tracker.entity.MpStatus;
import com.stalker2tracker.repository.MpNewsRepository;
import com.stalker2tracker.repository.MpStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackerService {
    
    private final MpStatusRepository mpStatusRepository;
    private final MpNewsRepository mpNewsRepository;
    
    private static final LocalDate STALKER2_RELEASE_DATE = LocalDate.of(2024, 11, 20);
    
    public StatusResponse getCurrentStatus() {
        MpStatus latestStatus = mpStatusRepository.findTopByOrderByCheckedAtDesc()
                .orElse(createDefaultStatus());
        
        List<MpNews> latestNews = mpNewsRepository.findTop5ByOrderByPublishedDateDescCreatedAtDesc();
        
        long daysSinceRelease = ChronoUnit.DAYS.between(STALKER2_RELEASE_DATE, LocalDate.now());
        
        List<StatusResponse.NewsItem> newsItems = latestNews.stream()
                .map(news -> StatusResponse.NewsItem.builder()
                        .title(news.getTitle())
                        .contentSnippet(news.getContentSnippet())
                        .url(news.getNewsUrl())
                        .publishedDate(news.getPublishedDate())
                        .build())
                .collect(Collectors.toList());
        
        return StatusResponse.builder()
                .stalker2ReleaseDate(STALKER2_RELEASE_DATE)
                .mpReleased(latestStatus.isReleased())
                .mpReleaseDate(latestStatus.getReleaseDate())
                .daysSinceStalkerRelease(daysSinceRelease)
                .lastChecked(latestStatus.getCheckedAt())
                .latestNews(newsItems)
                .build();
    }
    
    private MpStatus createDefaultStatus() {
        MpStatus status = new MpStatus();
        status.setReleased(false);
        status.setCheckedAt(LocalDate.now().atStartOfDay());
        return status;
    }
}