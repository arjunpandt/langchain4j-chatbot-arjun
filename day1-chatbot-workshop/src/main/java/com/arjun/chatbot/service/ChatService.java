package com.arjun.chatbot.service;

import com.arjun.chatbot.tools.ChatbotTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ChatService wraps the AI service and adds:
 * - Input validation
 * - Error handling / fallback responses
 *
 * Note: We manually build ChatAssistant here using AiServices.builder()
 * so we can attach memory and tools (the Spring Boot @AiService auto-scan
 * doesn't support wiring @Tool beans without explicit configuration in 0.31.0).
 */
@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private static final int MAX_INPUT_LENGTH = 1000;

    private final ChatAssistant assistant;

    public ChatService(GoogleAiGeminiChatModel geminiChatModel,
                       MessageWindowChatMemory chatMemory,
                       ChatbotTools chatbotTools) {
        // Build ChatAssistant programmatically — wires model + memory + tools
        this.assistant = AiServices.builder(ChatAssistant.class)
                .chatModel(geminiChatModel)
                .chatMemory(chatMemory)
                .tools(chatbotTools)
                .build();
    }

    /**
     * Main chat method — validates input, calls AI, handles errors.
     */
    public String chat(String userMessage) {

        // --- Input Validation ---
        if (userMessage == null || userMessage.isBlank()) {
            log.warn("Received empty or null user input.");
            return "⚠️ Please type a message. I'm here to help!";
        }

        if (userMessage.trim().length() > MAX_INPUT_LENGTH) {
            log.warn("User input exceeded maximum length ({} chars).", userMessage.length());
            return "⚠️ Your message is too long. Please keep it under 1000 characters.";
        }

        // --- AI Call with Error Handling ---
        try {
            log.info("User → {}", userMessage);
            String response = assistant.chat(userMessage.trim());
            log.info("Aria → {}", response);
            return response;

        } catch (Exception ex) {
            log.error("AI model call failed: {}", ex.getMessage(), ex);
            // --- Graceful Fallback Response ---
            return "🔴 Oops! I had trouble reaching the AI model. "
                    + "Please verify your API key in application.properties and try again. "
                    + "(Error: " + ex.getMessage() + ")";
        }
    }

    /**
     * Simulates an AI failure for demonstration / screenshot purposes.
     * Accessible via GET /api/chat/simulate-error
     */
    public String simulateError() {
        log.warn("Simulating AI model failure for demo purposes...");
        try {
            throw new RuntimeException("Simulated: API quota exceeded or model unreachable.");
        } catch (RuntimeException ex) {
            log.error("Simulated error caught: {}", ex.getMessage());
            return "🔴 Fallback triggered! The AI model is currently unavailable. "
                    + "Please try again later. (Simulated: " + ex.getMessage() + ")";
        }
    }
}
