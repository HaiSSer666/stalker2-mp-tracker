package com.stalker2tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mp_news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MpNews {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "content_snippet", columnDefinition = "TEXT")
    private String contentSnippet;
    
    @Column(name = "news_url", nullable = false)
    private String newsUrl;
    
    @Column(name = "published_date")
    private LocalDate publishedDate;
    
    @Column(name = "scraped_at", nullable = false)
    private LocalDateTime scrapedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}