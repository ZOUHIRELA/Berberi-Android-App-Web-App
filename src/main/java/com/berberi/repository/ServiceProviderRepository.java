package com.berberi.repository;

import com.berberi.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {

    @Query("SELECT s FROM ServiceProvider s WHERE " +
            "(:serviceName IS NULL OR s.fullName LIKE %:serviceName%) " +
            "AND (:location IS NULL OR s.location LIKE %:location%) " +
            "AND (:address IS NULL OR s.address LIKE %:address%) " +
            "AND (:category IS NULL OR s.sector = :category) " +
            "AND (:minPrice IS NULL OR :minPrice <= (SELECT MIN(sc.price) FROM ServiceCategory sc WHERE sc.serviceProvider = s)) " +
            "AND (:maxPrice IS NULL OR :maxPrice >= (SELECT MAX(sc.price) FROM ServiceCategory sc WHERE sc.serviceProvider = s)) " +
            "AND (:minRating IS NULL OR :minRating <= (SELECT AVG(sr.stars) FROM ServiceRating sr WHERE sr.serviceProvider = s))")
    List<ServiceProvider> findByCriteria(
            @Param("serviceName") String serviceName,
            @Param("location") String location,
            @Param("address") String address,
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minRating") Integer minRating
    );

    Optional<ServiceProvider> findByEmail(String email);
}