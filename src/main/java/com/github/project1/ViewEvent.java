package com.github.project1;

import com.github.project1.entity.Board;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ViewEvent extends ApplicationEvent {
    private Board board;
    public ViewEvent(Board board) {
        super(board);
        this.board = board;
    }
}
