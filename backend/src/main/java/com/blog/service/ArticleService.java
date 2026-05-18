package com.blog.service;

import com.blog.dto.request.ArticleCreateRequest;
import com.blog.dto.request.ArticleUpdateRequest;
import com.blog.dto.response.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Page<ArticleResponse> getAllArticles(Pageable pageable);
    Page<ArticleResponse> getPublishedArticles(Pageable pageable);
    ArticleResponse getArticleById(Long id);
    ArticleResponse getArticleBySlug(String slug);
    ArticleResponse createArticle(ArticleCreateRequest request);
    ArticleResponse updateArticle(Long id, ArticleUpdateRequest request);
    void deleteArticle(Long id);
    Page<ArticleResponse> searchArticles(String keyword, Pageable pageable);
    Page<ArticleResponse> getArticlesByCategory(Long categoryId, Pageable pageable);
    Page<ArticleResponse> getArticlesByTag(Long tagId, Pageable pageable);
    void incrementViewCount(Long id);
}
