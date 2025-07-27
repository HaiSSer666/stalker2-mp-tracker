package com.stalker2tracker.controller;

import com.stalker2tracker.dto.StatusResponse;
import com.stalker2tracker.service.TrackerService;
import com.stalker2tracker.service.WebScraperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatusController.class)
class StatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackerService trackerService;

    @MockBean
    private WebScraperService webScraperService;

    @Test
    void getStatus_ShouldReturnStatusResponse() throws Exception {
        StatusResponse mockResponse = StatusResponse.builder()
                .stalker2ReleaseDate(LocalDate.of(2024, 11, 20))
                .mpReleased(false)
                .daysSinceStalkerRelease(68)
                .lastChecked(LocalDateTime.now())
                .latestNews(new ArrayList<>())
                .build();

        when(trackerService.getCurrentStatus()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mpReleased").value(false))
                .andExpect(jsonPath("$.daysSinceStalkerRelease").value(68));
    }

    @Test
    void triggerScrape_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(post("/api/scrape"))
                .andExpect(status().isOk())
                .andExpect(content().string("Scraping triggered successfully"));
    }
}