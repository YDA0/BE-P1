package com.github.project1.repository;

import com.github.project1.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

        Optional<Comment> findById(Long commentId);

        @Query("select c from Comment c where c.board.boardId = :boardId")
        List<Comment> findByBoard_Id(@Param("boardId") Long boardId);

        // 댓글 수 카운트 메서드 수정
        @Query("SELECT COUNT(c) from Comment c where c.board.boardId = :boardId")
        Integer countComment(@Param("boardId") Long boardId);

        @Modifying
        @Query("delete from Comment c where c.board.boardId = :boardId")
        void deleteByBoardId(@Param("boardId") Long boardId);
}