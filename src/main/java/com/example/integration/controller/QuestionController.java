package com.example.integration.controller;
import com.example.integration.model.Question;
import com.example.integration.model.Answer;
import com.example.integration.enumerator.AIModel;
import com.example.integration.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final AIService AIService;

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return AIService.getAnswer(question, AIModel.GPT_4o_mini);
    }

}
