package com.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Author name is required")
    private String authorName;
    
    @Email(message = "Invalid email format")
    private String authorEmail;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private Long parentId;
}
