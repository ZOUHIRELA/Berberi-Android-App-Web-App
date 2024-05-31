package com.berberi.services;

import com.berberi.model.ServiceProvider;
import com.berberi.repository.ServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceProviderProfileService {

    private final ServiceProviderRepository serviceProviderRepository;

    @Transactional
    public ServiceProvider updateServiceProviderProfile(int serviceProviderId,
                                                        String fullName,
                                                        String email,
                                                        String password,
                                                        String phone,
                                                        String location,
                                                        MultipartFile profilePicture,
                                                        String businessName,
                                                        String address,
                                                        String businessPhone,
                                                        String sector,
                                                        String description,
                                                        List<String> hours,
                                                        List<String> servicePhotos,
                                                        Integer yearsOfExperience,
                                                        List<String> teamMembers,
                                                        String specialties,
                                                        String promotions,
                                                        Integer numberOfServices) throws IOException {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service Provider not found"));

        if (fullName != null) serviceProvider.setFullName(fullName);
        if (email != null) serviceProvider.setEmail(email);
        if (password != null) serviceProvider.setPassword(password);
        if (phone != null) serviceProvider.setPhone(phone);
        if (location != null) serviceProvider.setLocation(location);
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(profilePicture.getBytes());
            serviceProvider.setProfilePicture(Arrays.toString(base64Image.getBytes()));
        }
        if (businessName != null) serviceProvider.setBusinessName(businessName);
        if (address != null) serviceProvider.setAddress(address);
        if (businessPhone != null) serviceProvider.setBusinessPhone(businessPhone);
        if (sector != null) serviceProvider.setSector(sector);
        if (description != null) serviceProvider.setDescription(description);
        if (hours != null) serviceProvider.setHours(hours);
        if (servicePhotos != null) serviceProvider.setServicePhotos(servicePhotos);
        if (yearsOfExperience != null) serviceProvider.setYearsOfExperience(yearsOfExperience);
        if (teamMembers != null) serviceProvider.setTeamMembers(teamMembers);
        if (specialties != null) serviceProvider.setSpecialties(specialties);
        if (promotions != null) serviceProvider.setPromotions(promotions);
        if (numberOfServices != null) serviceProvider.setNumberOfServices(numberOfServices);

        return serviceProviderRepository.save(serviceProvider);
    }

    public ServiceProvider getServiceProviderByEmail(String email) {
        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository.findByEmail(email);
        return optionalServiceProvider.orElse(null);
    }

}
