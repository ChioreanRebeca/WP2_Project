package com.locafy.locafy.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Reviews {

    @Id
    @SequenceGenerator(
            name = "reviews_seq",
            sequenceName = "reviews_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reviews_seq"
    )
    @Column(updatable = false)
    private long id;

    @Column( columnDefinition = "Text")
    private String message;

    @Column(nullable = false)
    private double stars;

    @ManyToOne
    @JoinColumn(name = "local_user_id", nullable = false)
    private Local local;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

}
