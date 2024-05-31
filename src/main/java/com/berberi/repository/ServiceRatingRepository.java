package com.berberi.repository;

import com.berberi.model.ServiceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRatingRepository extends JpaRepository<ServiceRating, Integer> {
    List<ServiceRating> findByServiceProviderId(int serviceProviderId);
}
