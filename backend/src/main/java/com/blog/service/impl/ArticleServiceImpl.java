package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.ArticleCreateRequest;
import com.blog.dto.request.ArticleUpdateRequest;
import com.blog.dto.response.ArticleResponse;
import com.blog.entity.Article;
import com.blog.entity.ArticleStatus;
import com.blog.entity.Category;
import com.blog.entity.Tag;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.CommentRepository;
import com.blog.repository.TagRepository;
import com.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    @Override
    public Page<ArticleResponse> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(article -> 
            ArticleResponse.fromEntityWithCommentCount(article, 
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public Page<ArticleResponse> getPublishedArticles(Pageable pageable) {
        return articleRepository.findByStatus(ArticleStatus.PUBLISHED, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + id));
        return ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId()));
    }

    @Override
    @Transactional
    public ArticleResponse getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with slug: " + slug));
        
        articleRepository.incrementViewCount(article.getId());
        
        return ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId()));
    }

    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        String slug = generateSlug(request.getTitle());
        
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }
        
        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null) {
            request.getTagIds().forEach(tagId -> {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId));
                tags.add(tag);
            });
        }
        
        ArticleStatus status = ArticleStatus.valueOf(request.getStatus().toUpperCase());
        
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .summary(request.getSummary())
                .coverImage(request.getCoverImage())
                .slug(slug)
                .status(status)
                .category(category)
                .tags(tags)
                .build();
        
        return ArticleResponse.fromEntity(articleRepository.save(article));
    }

    @Override
    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + id));
        
        if (StringUtils.hasText(request.getTitle())) {
            article.setTitle(request.getTitle());
            article.setSlug(generateSlug(request.getTitle()));
        }
        
        if (request.getContent() != null) {
            article.setContent(request.getContent());
        }
        
        if (request.getSummary() != null) {
            article.setSummary(request.getSummary());
        }
        
        if (request.getCoverImage() != null) {
            article.setCoverImage(request.getCoverImage());
        }
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            article.setCategory(category);
        }
        
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>();
            request.getTagIds().forEach(tagId -> {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId));
                tags.add(tag);
            });
            article.setTags(tags);
        }
        
        if (StringUtils.hasText(request.getStatus())) {
            article.setStatus(ArticleStatus.valueOf(request.getStatus().toUpperCase()));
        }
        
        return ArticleResponse.fromEntity(articleRepository.save(article));
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article not found with id: " + id);
        }
        articleRepository.deleteById(id);
    }

    @Override
    public Page<ArticleResponse> searchArticles(String keyword, Pageable pageable) {
        return articleRepository.searchByKeyword(keyword, ArticleStatus.PUBLISHED, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public Page<ArticleResponse> getArticlesByCategory(Long categoryId, Pageable pageable) {
        return articleRepository.findByStatusAndCategoryId(ArticleStatus.PUBLISHED, categoryId, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public Page<ArticleResponse> getArticlesByTag(Long tagId, Pageable pageable) {
        return articleRepository.findByStatusAndTagsId(ArticleStatus.PUBLISHED, tagId, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        articleRepository.incrementViewCount(id);
    }

    private String generateSlug(String title) {
        String slug = title.toLowerCase().replaceAll("[^a-z0-9\\s]+", "").replaceAll("\\s+", "-");
        int counter = 1;
        String originalSlug = slug;
        
        while (articleRepository.existsBySlug(slug)) {
            slug = originalSlug + "-" + counter++;
        }
        
        return slug;
    }
}
