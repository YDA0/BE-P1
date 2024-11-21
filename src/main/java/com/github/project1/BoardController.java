package com.github.project1;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/board")
    public String board(
            Model model,
            @PageableDefault(size = 4) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String title,
            HttpSession session
    ) {
        // 세션 정보 가져오기
        getSession(model, session);

        // 검색 및 페이징 처리
        Page<Board> boards = boardService.search(title, pageable); // 검색어가 없는 경우 모든 데이터 반환
        List<Board> boardAll = boards.getContent(); // 현재 페이지에 해당하는 데이터

        // 페이징 관련 정보 계산
        int currentPage = boards.getPageable().getPageNumber() + 1;
        int totalPages = boards.getTotalPages();
        int visiblePages = 3;
        int startPage = Math.max(1, currentPage - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // 검색 결과가 있을 경우 추가
        if (boardAll != null) {
            model.addAttribute("boardAll", boardAll);
        }

        // 페이징 및 검색 관련 속성 추가
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", boards);
        model.addAttribute("title", title); // 검색어 필드 유지

        return "board"; // board.html 템플릿 반환
    }

    @GetMapping("/boardWrite")
    public String write(Model model, HttpSession session) {
        getSession(model, session);
        model.addAttribute("board", new BoardDto());
        return "boardWrite";
    }

    @PostMapping("/boardWrite")
    @ResponseBody
    public ResponseEntity<?> writing(@Valid @RequestBody BoardDto boardDto, BindingResult result) {
        try{
            if(result.hasErrors()){
                Map<String, String> errorMessages = new HashMap<>();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
            }
            boardService.save(boardDto); // boardService 사용하여 게시글 저장
            return ResponseEntity.ok("성공");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류발생");
        }
    }

    @GetMapping("/board/{boardId}")
    public String boardInfo(@PathVariable(name = "boardId") Long boardId, Model model, HttpSession session) {
        Board byBoardId = boardService.findByBoardId(boardId);
        eventPublisher.publishEvent(new ViewEvent(byBoardId)); // 조회 시 카운터 증가

        getSession(model, session);

        model.addAttribute("board", byBoardId);
        return "boardInfo";
    }

    @GetMapping("/board/update/{id}")
    public String updateGetBoard(@PathVariable(name = "id") Long id, Model model) {
        Board byBoardId = boardService.findByBoardId(id);
        model.addAttribute("board", byBoardId);
        return "boardUpdate";
    }

    @PutMapping("/board/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateBoardAfter(@PathVariable(name = "id") Long id, @Valid @RequestBody BoardDto boardDto) {
        try {
            // boardDto에 id 값을 직접 세팅하여 서비스에 전달
            boardDto.setBoardId(id);  // PathVariable로 받은 id를 boardDto에 설정

            // boardService를 통해 업데이트
            Board updatedBoard = boardService.updateBoard(boardDto);
            return ResponseEntity.ok(updatedBoard);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생");
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> boardDelete(@RequestBody BoardDto boardDto) {
        try{
            Long id = boardDto.getBoardId();
            String memberEmail = boardDto.getMemberDto().getEmail();
            boolean deleteBoard = boardService.deleteBoard(id, memberEmail);
            if (!deleteBoard) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록된 사용자만 삭제 가능");
            }
            return ResponseEntity.ok("삭제 성공");
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물이 존재하지 않습니다.");
        }
    }

    private static void getSession(Model model, HttpSession session) { // 로그인한 사용자 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
    }
}
