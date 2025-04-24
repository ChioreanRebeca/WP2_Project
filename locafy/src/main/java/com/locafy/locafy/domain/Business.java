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
    @Column(updatable = false)
    private long id;

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

    @Lob
    private byte[] image;

    // Foreign key: Many businesses to one owner

    @ManyToOne  // so one owner can have many businesses.
                //many businesses can have only one owner
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
