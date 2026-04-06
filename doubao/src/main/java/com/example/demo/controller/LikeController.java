package com.example.demo.controller;

import com.example.demo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/{noteId}")
    public Map<String, Object> likeNote(@PathVariable Long noteId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        boolean isLiked = likeService.likeNote(userId, noteId);
        return Map.of("code", 200, "message", isLiked ? "点赞成功" : "取消点赞成功", "isLiked", isLiked);
    }

    @GetMapping("/status/{noteId}")
    public Map<String, Object> getLikeStatus(@PathVariable Long noteId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        boolean isLiked = likeService.isLiked(userId, noteId);
        return Map.of("code", 200, "isLiked", isLiked);
    }
}