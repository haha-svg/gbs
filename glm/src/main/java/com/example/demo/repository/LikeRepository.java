package com.example.demo.repository;

import com.example.demo.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndNoteId(Long userId, Long noteId);
    boolean existsByUserIdAndNoteId(Long userId, Long noteId);
}
