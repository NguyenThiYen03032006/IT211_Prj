package com.it211_prj.service;

import org.springframework.web.multipart.MultipartFile;

// Abstraction de co the doi Cloudinary sang AWS S3 ma khong sua service nghiep vu.
public interface CloudStorageService {
    UploadResult upload(MultipartFile file, String folder);

    record UploadResult(String url, String publicId) {
    }
}
