package com.locafy.locafy.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Lob
    private byte[] data;

    @Setter
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

}

