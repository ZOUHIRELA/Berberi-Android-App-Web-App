package com.berberi.controllers;

import com.berberi.model.ServiceProvider;
import com.berberi.services.ServiceProviderProfileService;
import com.berberi.repository.ServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/service-provider/profile")
@RequiredArgsConstructor
public class ServiceProviderProfileController {

    private final ServiceProviderProfileService serviceProviderProfileService;
    private final ServiceProviderRepository serviceProviderRepository;

    @PutMapping("/update")
    public ResponseEntity<?> updateServiceProviderProfile(@RequestParam(value = "fullName", required = false) String fullName,
                                                          @RequestParam(value = "email", required = false) String email,
                                                          @RequestParam(value = "password", required = false) String password,
                                                          @RequestParam(value = "phone", required = false) String phone,
                                                          @RequestParam(value = "location", required = false) String location,
                                                          @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                                          @RequestParam(value = "businessName", required = false) String businessName,
                                                          @RequestParam(value = "address", required = false) String address,
                                                          @RequestParam(value = "businessPhone", required = false) String businessPhone,
                                                          @RequestParam(value = "sector", required = false) String sector,
                                                          @RequestParam(value = "description", required = false) String description,
                                                          @RequestParam(value = "hours", required = false) List<String> hours,
                                                          @RequestParam(value = "servicePhotos", required = false) List<String> servicePhotos,
                                                          @RequestParam(value = "yearsOfExperience", required = false) Integer yearsOfExperience,
                                                          @RequestParam(value = "teamMembers", required = false) List<String> teamMembers,
                                                          @RequestParam(value = "specialties", required = false) String specialties,
                                                          @RequestParam(value = "promotions", required = false) String promotions,
                                                          @RequestParam(value = "numberOfServices", required = false) Integer numberOfServices) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentEmail = userDetails.getUsername();

            ServiceProvider serviceProvider = serviceProviderProfileService.getServiceProviderByEmail(currentEmail);
            if (serviceProvider != null) {
                ServiceProvider updatedServiceProvider = serviceProviderProfileService.updateServiceProviderProfile(
                        serviceProvider.getId(), fullName, email, password, phone, location, profilePicture,
                        businessName, address, businessPhone, sector, description, hours, servicePhotos,
                        yearsOfExperience, teamMembers, specialties, promotions, numberOfServices);

                return ResponseEntity.ok(updatedServiceProvider);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Provider not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @GetMapping
    public ResponseEntity<?> getServiceProviderProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository.findByEmail(username);
            if (optionalServiceProvider.isPresent()) {
                ServiceProvider serviceProvider = optionalServiceProvider.get();
                return ResponseEntity.ok(serviceProvider);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service Provider not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}