package com.github.project1;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClickViewListener implements ApplicationListener<ViewEvent> {
    private final BoardServiceImpl boardService;

    @Override
    public void onApplicationEvent(ViewEvent event) {
        Board board = event.getBoard();
        boardService.countViews(board.getBoardId());
    }
}
