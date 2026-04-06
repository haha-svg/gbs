package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public Page<Notification> getNotifications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
    
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("通知不存在"));
        if (!notification.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权操作此通知");
        }
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
    
    @Transactional
    public void markAllAsRead(Long userId) {
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(
                userId, PageRequest.of(0, Integer.MAX_VALUE));
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }
}
