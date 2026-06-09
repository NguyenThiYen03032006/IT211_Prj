package com.it211_prj.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.it211_prj.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService implements CloudStorageService {
    private final Cloudinary cloudinary;
    private final boolean configured;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name:}") String cloudName,
            @Value("${cloudinary.api-key:}") String apiKey,
            @Value("${cloudinary.api-secret:}") String apiSecret
    ) {
        this.configured = !cloudName.isBlank() && !apiKey.isBlank() && !apiSecret.isBlank();
        this.cloudinary = configured
                ? new Cloudinary(ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret))
                : null;
    }

    // Upload file len Cloudinary; neu chua cau hinh key thi tra URL demo de project van chay duoc khi hoc tap.
    @Override
    public UploadResult upload(MultipartFile file, String folder) {
        validate(file);
        if (!configured) {
            String publicId = folder + "/" + UUID.randomUUID();
            return new UploadResult("https://example.com/mock-storage/" + publicId, publicId);
        }
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folder, "resource_type", "auto"));
            return new UploadResult(String.valueOf(result.get("secure_url")), String.valueOf(result.get("public_id")));
        } catch (IOException ex) {
            throw new BadRequestException("Cannot upload file: " + ex.getMessage());
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        if (file.getSize() > 20 * 1024 * 1024) {
            throw new BadRequestException("File must be smaller than 20MB");
        }
    }
}
