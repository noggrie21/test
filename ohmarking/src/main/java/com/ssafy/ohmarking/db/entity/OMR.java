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
public class OMR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "omr_id")
    private Long id;

    @Column(nullable = false)
    private Integer check_cnt;

    @Column(nullable = false)
    private Integer page_num;

    @OneToMany(mappedBy = "omr", cascade = CascadeType.ALL)
    private List<Note> noteList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;
}