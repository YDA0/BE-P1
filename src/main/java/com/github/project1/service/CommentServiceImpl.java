package com.github.project1.service;

import com.github.project1.entity.Board;
import com.github.project1.entity.Comment;
import com.github.project1.entity.Member;
import com.github.project1.dto.CommentDto;
import com.github.project1.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void updateComment(CommentDto commentDto) {
        Long id = commentDto.getId();
        String email = commentDto.getMemberDto().getEmail();

        // 이메일로 회원을 찾고, 댓글을 수정할 회원을 지정
        Member member = memberService.findByEmail(email);
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            // 수정할 댓글의 내용과 회원을 업데이트
            comment.setContent(commentDto.getContent()); // 댓글 내용 수정
            comment.setUpdateDateTime(LocalDateTime.now()); // 수정 시간 설정

            // 댓글의 작성자를 해당 회원으로 설정
            comment.setMember(member);

            // 댓글을 DB에 저장 (수정된 댓글 반영)
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("댓글을 찾을 수 없습니다.");
        }
    }

    @Transactional
    @Override
    public void deleteComment(CommentDto commentDto) {
        // 이메일을 통해 회원을 찾기
        String email = commentDto.getMemberDto().getEmail(); // 이름이 아닌 이메일을 사용해야 함
        Member member = memberService.findByEmail(email); // 이메일로 회원 찾기

        // 댓글 ID를 통해 댓글을 찾기
        Optional<Comment> optionalComment = commentRepository.findById(commentDto.getId());

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();

            // 댓글이 존재하고, 작성자 이메일이 맞는지 확인
            if (comment.getMember().getEmail().equals(member.getEmail())) {
                // 작성자 본인일 경우 댓글 삭제
                commentRepository.delete(comment);
            } else {
                throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
            }
        } else {
            throw new RuntimeException("댓글을 찾을 수 없습니다.");
        }
    }


    private Board setComment(Long boardId){ // 총합 댓글 갯수 설정
        Integer countComment = countComment(boardId); // 댓글 수 가져오기
        Board byBoardId = boardService.findByBoardId(boardId); // 해당 boardId로 Board 객체 찾기
        byBoardId.setCount(countComment); // 댓글 수 설정
        return byBoardId;
    }
}
