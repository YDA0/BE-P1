package com.github.project1.controllr;

import com.github.project1.service.CommentService;
import com.github.project1.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/board/comment/update")
    public ResponseEntity<String> updateComment(@RequestBody CommentDto commentDto) {
        try {
            commentService.updateComment(commentDto);
            return ResponseEntity.ok("댓글 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("댓글 수정 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/comment")
    public ResponseEntity<String> deleteComment(@RequestBody CommentDto commentDto) {
        try {
            commentService.deleteComment(commentDto);
            return ResponseEntity.ok("댓글 삭제 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 삭제 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("댓글 삭제 실패: " + e.getMessage());
        }
    }
}

