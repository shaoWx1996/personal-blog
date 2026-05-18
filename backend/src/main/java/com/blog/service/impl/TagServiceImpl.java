package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.TagRequest;
import com.blog.dto.response.TagResponse;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import com.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(TagResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return TagResponse.fromEntity(tag);
    }

    @Override
    @Transactional
    public TagResponse createTag(TagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tag name already exists");
        }
        
        String slug = StringUtils.hasText(request.getSlug()) 
                ? request.getSlug() 
                : request.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        
        if (tagRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Tag slug already exists");
        }
        
        Tag tag = Tag.builder()
                .name(request.getName())
                .slug(slug)
                .build();
        
        return TagResponse.fromEntity(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        
        if (StringUtils.hasText(request.getName()) && !request.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Tag name already exists");
            }
            tag.setName(request.getName());
        }
        
        if (StringUtils.hasText(request.getSlug())) {
            if (!request.getSlug().equals(tag.getSlug()) && tagRepository.existsBySlug(request.getSlug())) {
                throw new IllegalArgumentException("Tag slug already exists");
            }
            tag.setSlug(request.getSlug());
        }
        
        return TagResponse.fromEntity(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }
}
