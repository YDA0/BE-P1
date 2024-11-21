package com.github.project1.service;

import com.github.project1.entity.Comment;
import com.github.project1.dto.CommentDto;

import java.util.List;

public interface CommentService {

    Comment save(CommentDto commentDto);

    List<Comment> findAllComment(Long boardId);

    Integer countComment(Long boardId);
}