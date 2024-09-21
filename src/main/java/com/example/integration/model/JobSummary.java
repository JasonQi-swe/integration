package com.example.integration.model;

import com.example.integration.entity.Job;
import com.example.integration.enumerator.JobType;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class JobSummary {
    private String id;
    private String title;
    private String url;
    private String referenceId;
    private String posterId;
    private Company company;
    private String location;
    private String type;
    private String postDate;
    private String benefits;

    public Job convertToJob() {
        Job job = new Job();
        job.setId(Long.valueOf(this.id));
        job.setTitle(this.getTitle());
        job.setUrl(this.getUrl());
        job.setCompany(this.getCompany() != null ? this.getCompany().getName() : null);
        job.setLocation(this.getLocation());
        job.setType(JobType.valueOf(this.getType()));
        job.setListedAtDate(parsePostDate(this.getPostDate()));
        job.setState("UNKNOWN");
        job.setDescription("No description");

        job.setClosed(false);
        return job;
    }

    private LocalDateTime parsePostDate(String postDate) {
        if (postDate.equalsIgnoreCase("now")) {
            return LocalDateTime.now();
        } else if (postDate.matches("\\d+\\s+\\w+\\s+ago")) {
            return parseRelativeDate(postDate);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(postDate, formatter);
        }
    }

    private LocalDateTime parseRelativeDate(String relativeDate) {
        Pattern pattern = Pattern.compile("(\\d+)\\s+(\\w+)\\s+ago");
        Matcher matcher = pattern.matcher(relativeDate);
        if (matcher.find()) {
            int amount = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2).toLowerCase();
            LocalDateTime now = LocalDateTime.now();

            switch (unit) {
                case "minute":
                case "minutes":
                    return now.minus(amount, ChronoUnit.MINUTES);
                case "hour":
                case "hours":
                    return now.minus(amount, ChronoUnit.HOURS);
                case "day":
                case "days":
                    return now.minus(amount, ChronoUnit.DAYS);
                case "week":
                case "weeks":
                    return now.minus(amount, ChronoUnit.WEEKS);
                case "month":
                case "months":
                    return now.minus(amount, ChronoUnit.MONTHS);
                case "year":
                case "years":
                    return now.minus(amount, ChronoUnit.YEARS);
                default:
                    throw new IllegalArgumentException("Unknown time unit: " + unit);
            }
        } else {
            throw new IllegalArgumentException("Invalid relative date format: " + relativeDate);
        }
    }
}
