package com.github.project1.service;

import com.github.project1.entity.Member;
import com.github.project1.dto.LoginDto;
import com.github.project1.dto.MemberDto;
import com.github.project1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member saveDto(MemberDto memberDto) {
        Member member = Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .phoneNum(memberDto.getPhoneNum())
                .address(memberDto.getAddress())
                .build();
        return saveEntity(member);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // 로그인 구현
    public boolean login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        Member byEmail = memberRepository.findByEmail(email);
        if (byEmail != null) {
            return byEmail.getPassword().equals(password);
        }
        return false;
    }
}
