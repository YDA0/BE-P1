package com.github.project1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "board_id") // 데이터베이스의 컬럼 이름 매핑
    private Long boardId;

    @Column(nullable = false, length = 255) // 제목의 최대 길이 제한
    private String title;

    @Column(nullable = false, length = 255) // 작성자의 최대 길이 제한
    private String writer;

    @Column(nullable = false) // DATETIME 필드
    private LocalDateTime dateTime;

    @Column(nullable = false, columnDefinition = "TEXT") // 긴 텍스트 처리
    private String content;

    private Integer views; // 조회수

    private Integer count; // 카운트

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래 키 이름과 매핑
    private Member member;
}