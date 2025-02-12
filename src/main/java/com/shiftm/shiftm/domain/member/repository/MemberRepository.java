package com.shiftm.shiftm.domain.member.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
