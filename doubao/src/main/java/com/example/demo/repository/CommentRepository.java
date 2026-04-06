package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByNoteIdAndParentIdIsNull(Long noteId, Pageable pageable);
    List<Comment> findByParentId(Long parentId);
    Long countByNoteId(Long noteId);
}