package com.example.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Upload image to Cloudinary
     * @param file MultipartFile to upload
     * @param folder Folder name in Cloudinary (e.g., "users/profiles", "users/gallery")
     * @return URL of uploaded image
     * @throws IOException if upload fails
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Generate unique public_id for the image
        String publicId = folder + "/" + UUID.randomUUID().toString();

        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", folder,
                "resource_type", "image",
                "format", "jpg", // Convert all images to JPG
                "quality", "auto:good", // Optimize quality
                "fetch_format", "auto" // Auto format selection
        );

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Upload profile image
     */
    public String uploadProfileImage(MultipartFile file, UUID userId) throws IOException {
        return uploadImage(file, "users/profiles/" + userId);
    }

    /**
     * Upload gallery image
     */
    public String uploadGalleryImage(MultipartFile file, UUID userId, int imageNumber) throws IOException {
        return uploadImage(file, "users/gallery/" + userId + "/image" + imageNumber);
    }

    /**
     * Delete image from Cloudinary
     * @param imageUrl Full URL of the image to delete
     * @return true if deletion was successful
     */
    public boolean deleteImage(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                return false;
            }

            // Extract public_id from URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId == null) {
                return false;
            }

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract public_id from Cloudinary URL
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            // Example URL: https://res.cloudinary.com/cloud_name/image/upload/v1234567890/folder/image_name.jpg
            String[] urlParts = imageUrl.split("/");
            if (urlParts.length < 2) {
                return null;
            }

            // Find the version part (starts with 'v' followed by numbers)
            int versionIndex = -1;
            for (int i = 0; i < urlParts.length; i++) {
                if (urlParts[i].startsWith("v") && urlParts[i].length() > 1 &&
                        urlParts[i].substring(1).matches("\\d+")) {
                    versionIndex = i;
                    break;
                }
            }

            if (versionIndex == -1 || versionIndex + 1 >= urlParts.length) {
                return null;
            }

            // Reconstruct the path after version
            StringBuilder publicId = new StringBuilder();
            for (int i = versionIndex + 1; i < urlParts.length; i++) {
                if (i > versionIndex + 1) {
                    publicId.append("/");
                }
                // Remove file extension from the last part
                if (i == urlParts.length - 1) {
                    String lastPart = urlParts[i];
                    int dotIndex = lastPart.lastIndexOf('.');
                    if (dotIndex > 0) {
                        publicId.append(lastPart.substring(0, dotIndex));
                    } else {
                        publicId.append(lastPart);
                    }
                } else {
                    publicId.append(urlParts[i]);
                }
            }

            return publicId.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generate transformation URL for resizing images
     */
    public String getResizedImageUrl(String originalUrl, int width, int height) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            return null;
        }

        try {
            // Insert transformation parameters into the URL
            String transformation = "c_fill,w_" + width + ",h_" + height + ",q_auto,f_auto";
            return originalUrl.replace("/upload/", "/upload/" + transformation + "/");
        } catch (Exception e) {
            e.printStackTrace();
            return originalUrl; // Return original URL if transformation fails
        }
    }

    /**
     * Get thumbnail URL (150x150)
     */
    public String getThumbnailUrl(String originalUrl) {
        return getResizedImageUrl(originalUrl, 150, 150);
    }

    /**
     * Get medium size URL (400x400)
     */
    public String getMediumImageUrl(String originalUrl) {
        return getResizedImageUrl(originalUrl, 400, 400);
    }
}