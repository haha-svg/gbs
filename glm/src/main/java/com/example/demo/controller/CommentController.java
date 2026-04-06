package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping("/note/{noteId}")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long noteId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        CommentResponse response = commentService.addComment(principal.getId(), noteId, request);
        return ResponseEntity.ok(ApiResponse.success("评论成功", response));
    }
    
    @GetMapping("/note/{noteId}")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComments(
            @PathVariable Long noteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentResponse> comments = commentService.getComments(noteId, page, size);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }
    
    @GetMapping("/{parentId}/replies")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getReplies(
            @PathVariable Long parentId) {
        List<CommentResponse> replies = commentService.getReplies(parentId);
        return ResponseEntity.ok(ApiResponse.success(replies));
    }
}
