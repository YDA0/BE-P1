package com.github.project1;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    // 게시글 저장 메서드 (BoardDto를 받아서 저장)
    Board save(BoardDto boardDto);

    // 모든 게시글을 반환하는 메서드
    List<Board> findAll();

    // 특정 게시글 ID를 사용하여 게시글 조회
    Board findByBoardId(Long boardId);

    // 게시글 수정 메서드 (BoardDto를 받아서 수정)
    Board updateBoard(BoardDto boardDto);

    boolean deleteBoard(Long id, String memberEmail);

    Page<Board> search(String title, Pageable pageable);
}