package com.example.demo.repository;

import com.example.demo.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndNoteId(Long userId, Long noteId);
    void deleteByUserIdAndNoteId(Long userId, Long noteId);
    Long countByNoteId(Long noteId);
}