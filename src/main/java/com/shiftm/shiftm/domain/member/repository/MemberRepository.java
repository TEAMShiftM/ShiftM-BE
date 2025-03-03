package com.shiftm.shiftm.domain.member.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByEmail(final String email);
    List<Member> findByIdIn(List<String> ids);
}
