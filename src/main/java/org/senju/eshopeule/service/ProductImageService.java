package org.senju.eshopeule.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    String getImageUrlById(String id);

    List<String> getAllImageUrlByProductId(String productId);

    void uploadImage(MultipartFile[] images, String productId);

    void deleteById(String id);

    void deleteByProductId(String productId);
}
