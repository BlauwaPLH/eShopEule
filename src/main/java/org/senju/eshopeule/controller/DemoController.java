package org.senju.eshopeule.controller;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.senju.eshopeule.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final ImageService imageService;
    private final SqlSession sqlSession;
//    private final ProductAnalysisService productAnalysisService;
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

    @GetMapping(path = "/api/p/v1/demo/mybatis")
    public ResponseEntity<?> demoMyBatis() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("orderByClause", "oi.total DESC");
        return ResponseEntity.ok(sqlSession.selectList("org.senju.mybatis.DemoXmlMapper.demo1", parameters));
    }
}
