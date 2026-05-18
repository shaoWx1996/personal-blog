package com.blog.dto.response;

import com.blog.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private String slug;
    private String status;
    private Long viewCount;
    private Long likeCount;
    private CategoryResponse category;
    private List<TagResponse> tags;
    private Long commentCount;
    private String createdAt;
    private String updatedAt;
    
    public static ArticleResponse fromEntity(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .summary(article.getSummary())
                .coverImage(article.getCoverImage())
                .slug(article.getSlug())
                .status(article.getStatus().name())
                .viewCount(article.getViewCount())
                .likeCount(article.getLikeCount())
                .category(article.getCategory() != null ? CategoryResponse.fromEntity(article.getCategory()) : null)
                .tags(article.getTags() != null ? 
                        article.getTags().stream().map(TagResponse::fromEntity).collect(Collectors.toList()) : null)
                .commentCount(article.getComments() != null ? (long) article.getComments().size() : 0L)
                .createdAt(article.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .updatedAt(article.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
    
    public static ArticleResponse fromEntityWithCommentCount(Article article, Long commentCount) {
        ArticleResponse response = fromEntity(article);
        response.setCommentCount(commentCount);
        return response;
    }
}
