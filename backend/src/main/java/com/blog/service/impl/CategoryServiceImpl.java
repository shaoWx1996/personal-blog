package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.CategoryRequest;
import com.blog.dto.response.CategoryResponse;
import com.blog.entity.Category;
import com.blog.repository.CategoryRepository;
import com.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return CategoryResponse.fromEntity(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        
        String slug = generateSlug(request.getSlug(), request.getName());
        
        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .build();
        
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }
    
    private String generateSlug(String providedSlug, String name) {
        String slug;
        if (StringUtils.hasText(providedSlug)) {
            slug = providedSlug.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        } else {
            slug = name.toLowerCase().replaceAll("[^a-z0-9\\u4e00-\\u9fa5]+", "-");
            if (slug.isEmpty() || slug.equals("-")) {
                slug = "category-" + System.currentTimeMillis();
            }
        }
        slug = slug.replaceAll("-+", "-").replaceAll("^-|-$", "");
        if (slug.isEmpty()) {
            slug = "category-" + System.currentTimeMillis();
        }
        if (categoryRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }
        return slug;
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        if (StringUtils.hasText(request.getName()) && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Category name already exists");
            }
            category.setName(request.getName());
        }
        
        if (StringUtils.hasText(request.getSlug())) {
            if (!request.getSlug().equals(category.getSlug()) && categoryRepository.existsBySlug(request.getSlug())) {
                throw new IllegalArgumentException("Category slug already exists");
            }
            category.setSlug(request.getSlug());
        }
        
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
