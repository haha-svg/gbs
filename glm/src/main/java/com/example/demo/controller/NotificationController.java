package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Notification;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Notification>>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        Page<Notification> notifications = notificationService.getNotifications(principal.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal principal) {
        long count = notificationService.getUnreadCount(principal.getId());
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAsRead(id, principal.getId());
        return ResponseEntity.ok(ApiResponse.success("已标记为已读", null));
    }
    
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<String>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal principal) {
        notificationService.markAllAsRead(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("全部已标记为已读", null));
    }
}
