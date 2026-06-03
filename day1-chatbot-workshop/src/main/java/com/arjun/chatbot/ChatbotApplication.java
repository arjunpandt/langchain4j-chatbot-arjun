package com.arjun.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  LangChain4j Chatbot is running!");
        System.out.println("  Visit: http://localhost:8080");
        System.out.println("========================================\n");
    }
}
