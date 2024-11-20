package com.github.project1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

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
            return boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("Board not found with ID: " + boardId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while finding Board with ID: " + e);
        }
    }

    public Board countViews(Long boardId) {
        Board byBoardId = findByBoardId(boardId);
        Integer views = byBoardId.getViews();
        byBoardId.setViews(++views);
        return boardRepository.save(byBoardId);
    }
}
