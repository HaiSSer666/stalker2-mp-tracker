package com.stalker2tracker.config;

import com.stalker2tracker.service.WebScraperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    
    private final WebScraperService webScraperService;
    
    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        log.info("Running initial scrape on application startup...");
        webScraperService.scrapeAndUpdateStatus();
    }
    
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledScrape() {
        log.info("Running scheduled daily scrape...");
        webScraperService.scrapeAndUpdateStatus();
    }
    
    @Scheduled(cron = "0 0 */6 * * ?")
    public void frequentScrape() {
        log.info("Running 6-hourly scrape check...");
        webScraperService.scrapeAndUpdateStatus();
    }
}