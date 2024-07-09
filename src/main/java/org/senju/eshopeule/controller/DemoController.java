package org.senju.eshopeule.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @PostMapping(path = "/api/p/v1/demo/saveImage")
    public ResponseEntity<?> saveNewImage(@RequestParam("file") MultipartFile[] files) {
        Arrays.stream(files).forEach(f -> {
            try {
                imageService.save(f);
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        });
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/api/p/v1/demo/deleteImage")
    public ResponseEntity<?> deleteImage() {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get("ef87c5e9-7507-488e-963a-4738d4c9e9f4png");
        blob.delete();
        return ResponseEntity.noContent().build();
    }
}
