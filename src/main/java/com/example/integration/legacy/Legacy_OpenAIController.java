package com.example.integration.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class Legacy_OpenAIController {

    private static final Logger logger = LoggerFactory.getLogger(Legacy_OpenAIController.class);

    private final Legacy_OpenAIService openAIService;

    public Legacy_OpenAIController(Legacy_OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> getHistory() {
        logger.info("test");
        return new ResponseEntity<>("done", HttpStatus.OK);
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generateText(@RequestParam String prompt) {
        logger.info("entered generateText");
        String result = openAIService.generateText(prompt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
