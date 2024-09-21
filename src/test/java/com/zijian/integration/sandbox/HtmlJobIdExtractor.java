package com.zijian.integration.sandbox;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlJobIdExtractor {

    public static List<String> extractJobIds(String filePath) {
        List<String> jobIds = new ArrayList<>();
        try {
            File input = new File(filePath);
            Document doc = Jsoup.parse(input, "UTF-8");

            Elements divElements = doc.select("div[data-job-id]");
            for (Element div : divElements) {
                String jobId = div.attr("data-job-id");
                jobIds.add(jobId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobIds;
    }

    public static void main(String[] args) {
        List<String> result = extractJobIds("");
        System.out.println(result);
        System.out.println(result.size());
    }
}
