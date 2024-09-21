package com.example.integration.service;

import com.example.integration.model.Answer;
import com.example.integration.model.Question;
import com.knuddels.jtokkit.api.ModelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.autoconfigure.openai.OpenAiChatProperties;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AIService {

    private final OpenAiChatClient openAiChatClient;
    private final OpenAiChatProperties openAiChatProperties;

    @Autowired
    public AIService(OpenAiChatClient openAiChatClient, OpenAiChatProperties openAiChatProperties){
        this.openAiChatClient = openAiChatClient;
        this.openAiChatProperties = openAiChatProperties;
    }

    public Answer getAnswer(Question question, ModelType model) {
        OpenAiChatOptions openAiChatOptions = new OpenAiChatOptions.Builder(openAiChatProperties.getOptions())
                .withTemperature(0.6f)
                .withModel(model.getName())
                .build();

        PromptTemplate promptTemplate = new PromptTemplate(question.question());

        Prompt prompt = new Prompt(promptTemplate.createMessage(), openAiChatOptions);

        ChatResponse response;
        try {
            response = openAiChatClient.call(prompt);
            log.debug("Chat response received: {}", response);
        } catch (Exception e) {
            log.error("Error calling OpenAiChatClient", e);
            throw new RuntimeException("Failed to get response from AI", e);
        }

        //Message userMessage = new PromptTemplate(question.question()).createMessage();

        //Message systemMessage = new SystemPromptTemplate("You are a service that help me find suitable jobs based on my CV").createMessage();

        //var response = openAiChatClient.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));

        String answerContent = response.getResult().getOutput().getContent();

        return new Answer(answerContent);
    }
}


















