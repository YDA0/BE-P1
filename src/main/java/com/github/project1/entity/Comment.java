package com.github.project1.entity;

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
@Table(name = "comments") // 테이블 이름 매핑
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id") // Primary Key
    private Long id;

    @Column(name = "comment_content", nullable = false) // NOT NULL 제약 조건
    private String content;

    @Column(name = "dateTime", updatable = false) // 작성 시간, INSERT 후 변경되지 않음
    private LocalDateTime dateTime;

    @Column(name = "updateDateTime") // 수정 시간
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id", nullable = false) // 외래 키: board_id
    private Board board;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키: user_id
    private Member member; // `user` 테이블과 매핑된 엔티티
}