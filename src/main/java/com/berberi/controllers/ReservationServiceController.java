//package com.berberi.controllers;
//
//import com.berberi.services.ServiceDTO;
//import com.berberi.services.ServiceProviderProfileService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/services")
//public class ReservationServiceController {
//
//    private final ServiceProviderProfileService serviceProviderProfileService;
//
//    @Autowired
//    public ReservationServiceController(ServiceProviderProfileService serviceProviderProfileService) {
//        this.serviceProviderProfileService = serviceProviderProfileService;
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<ServiceDTO>> searchServices(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String category,
//            @RequestParam(required = false) BigDecimal minPrice,
//            @RequestParam(required = false) BigDecimal maxPrice,
//            @RequestParam(required = false) Integer minRating,
//            @RequestParam(required = false) Integer maxRating
//    ) {
//        List<ServiceDTO> services = serviceProviderProfileService.searchServices(name, category, minPrice, maxPrice, minRating, maxRating);
//        return ResponseEntity.ok(services);
//    }
//
//    // Ajoutez d'autres méthodes du contrôleur selon les besoins
//}
