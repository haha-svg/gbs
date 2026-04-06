package com.example.demo.service;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Note;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    
    @Transactional
    public CommentResponse addComment(Long userId, Long noteId, CommentRequest request) {
        if (request.getContent().length() > 500) {
            throw new RuntimeException("评论内容不能超过500字");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("笔记不存在"));
        
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setNote(note);
        comment.setContent(request.getContent());
        
        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            if (!parent.getNote().getId().equals(noteId)) {
                throw new RuntimeException("父评论不属于该笔记");
            }
            comment.setParent(parent);
            parent.setReplyCount(parent.getReplyCount() + 1);
            commentRepository.save(parent);
            
            sendReplyNotification(user, parent, note);
        } else {
            sendCommentNotification(user, note);
        }
        
        commentRepository.save(comment);
        
        note.setCommentCount(note.getCommentCount() + 1);
        noteRepository.save(note);
        
        return toCommentResponse(comment);
    }
    
    public Page<CommentResponse> getComments(Long noteId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentRepository.findByNoteIdAndParentIsNull(noteId, pageable);
        return comments.map(this::toCommentResponse);
    }
    
    public List<CommentResponse> getReplies(Long parentId) {
        List<Comment> replies = commentRepository.findByParentId(parentId);
        return replies.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }
    
    private CommentResponse toCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUserId(comment.getUser().getId());
        response.setUserNickname(comment.getUser().getNickname());
        response.setUserAvatar(comment.getUser().getAvatar());
        response.setNoteId(comment.getNote().getId());
        response.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        response.setReplyCount(comment.getReplyCount());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
    
    private void sendCommentNotification(User user, Note note) {
        if (!user.getId().equals(note.getUser().getId())) {
            Notification notification = new Notification();
            notification.setUser(note.getUser());
            notification.setType("COMMENT");
            notification.setTitle("收到新的评论");
            notification.setContent(user.getNickname() + " 评论了你的笔记《" + note.getTitle() + "》");
            notification.setRelatedId(note.getId());
            notificationRepository.save(notification);
        }
    }
    
    private void sendReplyNotification(User user, Comment parent, Note note) {
        if (!user.getId().equals(parent.getUser().getId())) {
            Notification notification = new Notification();
            notification.setUser(parent.getUser());
            notification.setType("REPLY");
            notification.setTitle("收到新的回复");
            notification.setContent(user.getNickname() + " 回复了你在《" + note.getTitle() + "》中的评论");
            notification.setRelatedId(note.getId());
            notificationRepository.save(notification);
        }
    }
}
