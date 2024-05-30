package com.berberi.repository;

import com.berberi.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Integer> {

//    @Query("SELECT s FROM ServiceProvider s WHERE (:name IS NULL OR s.fullName LIKE %:name%) " +
//            "AND (:category IS NULL OR s.category = :category) " +
//            "AND (:minPrice IS NULL OR s.price >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR s.price <= :maxPrice) " +
//            "AND (:minRating IS NULL OR s.rating >= :minRating) " +
//            "AND (:maxRating IS NULL OR s.rating <= :maxRating)")
//    List<ServiceProvider> searchServices(@Param("name") String name,
//                                         @Param("category") String category,
//                                         @Param("minPrice") BigDecimal minPrice,
//                                         @Param("maxPrice") BigDecimal maxPrice,
//                                         @Param("minRating") Integer minRating,
//                                         @Param("maxRating") Integer maxRating);

    Optional<ServiceProvider> findByEmail(String email);
}
