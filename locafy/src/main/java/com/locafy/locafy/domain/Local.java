package com.locafy.locafy.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Local") //this makes a table
@Data //this creates automatically getters and setters and toString and equals functions
@NoArgsConstructor //this creates a non argument constructor, so I no longer need to write code
public class Local {
    @Id
    @SequenceGenerator(
            name = "local_sequence",
            sequenceName = "local_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "local_sequence"
    )
    @Column(name = "id", updatable = false)
    private long id;

    @Column(nullable = false, columnDefinition = "TEXT", unique = true)
    private String userName;

    @Column(nullable = false, columnDefinition = "TEXT", unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT", unique = true)
    private String password;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String firstName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String lastName;

    @Column(nullable = false, columnDefinition = "TEXT", unique = true)
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

public Local( String userName, String email, String password, String firstName, String lastName, String phoneNumber, String address) {

    this.userName = userName;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.address = address;

}


}
