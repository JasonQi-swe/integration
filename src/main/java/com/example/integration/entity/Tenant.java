package com.example.integration.entity;

import com.example.integration.enumerator.LocationEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String userName;

    @Column(columnDefinition = "LONGTEXT")
    private String summary;

    @Column(columnDefinition = "LONGTEXT")
    private String cv;

    @Column(columnDefinition = "LONGTEXT")
    private String coverLetter;

    private String searchingKeyWords;

    private String screeningSkills;

    private String containingSkills;

    private Integer totalTargetJobNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime addedDateTime;

    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime lastUpdatedTime;

    private String languageName;

    @Enumerated(EnumType.STRING)
    private LocationEnum location;

    private boolean needCoverLetter;

    private String requirementsForCoverLetter;
}
