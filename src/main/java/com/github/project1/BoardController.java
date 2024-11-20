package com.github.project1;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public String board(Model model, HttpSession session) {
        getSession(model, session);

        List<Board> boardAll = boardService.findAll(); // boardService 사용하여 게시글 목록 가져오기
        model.addAttribute("boardAll", boardAll);

        return "board";
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

    private static void getSession(Model model, HttpSession session) { // 로그인한 사용자 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
    }
}
