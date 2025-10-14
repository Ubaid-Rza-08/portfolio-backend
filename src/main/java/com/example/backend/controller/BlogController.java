// BlogController.java - CORRECTED VERSION
package com.example.backend.controller;

import com.example.backend.entity.Blog;
import com.example.backend.service.BlogService;
import com.example.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/user/{userId}/published")
    public ResponseEntity<List<Blog>> getPublishedBlogs(@PathVariable UUID userId) {
        List<Blog> blogs = blogService.findPublishedByUserId(userId);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/user/{userId}/featured")
    public ResponseEntity<List<Blog>> getFeaturedBlogs(@PathVariable UUID userId) {
        List<Blog> blogs = blogService.findFeaturedByUserId(userId);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable UUID id) {
        Optional<Blog> blog = blogService.findById(id);
        return blog.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Blog> getBlogBySlug(@PathVariable String slug) {
        Optional<Blog> blog = blogService.findBySlug(slug);
        if (blog.isPresent()) {
            blogService.incrementViewCount(blog.get().getId());
            return ResponseEntity.ok(blog.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Blog>> getBlogsByCategory(@PathVariable String category) {
        List<Blog> blogs = blogService.findByCategory(category);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<Blog>> searchBlogs(@PathVariable UUID userId, @RequestParam String title) {
        List<Blog> blogs = blogService.searchByTitle(userId, title);
        return ResponseEntity.ok(blogs);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBlog(
            @RequestParam UUID userId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) String excerpt,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) String seoTitle,
            @RequestParam(required = false) String seoDescription,
            @RequestParam(required = false) Boolean allowComments,
            @RequestParam(required = false) Integer readingTimeMinutes,
            @RequestParam(required = false) MultipartFile featuredImage) {

        try {
            Blog blog = new Blog();
            blog.setUserId(userId);
            blog.setTitle(title);
            blog.setContent(content);
            blog.setSlug(slug);
            blog.setExcerpt(excerpt);
            blog.setCategory(category);
            blog.setTags(tags);
            blog.setStatus(status != null ? status : "draft");
            blog.setIsFeatured(isFeatured != null ? isFeatured : false);
            blog.setSeoTitle(seoTitle);
            blog.setSeoDescription(seoDescription);
            blog.setAllowComments(allowComments != null ? allowComments : true);
            blog.setReadingTimeMinutes(readingTimeMinutes);

            if (blog.getStatus().equals("published")) {
                blog.setPublishedAt(LocalDateTime.now());
            }

            // Save blog first
            Blog savedBlog = blogService.saveBlog(blog);

            // Handle featured image upload
            if (featuredImage != null && !featuredImage.isEmpty()) {
                String imageUrl = cloudinaryService.uploadBlogImage(featuredImage, savedBlog.getId());
                savedBlog.setFeaturedImageUrl(imageUrl);
                savedBlog = blogService.saveBlog(savedBlog);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBlog);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload featured image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // CORRECT METHOD - This should be the ONLY updateBlog method in BlogController
    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable UUID id, @RequestBody Blog blogDetails) {
        try {
            Blog updatedBlog = blogService.updateBlog(id, blogDetails);
            return ResponseEntity.ok(updatedBlog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable UUID id) {
        try {
            Optional<Blog> blogOpt = blogService.findById(id);
            if (blogOpt.isPresent()) {
                Blog blog = blogOpt.get();

                // Delete featured image from Cloudinary
                if (blog.getFeaturedImageUrl() != null) {
                    cloudinaryService.deleteImage(blog.getFeaturedImageUrl());
                }

                if (blogService.deleteBlog(id)) {
                    return ResponseEntity.noContent().build();
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/upload-featured-image")
    public ResponseEntity<?> uploadFeaturedImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Blog> blogOpt = blogService.findById(id);
            if (blogOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Blog blog = blogOpt.get();

            // Delete old featured image if exists
            if (blog.getFeaturedImageUrl() != null) {
                cloudinaryService.deleteImage(blog.getFeaturedImageUrl());
            }

            // Upload new featured image
            String imageUrl = cloudinaryService.uploadBlogImage(file, id);
            blog.setFeaturedImageUrl(imageUrl);

            Blog updatedBlog = blogService.saveBlog(blog);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Featured image uploaded successfully");
            response.put("imageUrl", imageUrl);
            response.put("thumbnailUrl", cloudinaryService.getThumbnailUrl(imageUrl));
            response.put("blog", updatedBlog);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getBlogCount() {
        try {
            long count = blogService.getBlogsCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}