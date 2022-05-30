package com.crud.library.mapper;

import com.crud.library.domain.Member;
import com.crud.library.domain.dto.MemberDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberMapper {

    public Member mapToMember(final MemberDto memberDto) {
        return new Member(
                memberDto.getMemberId(),
                memberDto.getFirstName(),
                memberDto.getLastName(),
                memberDto.getDateAccountCreated(),
                memberDto.getLibrary()
        );
    }

    public MemberDto mapToMemberDto(final Member member) {
        return new MemberDto(
                member.getMemberId(),
                member.getFirstName(),
                member.getLastName(),
                member.getDateAccountCreated(),
                member.getLibrary()
        );
    }

    public List<MemberDto> mapToMemberDtoList(final List<Member> memberList) {
        return memberList.stream()
                .map(this::mapToMemberDto)
                .collect(Collectors.toList());
    }
}