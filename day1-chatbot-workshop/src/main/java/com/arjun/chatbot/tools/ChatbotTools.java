package com.arjun.chatbot.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom tools that the AI can call automatically.
 * LangChain4j detects @Tool methods and invokes them when the user asks for relevant info.
 */
@Component
public class ChatbotTools {

    /**
     * Tool 1: Calculator
     * The AI will call this when the user asks to calculate something.
     */
    @Tool("Calculate the result of a simple math expression. Input must be in format: num1 operator num2. Supported operators: +, -, *, /")
    public String calculate(String expression) {
        try {
            expression = expression.trim();
            String[] parts;
            double a, b, result;

            if (expression.contains("+")) {
                parts = expression.split("\\+");
                a = Double.parseDouble(parts[0].trim());
                b = Double.parseDouble(parts[1].trim());
                result = a + b;
            } else if (expression.contains("-")) {
                parts = expression.split("-");
                a = Double.parseDouble(parts[0].trim());
                b = Double.parseDouble(parts[1].trim());
                result = a - b;
            } else if (expression.contains("*")) {
                parts = expression.split("\\*");
                a = Double.parseDouble(parts[0].trim());
                b = Double.parseDouble(parts[1].trim());
                result = a * b;
            } else if (expression.contains("/")) {
                parts = expression.split("/");
                a = Double.parseDouble(parts[0].trim());
                b = Double.parseDouble(parts[1].trim());
                if (b == 0) return "Error: Division by zero is not allowed.";
                result = a / b;
            } else {
                return "Invalid expression. Use format: 10 + 5";
            }

            return String.format("Result of [ %s ] = %.2f", expression, result);
        } catch (Exception e) {
            return "Could not parse the expression: " + expression;
        }
    }

    /**
     * Tool 2: Current Date & Time
     * The AI calls this when the user asks about current time/date.
     */
    @Tool("Get the current date and time.")
    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' hh:mm:ss a");
        return "Current date and time: " + now.format(formatter);
    }

    /**
     * Tool 3: Product Details Lookup (mock)
     * The AI calls this when the user asks about a product.
     */
    @Tool("Fetch product details by product name. Returns price, stock status, and description.")
    public String getProductDetails(String productName) {
        // Simulated product catalog
        return switch (productName.toLowerCase().trim()) {
            case "laptop" -> "Product: Laptop | Price: $999 | Stock: Available | Specs: 16GB RAM, 512GB SSD, Intel i7";
            case "phone"  -> "Product: Phone  | Price: $499 | Stock: Limited  | Specs: 128GB, 8GB RAM, 5G enabled";
            case "tablet" -> "Product: Tablet | Price: $349 | Stock: Available | Specs: 10-inch display, 64GB storage";
            case "headphones" -> "Product: Headphones | Price: $149 | Stock: In Stock | Specs: Noise Cancelling, Wireless";
            default -> "Product '" + productName + "' not found in catalog. Try: laptop, phone, tablet, headphones.";
        };
    }
}
