package com.example.demo.service;

import com.example.demo.entity.Like;
import com.example.demo.entity.Message;
import com.example.demo.entity.Note;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public boolean likeNote(Long userId, Long noteId) {
        // 检查是否已经点赞
        Optional<Like> likeOptional = likeRepository.findByUserIdAndNoteId(userId, noteId);
        if (likeOptional.isPresent()) {
            // 已点赞，取消点赞
            likeRepository.deleteByUserIdAndNoteId(userId, noteId);
            // 更新笔记点赞数
            updateNoteLikeCount(noteId);
            return false;
        } else {
            // 未点赞，添加点赞
            Like like = new Like();
            like.setUserId(userId);
            like.setNoteId(noteId);
            like.setCreatedAt(new Date());
            likeRepository.save(like);
            // 更新笔记点赞数
            updateNoteLikeCount(noteId);
            // 发送消息通知
            sendLikeNotification(userId, noteId);
            return true;
        }
    }

    private void updateNoteLikeCount(Long noteId) {
        Long likeCount = likeRepository.countByNoteId(noteId);
        Note note = noteRepository.findById(noteId).orElse(null);
        if (note != null) {
            note.setLikeCount(likeCount.intValue());
            noteRepository.save(note);
        }
    }

    private void sendLikeNotification(Long userId, Long noteId) {
        Note note = noteRepository.findById(noteId).orElse(null);
        if (note != null && !userId.equals(note.getUserId())) {
            Message message = new Message();
            message.setUserId(note.getUserId());
            message.setSenderId(userId);
            message.setType(1); // 1: 点赞通知
            message.setContent("有人点赞了你的笔记");
            message.setIsRead(false);
            message.setCreatedAt(new Date());
            messageRepository.save(message);
        }
    }

    public boolean isLiked(Long userId, Long noteId) {
        return likeRepository.findByUserIdAndNoteId(userId, noteId).isPresent();
    }
}