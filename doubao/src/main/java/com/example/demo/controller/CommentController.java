package com.example.demo.controller;

import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Map<String, Object> addComment(@RequestBody Map<String, Object> params, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Long noteId = Long.parseLong(params.get("noteId").toString());
        Long parentId = params.get("parentId") != null ? Long.parseLong(params.get("parentId").toString()) : null;
        String content = params.get("content").toString();

        try {
            commentService.addComment(userId, noteId, parentId, content);
            return Map.of("code", 200, "message", "评论成功");
        } catch (Exception e) {
            return Map.of("code", 400, "message", e.getMessage());
        }
    }

    @GetMapping("/note/{noteId}")
    public Map<String, Object> getCommentsByNoteId(@PathVariable Long noteId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Map.of("code", 200, "data", commentService.getCommentsByNoteId(noteId, pageable));
    }

    @GetMapping("/replies/{parentId}")
    public Map<String, Object> getRepliesByParentId(@PathVariable Long parentId) {
        return Map.of("code", 200, "data", commentService.getRepliesByParentId(parentId));
    }
}