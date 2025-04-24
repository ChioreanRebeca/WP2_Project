package com.locafy.locafy.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private long id;

    @Column(nullable=false)
    private String businessName;

    @Column(nullable=false, unique=true)
    private String phoneNumber;

    @Column(nullable=false, unique=true)
    private String email;
    private String address;

    @Column(unique=true)
    private String website;

    @Lob
    private byte[] image;

    // Foreign key: Many businesses to one owner

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private BusinessOwner owner;

    Business(String businessName, String phoneNumber, String email, String address, String website, byte[] image) {
        this.businessName = businessName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.website = website;
        this.image = image;
    }
}
