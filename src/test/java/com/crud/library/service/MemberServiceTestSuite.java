package com.crud.library.service;

import com.crud.library.domain.Member;
import com.crud.library.exception.MemberNotFoundException;
import com.crud.library.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class MemberServiceTestSuite {

    @Autowired
    private MemberDbService memberDbService;

    @Autowired
    private MemberRepository memberRepository;

    @Container
    private static MySQLContainer container = new MySQLContainer("mysql:latest");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @AfterEach
    void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    void testGetMember() throws MemberNotFoundException {
        //Given
        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        //When
        memberDbService.saveMember(member);
        Long id = member.getMemberId();

        //Then
        assertNotNull(memberDbService.getMember(id));
        assertEquals("John", memberDbService.getMember(id).getFirstName());
        assertEquals("Smith", memberDbService.getMember(id).getLastName());
    }

    @Test
    void testMemberNotFoundException() {
        //Given
        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        memberDbService.saveMember(member);
        Long id = member.getMemberId();

        //When
        Long nonexistentId = id + 1;

        //Then
        assertTrue(memberRepository.existsById(id));
        assertThrows(MemberNotFoundException.class, () -> memberDbService.getMember(nonexistentId));
    }

    @Test
    void testGetAllMembers() {
        //Given
        Member memberOne = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        Member memberTwo = Member.builder()
                .firstName("Anna")
                .lastName("Brown")
                .build();

        //When
        memberDbService.saveMember(memberOne);
        memberDbService.saveMember(memberTwo);

        //Then
        assertEquals(2, memberDbService.getAllMembers().size());
    }

    @Test
    void testMemberDateAccountCreatedNotNull() throws MemberNotFoundException {
        //Given
        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        //Given
        memberDbService.saveMember(member);
        Long id = member.getMemberId();

        //Then
        assertNotNull(memberDbService.getMember(id).getDateAccountCreated());
    }

    @Test
    void testDeleteMember() {
        //Given
        Member member = Member.builder()
                .firstName("John")
                .lastName("Smith")
                .build();

        memberDbService.saveMember(member);
        Long id = member.getMemberId();

        //When
        memberDbService.deleteMember(id);

        //Then
        assertFalse(memberRepository.existsById(id));
    }
}