package com.locafy.locafy;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Local")
@Data
@NoArgsConstructor
@Table
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
    @Column(
            name = "id",
            updatable = false
    )
    private long id;

    private String userName;

    @Column(
            nullable = false,
            columnDefinition = "TEXT",
            unique = true
    )
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
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
