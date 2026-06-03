package com.arjun.chatbot.config;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LangChain4j configuration.
 * Wires up the Gemini chat model with temperature, max tokens, etc.
 */
@Configuration
public class LangChainConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model.name:gemini-2.5-flash}")
    private String modelName;

    @Value("${gemini.model.temperature:0.7}")
    private double temperature;

    @Value("${gemini.model.max-tokens:512}")
    private int maxTokens;

    /**
     * Primary chat model bean — connected to Google Gemini.
     * Temperature=0.7 → balanced creativity vs. accuracy.
     * MaxOutputTokens=512 → keeps responses concise.
     */
    @Bean
    public GoogleAiGeminiChatModel geminiChatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxOutputTokens(maxTokens)
                .build();
    }

    /**
     * Chat memory — keeps the last 10 messages so the bot remembers context.
     */
    @Bean
    public MessageWindowChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }
}
