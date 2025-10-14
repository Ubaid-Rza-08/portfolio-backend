package com.example.backend.service;

import com.example.backend.entity.Blog;
import com.example.backend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public Blog saveBlog(Blog blog) {
        if (blog.getStatus().equals("published") && blog.getPublishedAt() == null) {
            blog.setPublishedAt(LocalDateTime.now());
        }
        return blogRepository.save(blog);
    }

    public List<Blog> findPublishedByUserId(UUID userId) {
        return blogRepository.findByUserIdAndStatusOrderByPublishedAtDesc(userId, "published");
    }

    public List<Blog> findFeaturedByUserId(UUID userId) {
        return blogRepository.findByUserIdAndIsFeaturedTrue(userId);
    }

    public Optional<Blog> findById(UUID id) {
        return blogRepository.findById(id);
    }

    public Optional<Blog> findBySlug(String slug) {
        return blogRepository.findBySlug(slug);
    }

    public List<Blog> findByCategory(String category) {
        return blogRepository.findByCategory(category);
    }

    public List<Blog> searchByTitle(UUID userId, String title) {
        return blogRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title);
    }

    public Blog updateBlog(UUID id, Blog details) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blog.setTitle(details.getTitle());
                    blog.setSlug(details.getSlug());
                    blog.setExcerpt(details.getExcerpt());
                    blog.setContent(details.getContent());
                    blog.setCategory(details.getCategory());
                    blog.setTags(details.getTags());
                    blog.setStatus(details.getStatus());
                    blog.setIsFeatured(details.getIsFeatured());
                    blog.setSeoTitle(details.getSeoTitle());
                    blog.setSeoDescription(details.getSeoDescription());
                    blog.setAllowComments(details.getAllowComments());
                    blog.setReadingTimeMinutes(details.getReadingTimeMinutes());

                    if (details.getStatus().equals("published") && blog.getPublishedAt() == null) {
                        blog.setPublishedAt(LocalDateTime.now());
                    }

                    if (details.getFeaturedImageUrl() != null) {
                        blog.setFeaturedImageUrl(details.getFeaturedImageUrl());
                    }

                    return blogRepository.save(blog);
                })
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
    }

    public Blog incrementViewCount(UUID id) {
        return blogRepository.findById(id)
                .map(blog -> {
                    blog.setViewCount(blog.getViewCount() + 1);
                    return blogRepository.save(blog);
                })
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
    }

    public boolean deleteBlog(UUID id) {
        if (blogRepository.existsById(id)) {
            blogRepository.deleteById(id);
            return true;
        }
        return false;
    }
    // Add this method to your BlogService class

    public long getBlogsCount() {
        return blogRepository.count();
    }

    
}
