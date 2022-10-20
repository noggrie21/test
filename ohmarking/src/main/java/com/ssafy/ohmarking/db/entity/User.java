package com.ssafy.ohmarking.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private Integer total_omr;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OMR> omrList = new ArrayList<>();
}