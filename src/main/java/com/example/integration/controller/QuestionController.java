package com.example.integration.controller;
import com.example.integration.model.Answer;
import com.example.integration.model.Question;
import com.example.integration.service.AIService;
import com.knuddels.jtokkit.api.ModelType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final AIService AIService;

    @Value("${openai.model}")
    private String modelType;

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return AIService.getAnswer(question, ModelType.fromName(modelType).get());
    }

}
