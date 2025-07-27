package com.stalker2tracker.repository;

import com.stalker2tracker.entity.MpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MpStatusRepository extends JpaRepository<MpStatus, Long> {
    
    Optional<MpStatus> findTopByOrderByCheckedAtDesc();
}