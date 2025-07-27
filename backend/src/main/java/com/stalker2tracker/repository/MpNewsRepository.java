package com.stalker2tracker.repository;

import com.stalker2tracker.entity.MpNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MpNewsRepository extends JpaRepository<MpNews, Long> {
    
    Optional<MpNews> findTopByOrderByPublishedDateDescCreatedAtDesc();
    
    List<MpNews> findTop5ByOrderByPublishedDateDescCreatedAtDesc();
    
    boolean existsByNewsUrl(String newsUrl);
}