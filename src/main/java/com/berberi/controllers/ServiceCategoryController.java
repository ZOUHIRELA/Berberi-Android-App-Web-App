package com.berberi.controllers;

import com.berberi.model.ServiceCategory;
import com.berberi.services.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/service-categories")
@RequiredArgsConstructor
public class ServiceCategoryController {

    private final ServiceCategoryService serviceCategoryService;

    @PostMapping("/create")
    public ResponseEntity<ServiceCategory> createServiceCategory(@RequestBody ServiceCategory serviceCategory) {
        ServiceCategory createdCategory = serviceCategoryService.createServiceCategory(serviceCategory);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ServiceCategory> updateServiceCategory(@PathVariable int categoryId, @RequestBody ServiceCategory serviceCategory) {
        ServiceCategory updatedCategory = serviceCategoryService.updateServiceCategory(categoryId, serviceCategory);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteServiceCategory(@PathVariable int categoryId) {
        serviceCategoryService.deleteServiceCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceCategory>> getAllServiceCategories() {
        List<ServiceCategory> categories = serviceCategoryService.getAllServiceCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ServiceCategory> getServiceCategoryById(@PathVariable int categoryId) {
        Optional<ServiceCategory> category = serviceCategoryService.getServiceCategoryById(categoryId);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
