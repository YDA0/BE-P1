package com.github.project1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "id")
    private Long userId;

    @Column(nullable = false, length = 20) // VARCHAR(20)
    private String password;

    @Column(nullable = false, length = 50) // VARCHAR(50)
    private String name;

    @Column(nullable = false, length = 40, unique = true) // VARCHAR(40), UNIQUE
    private String email;

    @Column(name = "phone_num", nullable = false, length = 15, unique = true) // 데이터베이스 컬럼 이름 매핑
    private String phoneNum;

    @Column(nullable = false, length = 100) // VARCHAR(100
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

}