package com.github.project1;

public interface MemberService {
    Member saveEntity(Member member);
    Member saveDto(MemberDto memberDto);

    Member findByEmail(String email);

    boolean login(LoginDto loginDto);
}
