package com.locafy.locafy.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Favorites {

    @Id
    @SequenceGenerator(
            name = "favorites_sequence",
            sequenceName = "favorites_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favorites_sequence"
    )
    @Column(updatable = false)
    private long id;

    @ManyToOne  //so one business can be added to favorites by multiple users
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "local_user_id", nullable = false)
    private Local localUser;
}
