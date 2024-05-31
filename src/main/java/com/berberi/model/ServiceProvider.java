package com.berberi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_providers")
public class ServiceProvider implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NonNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(name = "password", nullable = false)
    private String password;

    @NonNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NonNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "address")
    private String address;

    @Column(name = "business_phone")
    private String businessPhone;

    @Column(name = "sector")
    private String sector;

    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(name = "service_hours", joinColumns = @JoinColumn(name = "service_provider_id"))
    @Column(name = "hours")
    private List<String> hours;

    @ElementCollection
    @CollectionTable(name = "service_photos", joinColumns = @JoinColumn(name = "service_provider_id"))
    @Column(name = "photo_url")
    private List<String> servicePhotos;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ElementCollection
    @CollectionTable(name = "team_members", joinColumns = @JoinColumn(name = "service_provider_id"))
    @Column(name = "member_name")
    private List<String> teamMembers;

    @Column(name = "specialties")
    private String specialties;

    @Column(name = "promotions")
    private String promotions;

    @Column(name = "number_of_services")
    private Integer numberOfServices;

    @Column(name = "verification_code")
    private String verificationCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "code_generated_time")
    private Date codeGeneratedTime;

    @Column(name = "verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean verified;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceCategory> serviceCategories;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceRating> serviceRatings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
