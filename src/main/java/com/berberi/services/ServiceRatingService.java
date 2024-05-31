package com.berberi.services;

import com.berberi.model.ServiceRating;
import com.berberi.repository.ServiceRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRatingService {

    private final ServiceRatingRepository serviceRatingRepository;

    public ServiceRating createServiceRating(ServiceRating serviceRating) {
        return serviceRatingRepository.save(serviceRating);
    }

    public ServiceRating updateServiceRating(int ratingId, ServiceRating serviceRating) {
        ServiceRating existingRating = serviceRatingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Service Rating not found."));
        existingRating.setStars(serviceRating.getStars());
        existingRating.setComment(serviceRating.getComment());
        return serviceRatingRepository.save(existingRating);
    }

    public void deleteServiceRating(int ratingId) {
        serviceRatingRepository.deleteById(ratingId);
    }

    public List<ServiceRating> getAllServiceRatings() {
        return serviceRatingRepository.findAll();
    }

    public List<ServiceRating> getServiceRatingsByServiceProviderId(int serviceProviderId) {
        return serviceRatingRepository.findByServiceProviderId(serviceProviderId);
    }
}
