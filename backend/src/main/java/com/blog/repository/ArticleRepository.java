package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);
    Page<Article> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Article> findByTagsId(Long tagId, Pageable pageable);
    Page<Article> findByStatusAndCategoryId(ArticleStatus status, Long categoryId, Pageable pageable);
    Page<Article> findByStatusAndTagsId(ArticleStatus status, Long tagId, Pageable pageable);
    
    @Query("SELECT a FROM Article a WHERE a.status = :status AND (a.title LIKE %:keyword% OR a.summary LIKE %:keyword%)")
    Page<Article> searchByKeyword(@Param("keyword") String keyword, @Param("status") ArticleStatus status, Pageable pageable);
    
    @Query("SELECT a FROM Article a WHERE a.status = :status ORDER BY a.createdAt DESC")
    List<Article> findTopByStatusOrderByCreatedAtDesc(@Param("status") ArticleStatus status, Pageable pageable);
    
    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    @Modifying
    @Query("UPDATE Article a SET a.likeCount = a.likeCount + 1 WHERE a.id = :id")
    void incrementLikeCount(@Param("id") Long id);
    
    boolean existsBySlug(String slug);
}
