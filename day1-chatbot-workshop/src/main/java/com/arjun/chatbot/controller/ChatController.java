package com.arjun.chatbot.controller;

import com.arjun.chatbot.dto.ChatRequest;
import com.arjun.chatbot.dto.ChatResponse;
import com.arjun.chatbot.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing chatbot endpoints.
 *
 * POST /api/chat          → Send a message, get AI response
 * GET  /api/chat/health   → Health check
 * GET  /api/chat/simulate-error → Demonstrate error fallback
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = chatService.chat(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(reply, "success"));
    }

    @GetMapping("/health")
    public ResponseEntity<ChatResponse> health() {
        return ResponseEntity.ok(new ChatResponse("Aria chatbot is running!", "ok"));
    }

    @GetMapping("/simulate-error")
    public ResponseEntity<ChatResponse> simulateError() {
        String fallback = chatService.simulateError();
        return ResponseEntity.ok(new ChatResponse(fallback, "fallback"));
    }
}
