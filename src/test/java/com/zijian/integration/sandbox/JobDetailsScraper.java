package com.zijian.integration.sandbox;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class JobDetailsScraper {

    public static void main(String[] args) {
        String url = "https://www.linkedin.com/jobs/view/3947663383/?eBP=NOT_ELIGIBLE_FOR_CHARGING&refId=DexyRHTd7PxwlQJlKJAbTA%3D%3D&trackingId=XvsBcBWpGehYel8coM6yDg%3D%3D&trk=flagship3_search_srp_jobs";  // Replace with the actual URL
        int maxRetries = 1;
        int timeoutMillis = 30 * 1000;  // 30 seconds

        Document doc = null;
        for (int i = 0; i < maxRetries; i++) {
            try {
                // Fetch the HTML content from the URL with increased timeout
                doc = Jsoup.connect(url).timeout(timeoutMillis).get();
                break;
            } catch (IOException e) {
                System.out.println("Attempt " + (i + 1) + " failed: " + e.getMessage());
                if (i == maxRetries - 1) {
                    e.printStackTrace();
                    return;
                }
                try {
                    // Wait a bit before retrying
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                    return;
                }
            }
        }

        if (doc != null) {
            // Retrieve the element with id="job-details"
            Element jobDetailsElement = doc.getElementById("job-details");

            // Print the content of the job details element
            if (jobDetailsElement != null) {
                System.out.println("Job Details:");
                System.out.println(jobDetailsElement.text());
            } else {
                System.out.println("Element with id 'job-details' not found.");
            }
        }
    }
}
