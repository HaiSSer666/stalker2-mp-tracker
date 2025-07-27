package com.stalker2tracker.service;

import com.stalker2tracker.entity.MpNews;
import com.stalker2tracker.entity.MpStatus;
import com.stalker2tracker.repository.MpNewsRepository;
import com.stalker2tracker.repository.MpStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebScraperService {
    
    private final MpStatusRepository mpStatusRepository;
    private final MpNewsRepository mpNewsRepository;
    
    private static final String NEWS_URL = "https://www.stalker2.com/de/news";
    private static final List<String> MP_KEYWORDS = List.of(
        "multiplayer", "mp", "multi-player", "online", "coop", "co-op", 
        "pvp", "multiplayer mode", "online mode", "mehrspieler"
    );
    private static final Pattern MP_RELEASE_PATTERN = Pattern.compile(
        "(multiplayer|mp|online).*(released|available|launch|verfügbar|veröffentlicht)",
        Pattern.CASE_INSENSITIVE
    );
    
    @Transactional
    public void scrapeAndUpdateStatus() {
        log.info("Starting web scraping for STALKER 2 MP news...");
        
        try {
            Document doc = Jsoup.connect(NEWS_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(30000)
                    .get();
            
            Elements newsArticles = doc.select("article, .news-item, .post, [class*='news'], [class*='article']");
            
            if (newsArticles.isEmpty()) {
                newsArticles = doc.select("div").stream()
                        .filter(div -> containsNewsContent(div))
                        .collect(Elements::new, Elements::add, Elements::addAll);
            }
            
            log.info("Found {} potential news articles", newsArticles.size());
            
            List<MpNews> mpRelatedNews = new ArrayList<>();
            boolean mpReleased = false;
            LocalDate mpReleaseDate = null;
            
            for (Element article : newsArticles) {
                String title = extractTitle(article);
                String content = extractContent(article);
                String url = extractUrl(article, doc);
                LocalDate publishedDate = extractDate(article);
                
                if (title == null || content == null) {
                    continue;
                }
                
                String fullText = (title + " " + content).toLowerCase();
                
                if (containsMpKeywords(fullText)) {
                    log.info("Found MP-related article: {}", title);
                    
                    if (!mpNewsRepository.existsByNewsUrl(url)) {
                        MpNews news = new MpNews();
                        news.setTitle(title);
                        news.setContentSnippet(content.length() > 300 ? 
                                content.substring(0, 300) + "..." : content);
                        news.setNewsUrl(url);
                        news.setPublishedDate(publishedDate != null ? publishedDate : LocalDate.now());
                        news.setScrapedAt(LocalDateTime.now());
                        
                        mpRelatedNews.add(news);
                    }
                    
                    if (MP_RELEASE_PATTERN.matcher(fullText).find()) {
                        mpReleased = true;
                        if (publishedDate != null && mpReleaseDate == null) {
                            mpReleaseDate = publishedDate;
                        }
                    }
                }
            }
            
            mpNewsRepository.saveAll(mpRelatedNews);
            log.info("Saved {} new MP-related news articles", mpRelatedNews.size());
            
            MpStatus status = new MpStatus();
            status.setReleased(mpReleased);
            status.setReleaseDate(mpReleaseDate);
            status.setCheckedAt(LocalDateTime.now());
            mpStatusRepository.save(status);
            
            log.info("Status updated - MP Released: {}", mpReleased);
            
        } catch (IOException e) {
            log.error("Error scraping website: {}", e.getMessage(), e);
        }
    }
    
    private boolean containsNewsContent(Element element) {
        String text = element.text().toLowerCase();
        return text.contains("stalker") && 
               (text.contains("news") || text.contains("update") || text.contains("announcement"));
    }
    
    private String extractTitle(Element article) {
        Elements titleElements = article.select("h1, h2, h3, h4, .title, [class*='title'], [class*='headline']");
        return titleElements.isEmpty() ? null : titleElements.first().text().trim();
    }
    
    private String extractContent(Element article) {
        Elements contentElements = article.select("p, .content, .description, [class*='content'], [class*='text']");
        StringBuilder content = new StringBuilder();
        for (Element elem : contentElements) {
            content.append(elem.text()).append(" ");
        }
        return content.toString().trim();
    }
    
    private String extractUrl(Element article, Document doc) {
        Elements links = article.select("a[href]");
        for (Element link : links) {
            String href = link.attr("abs:href");
            if (!href.isEmpty()) {
                return href;
            }
        }
        return doc.location();
    }
    
    private LocalDate extractDate(Element article) {
        Elements dateElements = article.select("time, .date, [class*='date'], [datetime]");
        
        for (Element dateElem : dateElements) {
            String dateStr = dateElem.hasAttr("datetime") ? 
                    dateElem.attr("datetime") : dateElem.text();
            
            LocalDate date = tryParseDate(dateStr);
            if (date != null) {
                return date;
            }
        }
        
        return null;
    }
    
    private LocalDate tryParseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        List<DateTimeFormatter> formatters = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
        
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr.trim(), formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }
        
        return null;
    }
    
    private boolean containsMpKeywords(String text) {
        String lowerText = text.toLowerCase();
        return MP_KEYWORDS.stream().anyMatch(lowerText::contains);
    }
}