# langchain4j-chatbot-arjun

**Author:** Arjun Pandit

## Project Overview

This repository contains an **AI Chatbot** built as part of the LangChain4j Day 1 Workshop.  
The chatbot uses **LangChain4j** (Java AI framework) and **Google Gemini** as the AI model provider, wrapped in a **Spring Boot** application.

## Short Explanation

The chatbot — named **Aria** — can hold multi-turn conversations, answer general knowledge questions, perform math calculations, retrieve product details, and tell the current time. It demonstrates key AI engineering concepts like AI Service layers, system prompts, tool/function calling, model parameter tuning, and graceful error handling.

## Repository Structure

```
langchain4j-chatbot-arjun/
├── day1-chatbot-workshop/     ← Main Spring Boot project
│   ├── src/
│   ├── pom.xml
│   └── README.md              ← Detailed documentation
└── README.md                  ← This file
```

## Quick Start

1. Add your Gemini API key to `day1-chatbot-workshop/src/main/resources/application.properties`
2. Run: `cd day1-chatbot-workshop && mvn spring-boot:run`
3. Open: `http://localhost:8080`
