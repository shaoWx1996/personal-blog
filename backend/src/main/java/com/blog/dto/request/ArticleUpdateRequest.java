package com.blog.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateRequest {
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    private String content;
    
    @Size(max = 500, message = "Summary must be less than 500 characters")
    private String summary;
    
    private String coverImage;
    
    private Long categoryId;
    
    private List<Long> tagIds;
    
    private String status;
}
