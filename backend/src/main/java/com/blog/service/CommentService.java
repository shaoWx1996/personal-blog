package com.blog.service;

import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsByArticle(Long articleId);
    List<CommentResponse> getAllComments();
    CommentResponse createComment(Long articleId, CommentRequest request);
    CommentResponse approveComment(Long id);
    void deleteComment(Long id);
}
