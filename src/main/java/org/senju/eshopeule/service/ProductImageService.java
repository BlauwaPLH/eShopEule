package org.senju.eshopeule.service;

import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ProductException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    String getImageUrlById(String id) throws NotFoundException;

    List<String> getAllImageUrlByProductId(String productId);

    void uploadImage(MultipartFile[] images, String productId) throws NotFoundException, ProductException;

    void deleteById(String id) throws NotFoundException, ProductException;

    void deleteByProductId(String productId) throws NotFoundException, ProductException;
}
