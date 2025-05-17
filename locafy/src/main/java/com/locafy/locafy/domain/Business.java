package com.locafy.locafy.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Business {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "business_seq"
    )
    @SequenceGenerator(
            name = "business_seq",
            sequenceName = "business_seq",
            allocationSize = 1
    )
    @Column(updatable = false)
    private Long id;

    @Column(nullable=false, columnDefinition = "Text")
    private String businessName;

    @Column(nullable=false, unique=true, columnDefinition = "Text")
    private String phoneNumber;

    @Column(nullable=false, unique=true, columnDefinition = "Text")
    private String email;

    @Column(columnDefinition = "Text")
    private String address;

    @Column(unique=true, columnDefinition = "Text")
    private String website;

    @Column(unique=true, columnDefinition = "Text")
    private String description;

    // Foreign key: Many businesses to one owner

    @ManyToOne  // so one owner can have many businesses.
                //many businesses can have only one owner
    @JoinColumn(name = "owner_id", nullable = false)
    private BusinessOwner owner;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Favorites> favorites;

    @OneToMany(mappedBy = "business", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reviews> reviews;

    Business(String businessName, String phoneNumber, String email, String address, String website, String description) {
        this.businessName = businessName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.website = website;
        this.description = description;
    }
}
