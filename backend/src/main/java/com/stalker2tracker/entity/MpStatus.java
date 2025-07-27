package com.stalker2tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mp_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MpStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "is_released", nullable = false)
    private boolean isReleased;
    
    @Column(name = "release_date")
    private LocalDate releaseDate;
    
    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}