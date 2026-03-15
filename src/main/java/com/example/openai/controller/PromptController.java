package com.example.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api")
public class PromptController
    {
    private final ChatClient chatClient;

    public PromptController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @Value("classpath:/templates/userPromptTemplate.st")
    Resource userPromptTemplate;

    @Value("classpath:/templates/systemPromptTemplate.st")
    Resource systemPromptTemplate;

    @GetMapping("/email")
    public String chat(@RequestParam("customerName") String customerName, @RequestParam("customerMessage") String customerMessage){
        return chatClient.prompt()
                .system("""
                        You are a professional customer service assistant which helps drafting email
                        responses to improve the productivity of the customer support team
                        """)
                .user(promptUserSpec -> {
                    promptUserSpec.text(userPromptTemplate).param("customerName",customerName)
                            .param("customerMessage", customerMessage);
                })
                .call().content();
    }


    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message){
        return chatClient.prompt()
                .system(systemPromptTemplate)
                .user(message)
                .call().content();
    }

    }
