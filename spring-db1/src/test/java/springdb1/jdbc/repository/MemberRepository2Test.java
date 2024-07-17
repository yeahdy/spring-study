package springdb1.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springdb1.jdbc.connection.ConnectionConst.*;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import springdb1.jdbc.domain.Member;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberRepository2Test {

    private MemberRepository2 repository;

    @BeforeEach
    void beforeEach() throws Exception {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        repository = new MemberRepository2(hikariDataSource);
    }

    @Test
    @Order(1)
    void member_save_test() throws SQLException {
        Member member = new Member("elly", 50000);
        repository.save(member);

        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}",findMember);
        assertThat(findMember).isEqualTo(member);  
    }

    @Test
    @Order(2)
    void member_update_test() throws SQLException {
        //given
        String memberId = "elly";
        Member member = repository.findById(memberId);

        //when
        repository.update(member.getMemberId(), 20000);

        //then
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);
    }

    @Test
    void member_delete_test() throws SQLException {
        //given
        String memberId = "elly";

        //when
        repository.delete(memberId);

        //then
        assertThatThrownBy(() -> repository.findById(memberId))
                .isInstanceOf(NoSuchElementException.class);
    }

}