package com.github.project1;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;
    private String writer;
    private LocalDateTime dateTime;  // 작성 시간
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;
    private Integer views;
    private Integer count;
    private Long userId; // user_id (Member 엔티티의 PK)

    private MemberDto memberDto;

    public void updateEntity(Board board) {
        board.update(this);
    }
}
