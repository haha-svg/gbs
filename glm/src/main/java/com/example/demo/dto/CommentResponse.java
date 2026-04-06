package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private Long noteId;
    private Long parentId;
    private Integer replyCount;
    private LocalDateTime createdAt;
}
