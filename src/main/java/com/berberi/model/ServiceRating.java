package com.berberi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_ratings")
public class ServiceRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider serviceProvider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public ServiceRating(Integer stars, String comment, ServiceProvider serviceProvider, User user) {
        this.stars = stars;
        this.comment = comment;
        this.serviceProvider = serviceProvider;
        this.user = user;
    }
}
