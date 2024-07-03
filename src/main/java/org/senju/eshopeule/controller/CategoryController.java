package org.senju.eshopeule.controller;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.CategoryDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getCategoryWithId(@RequestParam("id") String id) {
        try {
            return ResponseEntity.ok(categoryService.getById(id));
        } catch (NotFoundException ex) {
            return ResponseEntity.status(NOT_FOUND).body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Collection<? extends BaseResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @PostMapping
    public ResponseEntity<? extends BaseResponse> createNewCategory(@RequestBody CategoryDTO dto) {
        try {
            categoryService.createNewCategory(dto);
            return ResponseEntity.ok(new SimpleResponse("Create new category successfully!"));
        } catch (ObjectAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    public ResponseEntity<?> deleteById(@RequestParam("id") String id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
