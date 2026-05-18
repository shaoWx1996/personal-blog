package com.blog.dto.response;

import com.blog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long articleId;
    private String authorName;
    private String authorEmail;
    private String content;
    private Long parentId;
    private Boolean isApproved;
    private String createdAt;
    
    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .articleId(comment.getArticle().getId())
                .authorName(comment.getAuthorName())
                .authorEmail(comment.getAuthorEmail())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .isApproved(comment.getIsApproved())
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
}
