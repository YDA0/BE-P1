package com.github.project1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 폼 표시
    @GetMapping("/createMember")
    public String getJoin(Model model) {
        model.addAttribute("member", new MemberDto()); // 빈 객체 추가
        return "joinMember"; // 회원가입 페이지의 HTML 파일 경로
    }

    // 회원가입 처리
    @PostMapping("/createMember")
    public String joinMember(@ModelAttribute("member") @Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // 유효성 검증 실패 시
            return "joinMember"; // 오류가 있을 경우, 폼을 다시 보여줌
        }
        // 회원가입 처리
        memberService.saveDto(memberDto);
        return "redirect:/login"; // 회원가입 성공 시 로그인 페이지로 이동
    }

    // 로그인 폼 표시
    @GetMapping("/login")
    public String getLogin(HttpServletRequest request, Model model) {
        // 이전 페이지 URL을 세션에 저장
        String referer = request.getHeader("Referer");
        if (referer != null) {
            request.getSession().setAttribute("prevPage", referer);
        }
        log.info("url={}", referer);
        model.addAttribute("login", new LoginDto());
        return "login"; // 로그인 페이지의 HTML 파일 경로
    }

    // 로그인 처리
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("login") LoginDto loginDto, HttpServletRequest request, HttpSession session, Model model) {
        boolean login = memberService.login(loginDto);

        if (login) {
            String email = loginDto.getEmail();
            Member member = memberService.findByEmail(email);
            session.setAttribute("loginMember", member); // 로그인 후 세션에 정보 저장

            // 저장된 이전 페이지 주소를 가져오고 기본값 설정
            String prevPage = (String) request.getSession().getAttribute("prevPage");
            if (prevPage == null || prevPage.isEmpty()) {
                prevPage = "/";
            }
            // 세션에 페이지 주소 삭제
            request.getSession().removeAttribute("prevPage");
            return "redirect:" + prevPage;
        }

        model.addAttribute("error", "비밀번호 또는 이메일이 올바르지 않습니다.");
        return "login"; // 실패 시 로그인 폼으로 다시 이동
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginMember");
        return "redirect:/"; // 메인 페이지로 이동
    }
}
