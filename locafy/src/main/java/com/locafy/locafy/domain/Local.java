package com.locafy.locafy.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

/*    @Getter
    @Setter*/
/*    @ElementCollection(fetch = FetchType.EAGER)  // To load roles eagerly from the database
    @CollectionTable(name = "local_roles", joinColumns = @JoinColumn(name = "local_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();  // Store roles as strings*/

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
