package com.stalker2tracker.controller;

import com.stalker2tracker.dto.StatusResponse;
import com.stalker2tracker.service.TrackerService;
import com.stalker2tracker.service.WebScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class StatusController {
    
    private final TrackerService trackerService;
    private final WebScraperService webScraperService;

    public StatusController(TrackerService trackerService, WebScraperService webScraperService) {
        this.trackerService = trackerService;
        this.webScraperService = webScraperService;
    }
    
    @GetMapping("/status")
    public ResponseEntity<StatusResponse> getStatus() {
        return ResponseEntity.ok(trackerService.getCurrentStatus());
    }
    
    @PostMapping("/scrape")
    public ResponseEntity<String> triggerScrape() {
        webScraperService.scrapeAndUpdateStatus();
        return ResponseEntity.ok("Scraping triggered successfully");
    }
}