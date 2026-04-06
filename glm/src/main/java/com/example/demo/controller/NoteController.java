package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Note>> createNote(
            @RequestBody Note note,
            @AuthenticationPrincipal UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        note.setUser(user);
        note.setLikeCount(0);
        note.setCommentCount(0);
        Note savedNote = noteRepository.save(note);
        return ResponseEntity.ok(ApiResponse.success("笔记创建成功", savedNote));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Note>>> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(notes));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Note>> getNote(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("笔记不存在"));
        return ResponseEntity.ok(ApiResponse.success(note));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteNote(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("笔记不存在"));
        if (!note.getUser().getId().equals(principal.getId())) {
            throw new RuntimeException("无权删除此笔记");
        }
        noteRepository.delete(note);
        return ResponseEntity.ok(ApiResponse.success("笔记删除成功", null));
    }
}
