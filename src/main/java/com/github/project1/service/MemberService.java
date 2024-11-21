package com.github.project1.service;

import com.github.project1.entity.Member;
import com.github.project1.dto.LoginDto;
import com.github.project1.dto.MemberDto;

public interface MemberService {
    Member saveEntity(Member member);
    Member saveDto(MemberDto memberDto);

    Member findByEmail(String email);

    boolean login(LoginDto loginDto);
}
