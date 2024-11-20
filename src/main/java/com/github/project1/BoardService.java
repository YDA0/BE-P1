package com.github.project1;

import java.util.List;

public interface BoardService {

    // 게시글 저장 메서드 (BoardDto를 받아서 저장)
    Board save(BoardDto boardDto);

    // 모든 게시글을 반환하는 메서드
    List<Board> findAll();

    Board findByBoardId(Long boardId);
}