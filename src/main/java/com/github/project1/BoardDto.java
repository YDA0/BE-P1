package com.github.project1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long boardId; // board_id (Board 엔티티의 PK)
    private String title;
    private String writer;
    private LocalDateTime dateTime;  // 작성 시간
    private String content;
    private Integer views;
    private Integer count;
    private Long userId; // user_id (Member 엔티티의 PK)

    private MemberDto memberDto;
}
