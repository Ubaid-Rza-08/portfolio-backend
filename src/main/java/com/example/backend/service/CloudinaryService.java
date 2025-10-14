package com.example.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    private static final int MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB in bytes
    private static final int MAX_WIDTH = 1920;
    private static final int MAX_HEIGHT = 1920;

    /**
     * Upload image to Cloudinary with compression
     * @param file MultipartFile to upload
     * @param folder Folder name in Cloudinary
     * @return URL of uploaded image
     * @throws IOException if upload fails
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        byte[] compressedImageData = compressImage(file);

        // Generate unique public_id for the image
        String publicId = folder + "/" + UUID.randomUUID().toString();

        Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", folder,
                "resource_type", "image",
                "format", "jpg",
                "quality", "auto:good",
                "fetch_format", "auto"
                // REMOVED THE TRANSFORMATION BLOCK - this was causing the error
        );

        Map uploadResult = cloudinary.uploader().upload(compressedImageData, uploadOptions);
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Compress image to reduce file size under 2MB
     */
    private byte[] compressImage(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IOException("Invalid image file");
        }

        // Resize if image is too large
        BufferedImage resizedImage = resizeImageIfNeeded(originalImage);

        // Compress the image
        ByteArrayOutputStream compressedImageStream = new ByteArrayOutputStream();

        // Try different compression qualities until file size is under 2MB
        float quality = 0.9f;
        byte[] compressedData;

        do {
            compressedImageStream.reset();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) {
                throw new IllegalStateException("No JPEG writers found");
            }

            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(compressedImageStream)) {
                writer.setOutput(imageOutputStream);
                writer.write(null, new IIOImage(resizedImage, null, null), param);
            }
            writer.dispose();

            compressedData = compressedImageStream.toByteArray();
            quality -= 0.1f; // Reduce quality for next iteration

        } while (compressedData.length > MAX_FILE_SIZE && quality > 0.1f);

        return compressedData;
    }

    /**
     * Resize image if it exceeds maximum dimensions
     */
    private BufferedImage resizeImageIfNeeded(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (originalWidth <= MAX_WIDTH && originalHeight <= MAX_HEIGHT) {
            return originalImage;
        }

        // Calculate new dimensions maintaining aspect ratio
        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth, newHeight;

        if (originalWidth > originalHeight) {
            newWidth = Math.min(originalWidth, MAX_WIDTH);
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newHeight = Math.min(originalHeight, MAX_HEIGHT);
            newWidth = (int) (newHeight * aspectRatio);
        }

        // Create resized image
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();

        // Set rendering hints for better quality
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();

        return resizedImage;
    }

    /**
     * Upload profile image with compression
     */
    public String uploadProfileImage(MultipartFile file, UUID userId) throws IOException {
        return uploadImage(file, "users/profiles/" + userId);
    }

    /**
     * Upload gallery image with compression
     */
    public String uploadGalleryImage(MultipartFile file, UUID userId, int imageNumber) throws IOException {
        return uploadImage(file, "users/gallery/" + userId + "/image" + imageNumber);
    }

    /**
     * Upload service image with compression
     */
    public String uploadServiceImage(MultipartFile file, UUID serviceId) throws IOException {
        return uploadImage(file, "services/" + serviceId);
    }

    /**
     * Upload portfolio image with compression
     */
    public String uploadPortfolioImage(MultipartFile file, UUID portfolioId, int imageIndex) throws IOException {
        return uploadImage(file, "portfolios/" + portfolioId + "/image" + imageIndex);
    }

    /**
     * Upload testimonial client image with compression
     */
    public String uploadTestimonialImage(MultipartFile file, UUID testimonialId) throws IOException {
        return uploadImage(file, "testimonials/" + testimonialId);
    }

    /**
     * Upload blog featured image with compression
     */
    public String uploadBlogImage(MultipartFile file, UUID blogId) throws IOException {
        return uploadImage(file, "blogs/" + blogId);
    }

    /**
     * Upload achievement badge image with compression
     */
    public String uploadAchievementBadge(MultipartFile file, UUID achievementId) throws IOException {
        return uploadImage(file, "achievements/" + achievementId);
    }

    /**
     * Upload team member image with compression
     */
    public String uploadTeamMemberImage(MultipartFile file, UUID teamMemberId) throws IOException {
        return uploadImage(file, "team/" + teamMemberId);
    }

    /**
     * Upload event image with compression
     */
    public String uploadEventImage(MultipartFile file, UUID eventId) throws IOException {
        return uploadImage(file, "events/" + eventId);
    }

    /**
     * Delete image from Cloudinary
     */
    public boolean deleteImage(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                return false;
            }

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
            String transformation = "c_fill,w_" + width + ",h_" + height + ",q_auto,f_auto";
            return originalUrl.replace("/upload/", "/upload/" + transformation + "/");
        } catch (Exception e) {
            e.printStackTrace();
            return originalUrl;
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

    /**
     * Get large size URL (800x800)
     */
    public String getLargeImageUrl(String originalUrl) {
        return getResizedImageUrl(originalUrl, 800, 800);
    }

    /**
     * Get compressed file size information
     */
    public long getCompressedFileSize(MultipartFile file) throws IOException {
        byte[] compressedData = compressImage(file);
        return compressedData.length;
    }
}