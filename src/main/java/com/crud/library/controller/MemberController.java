package com.crud.library.controller;

import com.crud.library.domain.Member;
import com.crud.library.domain.dto.MemberDto;
import com.crud.library.exception.MemberNotFoundException;
import com.crud.library.mapper.MemberMapper;
import com.crud.library.service.MemberDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberDbService service;
    private final MemberMapper memberMapper;

    @GetMapping(value = "{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId) throws MemberNotFoundException {
        return ok(memberMapper.mapToMemberDto(service.getMember(memberId)));
    }

    @GetMapping
    public ResponseEntity<List<MemberDto>> getMembers() {
        List<Member> members = service.getAllMembers();
        return ok(memberMapper.mapToMemberDtoList(members));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewMember(@RequestBody MemberDto memberDto) {
        Member member = memberMapper.mapToMember(memberDto);
        service.saveMember(member);
        return ok().build();
    }

    @PutMapping
    public ResponseEntity<MemberDto> updateMember(@RequestBody MemberDto memberDto) {
        Member member = memberMapper.mapToMember(memberDto);
        Member savedMember = service.saveMember(member);
        return ok(memberMapper.mapToMemberDto(savedMember));
    }

    @DeleteMapping(value = "{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        service.deleteMember(memberId);
        return ok().build();
    }
}