package com.example.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String url;
    private String company;
    private String location;
    private String type; //maybe change to enum later,  full-time, part-time, contract, internship
    private boolean closed;
    private String languageName;
    //workRemoteAllowed or workPlace
    private LocalDateTime listedAtDate;

    @Builder.Default
    private String formattedExperienceLevel = "Unknown"; // maybe change to enum later

    private LocalDate addedDate;
    private String source;
}
