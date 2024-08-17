package com.example.integration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "application", uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "tenant_id"}))
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(columnDefinition = "LONGTEXT")
    private String coverLetter;

    private LocalDate localDate;

    @Column(columnDefinition = "LONGTEXT")
    private String reasonToSelect;
}
