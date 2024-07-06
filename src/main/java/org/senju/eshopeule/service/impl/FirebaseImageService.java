package org.senju.eshopeule.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FirebaseImageService implements ImageService {

    @Value("${firebase.image-url}")
    private String imageUrl;

    @Override
    public String getImageUrl(String name) {
        return String.format(imageUrl, name);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = this.generateFileName(file.getOriginalFilename());
        bucket.create(name, file.getBytes(), file.getContentType());
        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = this.generateFileName(originalFileName);
        byte[] bytes = this.getByteArrays(bufferedImage, originalFileName);
        bucket.create(name, bytes);
        return name;
    }

    @Override
    public void delete(String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        if (name.isEmpty()) throw new IOException("Invalid file name");
        Blob blob = bucket.get(name);
        if (blob != null) blob.delete();
    }


    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + getFileExtension(originalFileName);
    }

    private byte[] getByteArrays(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, byteStream);
            byteStream.flush();
            return byteStream.toByteArray();
        }
    }

    private String getFileExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }
}
