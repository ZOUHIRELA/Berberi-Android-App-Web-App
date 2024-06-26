package com.berberi.controllers;

import com.berberi.model.ServiceRating;
import com.berberi.model.User;
import com.berberi.repository.UserRepository;
import com.berberi.services.ServiceRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/service-ratings")
@RequiredArgsConstructor
public class ServiceRatingController {

    private final ServiceRatingService serviceRatingService;
    private final UserRepository userRepository;

    @PostMapping("/rate")
    public ResponseEntity<ServiceRating> rateService(@RequestBody ServiceRating serviceRating,
                                                     @AuthenticationPrincipal User user) {
        if (user != null) {
            serviceRating.setUser(user);
            ServiceRating ratedService = serviceRatingService.createServiceRating(serviceRating);
            return ResponseEntity.ok(ratedService);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceRating>> getAllRatings() {
        List<ServiceRating> allRatings = serviceRatingService.getAllServiceRatings();
        return ResponseEntity.ok(allRatings);
    }

    @GetMapping("/by-service-provider/{providerId}")
    public ResponseEntity<List<ServiceRating>> getRatingsByServiceProvider(@PathVariable int providerId) {
        List<ServiceRating> ratings = serviceRatingService.getServiceRatingsByServiceProviderId(providerId);
        return ResponseEntity.ok(ratings);
    }

    @PutMapping("/update/{ratingId}")
    public ResponseEntity<ServiceRating> updateRating(@PathVariable int ratingId, @RequestBody ServiceRating serviceRating) {
        ServiceRating updatedRating = serviceRatingService.updateServiceRating(ratingId, serviceRating);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/delete/{ratingId}")
    public ResponseEntity<?> deleteRating(@PathVariable int ratingId) {
        serviceRatingService.deleteServiceRating(ratingId);
        return ResponseEntity.ok().build();
    }
}