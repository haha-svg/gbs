package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    
    private final LikeService likeService;
    
    @PostMapping("/note/{noteId}")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> toggleLike(
            @PathVariable Long noteId,
            @AuthenticationPrincipal UserPrincipal principal) {
        boolean isLiked = likeService.toggleLike(principal.getId(), noteId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isLiked", isLiked);
        return ResponseEntity.ok(ApiResponse.success(isLiked ? "点赞成功" : "取消点赞", result));
    }
    
    @GetMapping("/note/{noteId}/status")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> getLikeStatus(
            @PathVariable Long noteId,
            @AuthenticationPrincipal UserPrincipal principal) {
        boolean isLiked = likeService.isLiked(principal.getId(), noteId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isLiked", isLiked);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
