package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Message;
import com.example.demo.entity.Note;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MessageRepository messageRepository;

    private static final int MAX_CONTENT_LENGTH = 500;

    @Transactional
    public Comment addComment(Long userId, Long noteId, Long parentId, String content) {
        // 检查内容长度
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new RuntimeException("评论内容长度不能超过500字");
        }

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setNoteId(noteId);
        comment.setParentId(parentId);
        comment.setContent(content);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());

        Comment savedComment = commentRepository.save(comment);

        // 更新笔记评论数
        updateNoteCommentCount(noteId);

        // 发送消息通知
        sendCommentNotification(userId, noteId, parentId);

        return savedComment;
    }

    private void updateNoteCommentCount(Long noteId) {
        Long commentCount = commentRepository.countByNoteId(noteId);
        Note note = noteRepository.findById(noteId).orElse(null);
        if (note != null) {
            note.setCommentCount(commentCount.intValue());
            noteRepository.save(note);
        }
    }

    private void sendCommentNotification(Long userId, Long noteId, Long parentId) {
        Note note = noteRepository.findById(noteId).orElse(null);
        if (note != null) {
            // 通知笔记作者
            if (!userId.equals(note.getUserId())) {
                Message message = new Message();
                message.setUserId(note.getUserId());
                message.setSenderId(userId);
                message.setType(2); // 2: 评论通知
                message.setContent("有人评论了你的笔记");
                message.setIsRead(false);
                message.setCreatedAt(new Date());
                messageRepository.save(message);
            }

            // 如果是回复评论，通知被回复的评论作者
            if (parentId != null) {
                Comment parentComment = commentRepository.findById(parentId).orElse(null);
                if (parentComment != null && !userId.equals(parentComment.getUserId())) {
                    Message message = new Message();
                    message.setUserId(parentComment.getUserId());
                    message.setSenderId(userId);
                    message.setType(2); // 2: 评论通知
                    message.setContent("有人回复了你的评论");
                    message.setIsRead(false);
                    message.setCreatedAt(new Date());
                    messageRepository.save(message);
                }
            }
        }
    }

    public Page<Comment> getCommentsByNoteId(Long noteId, Pageable pageable) {
        return commentRepository.findByNoteIdAndParentIdIsNull(noteId, pageable);
    }

    public Iterable<Comment> getRepliesByParentId(Long parentId) {
        return commentRepository.findByParentId(parentId);
    }
}