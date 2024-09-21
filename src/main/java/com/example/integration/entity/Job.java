package com.example.integration.entity;

import com.example.integration.enumerator.ExperienceLevel;
import com.example.integration.enumerator.JobType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    private Long id;
    private String title;
    @Builder.Default
    private String state = "Unknown"; //maybe change to enum later

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;
    private String url;
    private String company;
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType type;
    private boolean closed;
    private String languageName;
    //workRemoteAllowed or workPlace
    private LocalDateTime listedAtDate;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime addedDateTime;

    private String source;
}
