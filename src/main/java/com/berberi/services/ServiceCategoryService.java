package com.berberi.services;

import com.berberi.model.ServiceCategory;
import com.berberi.repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCategoryService {

    private final ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCategory createServiceCategory(ServiceCategory serviceCategory) {
        return serviceCategoryRepository.save(serviceCategory);
    }

    public ServiceCategory updateServiceCategory(int categoryId, ServiceCategory serviceCategory) {
        ServiceCategory existingCategory = serviceCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Service Category not found."));
        existingCategory.setName(serviceCategory.getName());
        existingCategory.setDescription(serviceCategory.getDescription());
        return serviceCategoryRepository.save(existingCategory);
    }

    public void deleteServiceCategory(int categoryId) {
        serviceCategoryRepository.deleteById(categoryId);
    }

    public List<ServiceCategory> getAllServiceCategories() {
        return serviceCategoryRepository.findAll();
    }
}
