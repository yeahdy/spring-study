package springdb1.jdbc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springdb1.jdbc.connection.ConnectionConst.PASSWORD;
import static springdb1.jdbc.connection.ConnectionConst.URL;
import static springdb1.jdbc.connection.ConnectionConst.USERNAME;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepository2;

class MemberServiceTest {

    public static final String MemberA = "personA";
    public static final String MemberB = "personB";
    public static final String MemberEX = "ex";

    private MemberRepository2 memberRepository;
    private MemberService memberService;

    @BeforeEach
    void beforeEach() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        memberRepository = new MemberRepository2(hikariDataSource);
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    void afterEach() throws SQLException {
        memberRepository.delete(MemberA);
        memberRepository.delete(MemberB);
        memberRepository.delete(MemberEX);
    }

    @Test
    @DisplayName("정상 이체 테스트")
    void accountTransferTest() throws SQLException {
        //given
        Member memberA = new Member(MemberA, 20000);
        Member memberB = new Member(MemberB, 20000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 5000);

        //then
        Member findA = memberRepository.findById(memberA.getMemberId());
        Member findB = memberRepository.findById(memberB.getMemberId());
        assertThat(findA.getMoney()).isEqualTo(15000);
        assertThat(findB.getMoney()).isEqualTo(25000);
    }

    @Test
    @DisplayName("이체 중 에러가 발생해서 memberA의 돈만 감소된다.")
    void accountTransferExceptionTest() throws SQLException {
        //given
        Member memberA = new Member(MemberA, 20000);
        Member memberEX = new Member(MemberEX, 20000);
        memberRepository.save(memberA);
        memberRepository.save(memberEX);

        //when
        assertThatThrownBy(() ->
                memberService.accountTransfer(memberA.getMemberId(), memberEX.getMemberId(), 5000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member findA = memberRepository.findById(memberA.getMemberId());
        Member findEX = memberRepository.findById(memberEX.getMemberId());
        assertThat(findA.getMoney()).isEqualTo(15000);  //memberA 의 돈만 감소
        assertThat(findEX.getMoney()).isEqualTo(20000);
    }

}