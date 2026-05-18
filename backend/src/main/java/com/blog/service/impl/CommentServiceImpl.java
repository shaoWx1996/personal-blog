package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;
import com.blog.entity.Article;
import com.blog.entity.Comment;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CommentRepository;
import com.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Override
    public List<CommentResponse> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleIdAndIsApprovedTrueOrderByCreatedAtAsc(articleId).stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long articleId, CommentRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        
        Comment comment = Comment.builder()
                .article(article)
                .authorName(request.getAuthorName())
                .authorEmail(request.getAuthorEmail())
                .content(request.getContent())
                .parentId(request.getParentId())
                .isApproved(false)
                .build();
        
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponse approveComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        comment.setIsApproved(true);
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
}
