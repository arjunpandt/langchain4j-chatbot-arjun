package com.arjun.chatbot.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.arjun.chatbot.tools.ChatbotTools;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChatServiceTest {

    @MockBean
    GoogleAiGeminiChatModel geminiChatModel;

    @Autowired
    ChatbotTools chatbotTools;

    @Test
    void calculator_addition_works() {
        String result = chatbotTools.calculate("10 + 5");
        assertThat(result).contains("15.00");
    }

    @Test
    void calculator_division_by_zero_returns_error() {
        String result = chatbotTools.calculate("10 / 0");
        assertThat(result).containsIgnoringCase("division by zero");
    }

    @Test
    void product_lookup_returns_laptop_details() {
        String result = chatbotTools.getProductDetails("laptop");
        assertThat(result).containsIgnoringCase("laptop");
        assertThat(result).contains("$999");
    }

    @Test
    void product_lookup_unknown_returns_not_found() {
        String result = chatbotTools.getProductDetails("spaceship");
        assertThat(result).containsIgnoringCase("not found");
    }

    @Test
    void getCurrentDateTime_returns_non_null() {
        String result = chatbotTools.getCurrentDateTime();
        assertThat(result).isNotNull().isNotBlank();
        assertThat(result).containsIgnoringCase("Current date and time");
    }
}
