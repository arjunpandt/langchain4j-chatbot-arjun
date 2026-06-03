# Day 1 Chatbot Workshop — LangChain4j + Spring Boot + Gemini

> **Author:** Arjun Pandit  
> **Branch:** `feature/langchain4j-chatbot`

---

## Implementation Details

### Project Architecture

```
HTTP Request
     │
     ▼
ChatController (REST API — /api/chat)
     │
     ▼
ChatService (Input validation + Error handling)
     │
     ▼
ChatAssistant (@AiService — LangChain4j proxy)
     │           │
     │           ▼
     │      ChatbotTools (@Tool methods)
     │       ├── calculate()
     │       ├── getCurrentDateTime()
     │       └── getProductDetails()
     │
     ▼
GoogleAiGeminiChatModel (Gemini API)
     │
     ▼
MessageWindowChatMemory (last 10 messages)
```

**Flow:** User sends a message → `ChatController` receives it → `ChatService` validates + calls `ChatAssistant` → LangChain4j routes to Gemini with system prompt and memory → If the AI decides a tool is needed, it calls the `@Tool` method automatically → Response returned to user.

### Framework

- **Spring Boot 3.2.5** — application framework
- **LangChain4j 0.31.0** — AI integration framework
- **Google Gemini (gemini-2.5-flash)** — AI model provider
- **Java 17**

---

## Feature Description

### 1. AI Service Layer (`@AiService`)

`ChatAssistant` is a Java interface annotated with `@AiService`. LangChain4j automatically creates a proxy implementation that:
- Routes method calls to the configured Gemini model
- Injects chat memory (conversation history)
- Automatically calls `@Tool` methods when the model decides they are needed

```java
@AiService
public interface ChatAssistant {
    @SystemMessage("You are Aria, a helpful AI assistant...")
    String chat(String userMessage);
}
```

### 2. Prompt Strategy (`@SystemMessage`)

The `@SystemMessage` annotation defines the **system prompt** — the instructions given to the AI before every conversation. It defines:
- **Persona:** "You are Aria, a friendly AI assistant"
- **Responsibilities:** What the bot should and shouldn't do
- **Tool routing hints:** When to use which tool
- **Response style:** Concise, 2–4 sentences, bullet points when needed

This is **prompt engineering** — carefully crafted instructions that shape the AI's behavior and quality of output.

### 3. Tool / Function Calling (`@Tool`)

LangChain4j lets you annotate regular Java methods with `@Tool`. When the AI detects user intent that matches a tool, it calls the method automatically and incorporates the result into its response.

| Tool | Triggered When |
|------|---------------|
| `calculate(String expr)` | User asks for a math calculation |
| `getCurrentDateTime()` | User asks for current time/date |
| `getProductDetails(String name)` | User asks about a product |

```java
@Tool("Calculate the result of a simple math expression")
public String calculate(String expression) { ... }
```

### 4. Model Parameter Configuration

Configured in `LangChainConfig.java` and `application.properties`:

| Parameter | Value | Effect |
|-----------|-------|--------|
| `temperature` | `0.7` | Balanced creativity — not too random, not too rigid |
| `maxOutputTokens` | `512` | Limits response length to ~400 words |
| `model` | `gemini-2.5-flash` | Fast, cost-efficient Gemini model |

**Changing Temperature Effects:**
- `temperature=0.1` → Very predictable, factual, almost robotic
- `temperature=0.7` → Balanced (default)
- `temperature=1.5` → Very creative, sometimes random or off-topic

### 5. Error Handling

`ChatService` handles:

| Scenario | Handling |
|----------|---------|
| Empty/null input | Returns: "Please type a message" |
| Input > 1000 chars | Returns: "Message too long" |
| AI model exception | Catches exception, returns friendly fallback message |
| Simulated failure | `/api/chat/simulate-error` endpoint demonstrates fallback |

---

### Screenshots
<img width="1920" height="1032" alt="Screenshot 2026-06-03 182115" src="https://github.com/user-attachments/assets/c8039565-27fc-4aff-89a6-5998af08af3b" />
<img width="1920" height="1032" alt="Screenshot 2026-06-03 182143" src="https://github.com/user-attachments/assets/af723e27-5e71-473a-a15f-472e8076d239" />




---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+
- Gemini API Key → [Get it free at Google AI Studio](https://aistudio.google.com/)

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/YOUR_USERNAME/langchain4j-chatbot-arjun.git
cd langchain4j-chatbot-arjun/day2-chatbot-workshop

# 2. Add your API key
# Open src/main/resources/application.properties
# Replace YOUR_GEMINI_API_KEY_HERE with your actual key

# 3. Run
mvn spring-boot:run

# 4. Open browser
# http://localhost:8080
```

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/chat` | Send a message, get AI response |
| `GET`  | `/api/chat/health` | Health check |
| `GET`  | `/api/chat/simulate-error` | Demo error fallback |

**Example request:**
```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "calculate 25 * 4"}'
```

---

## Model Settings Demo

To see different outputs with different temperatures, change in `application.properties`:

```properties
# Predictable (factual)
gemini.model.temperature=0.1

# Balanced (default)
gemini.model.temperature=0.7

# Creative (experimental)
gemini.model.temperature=1.5
```
