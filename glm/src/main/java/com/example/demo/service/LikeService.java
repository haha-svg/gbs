package com.example.demo.service;

import com.example.demo.entity.Like;
import com.example.demo.entity.Note;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    
    @Transactional
    public boolean toggleLike(Long userId, Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("笔记不存在"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Optional<Like> existingLike = likeRepository.findByUserIdAndNoteId(userId, noteId);
        
        if (existingLike.isPresent()) {
            Like like = existingLike.get();
            if (like.getStatus()) {
                like.setStatus(false);
                note.setLikeCount(note.getLikeCount() - 1);
            } else {
                like.setStatus(true);
                note.setLikeCount(note.getLikeCount() + 1);
            }
            likeRepository.save(like);
            noteRepository.save(note);
            return like.getStatus();
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setNote(note);
            like.setStatus(true);
            likeRepository.save(like);
            
            note.setLikeCount(note.getLikeCount() + 1);
            noteRepository.save(note);
            
            sendLikeNotification(user, note);
            return true;
        }
    }
    
    public boolean isLiked(Long userId, Long noteId) {
        return likeRepository.findByUserIdAndNoteId(userId, noteId)
                .map(Like::getStatus)
                .orElse(false);
    }
    
    private void sendLikeNotification(User user, Note note) {
        if (!user.getId().equals(note.getUser().getId())) {
            Notification notification = new Notification();
            notification.setUser(note.getUser());
            notification.setType("LIKE");
            notification.setTitle("收到新的点赞");
            notification.setContent(user.getNickname() + " 赞了你的笔记《" + note.getTitle() + "》");
            notification.setRelatedId(note.getId());
            notificationRepository.save(notification);
        }
    }
}
