package com.example.demo.service;

import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesByUserId(Long userId) {
        return messageRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Message> getUnreadMessagesByUserId(Long userId) {
        return messageRepository.findByUserIdAndIsReadFalse(userId);
    }

    public void markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setIsRead(true);
            messageRepository.save(message);
        }
    }
}