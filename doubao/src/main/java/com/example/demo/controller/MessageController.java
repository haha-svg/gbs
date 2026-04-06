package com.example.demo.controller;

import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public Map<String, Object> getMessages(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Map.of("code", 200, "data", messageService.getMessagesByUserId(userId));
    }

    @GetMapping("/unread")
    public Map<String, Object> getUnreadMessages(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Map.of("code", 200, "data", messageService.getUnreadMessagesByUserId(userId));
    }

    @PutMapping("/{messageId}/read")
    public Map<String, Object> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
        return Map.of("code", 200, "message", "标记已读成功");
    }
}