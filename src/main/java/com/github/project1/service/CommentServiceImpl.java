package com.github.project1.service;

import com.github.project1.entity.Board;
import com.github.project1.entity.Comment;
import com.github.project1.entity.Member;
import com.github.project1.dto.CommentDto;
import com.github.project1.repository.CommentRepository;
import com.github.project1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardServiceImpl boardService;
    private final MemberServiceImpl memberService;

    @Override
    public Comment save(CommentDto commentDto) {

        Long boardId = commentDto.getBoardDto().getBoardId();
        String email = commentDto.getMemberDto().getEmail();

        // 작성자와 게시판 정보 조회
        Member byEmail = memberService.findByEmail(email);
        Board byBoardId = boardService.findByBoardId(boardId);

        log.info("byEmail: {}", byEmail);
        log.info("byBoardId: {}", byBoardId);

        if (byEmail == null) {
            throw new IllegalArgumentException("Member not found");
        }

        if (byBoardId == null) {
            throw new IllegalArgumentException("Board not found");
        }

        // 댓글 생성 및 저장
        Comment build = Comment.builder()
                .content(commentDto.getContent())
                .dateTime(LocalDateTime.now())
                .member(byEmail)
                .board(byBoardId)
                .build();

        Comment save = commentRepository.save(build);
        log.info("Saved Comment: {}", save);

        // 댓글 수 업데이트
        setComment(byBoardId.getBoardId());  // 댓글 수 업데이트 메서드 호출

        return save;
    }

    @Override    //댓글 모두 보여주기
    public List<Comment> findAllComment(Long boardId) {
        List<Comment> byBoardId = commentRepository.findByBoard_Id(boardId);
        return byBoardId;
    }

    @Override //댓글수 카운트
    public Integer countComment(Long boardId) {
        try{
            return commentRepository.countComment(boardId);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("값이 나오지 않았습니다.");
        }
    }

    private Board setComment(Long boardId){ // 총합 댓글 갯수 설정
        Integer countComment = countComment(boardId); // 댓글 수 가져오기
        Board byBoardId = boardService.findByBoardId(boardId); // 해당 boardId로 Board 객체 찾기
        byBoardId.setCount(countComment); // 댓글 수 설정
        return byBoardId;
    }
}
