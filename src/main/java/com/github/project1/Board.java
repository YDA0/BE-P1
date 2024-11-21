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
    @Column(name = "board_id")
    private Long boardId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String writer;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, columnDefinition = "TEXT") // 긴 텍스트 처리
    private String content;

    private Integer views = 0; // 조회수 기본값을 0으로 설정

    private Integer count;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    // BoardDto의 값을 받아와서 엔티티를 업데이트하는 메소드
    public void update(BoardDto boardDto) {
        this.title = boardDto.getTitle();
        this.content = boardDto.getContent();
        this.writer = boardDto.getWriter();
        this.dateTime = boardDto.getDateTime();

        // BoardDto에서 views가 null인 경우 0으로 설정
        this.views = (boardDto.getViews() != null) ? boardDto.getViews() : 0; // views가 null이면 0으로 설정

        this.count = boardDto.getCount();
    }
}