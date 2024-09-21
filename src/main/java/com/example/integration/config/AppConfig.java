package com.example.integration.config;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.ModelType;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${openai.model}")
    private String model;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public LanguageDetector languageDetector() {
        return new OptimaizeLangDetector().loadModels();
    }

    @Bean
    public Encoding encodingRegistry() {
       return Encodings.newDefaultEncodingRegistry().getEncodingForModel(ModelType.fromName(model).get());
    }
}
