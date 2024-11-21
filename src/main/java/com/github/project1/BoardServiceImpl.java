package com.github.project1;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    public Board save(BoardDto boardDto) {
        String email = boardDto.getMemberDto().getEmail();
        Member byEmail = memberRepository.findByEmail(email);
        Board build = Board.builder()
                .title(boardDto.getTitle())
                .dateTime(LocalDateTime.now())
                .writer(byEmail.getEmail())
                .content(boardDto.getContent())
                .count(0)
                .member(byEmail)
                .views(0)
                .build();

        Board save = boardRepository.save(build);
        return save;
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }


    public Board findByBoardId(Long boardId) {
        try{
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("Board not found with ID: " + boardId));

            // views가 null일 경우 0으로 설정
            if (board.getViews() == null) {
                board.setViews(0);
            }

            return board;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while finding Board with ID: " + e);
        }
    }

    public Board countViews(Long boardId) {
        Board board = findByBoardId(boardId);

        // views가 null일 경우 0으로 초기화하고 증가
        if (board.getViews() == null) {
            board.setViews(0);
        }

        Integer views = board.getViews();
        board.setViews(++views);
        return boardRepository.save(board);
    }

    @Override
    public Board updateBoard(BoardDto boardDto) {
        // 기존 게시글 가져오기
        Board existingBoard = findByBoardId(boardDto.getBoardId());

        // DTO를 사용해 기존 엔티티를 업데이트
        boardDto.setWriter(existingBoard.getWriter()); // 작성자 유지
        boardDto.setDateTime(existingBoard.getDateTime()); // 작성일 유지
        boardDto.setViews(existingBoard.getViews()); // 조회수 유지

        boardDto.updateEntity(existingBoard);

        // 저장 후 반환
        return boardRepository.save(existingBoard);
    }

    @Override
    public boolean deleteBoard(Long boardId, String memberEmail) {
        Member byEmail = memberService.findByEmail(memberEmail);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시물이 존재하지 않습니다. ID: " + boardId));

        String writer = board.getWriter();
        if (writer == null || !writer.equals(memberEmail)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        boardRepository.deleteById(boardId);
        return true;
    }
}
