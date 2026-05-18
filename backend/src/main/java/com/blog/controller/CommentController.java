package com.blog.controller;

import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;
import com.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/articles/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByArticle(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByArticle(id));
    }

    @GetMapping("/admin/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(id, request));
    }

    @PutMapping("/admin/comments/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentResponse> approveComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.approveComment(id));
    }

    @DeleteMapping("/admin/comments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
