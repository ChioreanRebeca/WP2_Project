package com.locafy.locafy.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "business_owner")
@Data
@NoArgsConstructor
public class BusinessOwner {
    @Id
    @SequenceGenerator(
            name = "business_owner_seq",
            sequenceName = "business_owner_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "business_owner_seq"
    )
    @Column(name = "id", updatable = false)
    private long id;

    @Column(unique = true, nullable = false, columnDefinition = "Text")
    private String username;

    @Column(nullable = false, columnDefinition = "Text")
    private String password;

    @Column(nullable = false, columnDefinition = "Text")
    private String firstName;

    @Column(nullable = false, columnDefinition = "Text")
    private String lastName;

    @Column(unique = true, nullable = false, columnDefinition = "Text")
    private String email;

    @Column(unique = true, nullable = false, columnDefinition = "Text")
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "Text")
    private String address;

    public BusinessOwner(String username, String password, String firstName, String lastName, String email, String phoneNumber, String address) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

}
