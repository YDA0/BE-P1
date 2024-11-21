package com.github.project1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id; // 댓글 ID (Primary Key)
    private String content; // 댓글 내용

    private LocalDateTime dateTime; // 작성 시간

    private LocalDateTime updateDateTime; // 수정 시간

    private Long boardId; // 게시판 ID

    private Long memberId; // 작성자 ID

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BoardDto boardDto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MemberDto memberDto;
}
