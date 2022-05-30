package com.crud.library.service;

import com.crud.library.domain.Member;
import com.crud.library.exception.MemberNotFoundException;
import com.crud.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberDbService {

    @Autowired
    private final MemberRepository memberRepository;

    public Member getMember(final Long memberId) throws MemberNotFoundException {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member saveMember(final Member member) {
        return memberRepository.save(member);
    }

    public void deleteMember(final Long memberId) {
        if (memberRepository.existsById(memberId)) {
            memberRepository.deleteById(memberId);
        }
    }
}
