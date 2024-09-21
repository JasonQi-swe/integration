package com.zijian.integration.sandbox;

import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

public class LanguageDetectionExample {
    public static void main(String[] args) {
        String chineseText = "这是一个中文句子。";
        String swedishText = "Detta är en svensk mening.";
        String english ="Tika can detect English, along with many other languages. To demonstrate this, we can use a simple Java program that utilizes Apache Tika's language detection capabilities. Below is an example of how to detect if a given text is in English using Apache Tika:";
        
        detectLanguage(chineseText);
        detectLanguage(swedishText);
        detectLanguage(english);
    }

    private static void detectLanguage(String text) {
        try {
            LanguageDetector detector = new OptimaizeLangDetector().loadModels();
            LanguageResult result = detector.detect(text);
            
            System.out.println("Text: " + text);
            System.out.println("Detected Language: " + result.getLanguage());
            System.out.println("Confidence: " + result.getRawScore());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
