package com.github.project1.controllr;

import com.github.project1.service.CommentService;
import com.github.project1.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/comment")
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto) {
        try {
            commentService.save(commentDto);
            return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            log.error("댓글 작성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}

