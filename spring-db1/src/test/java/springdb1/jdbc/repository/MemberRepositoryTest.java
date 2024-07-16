package springdb1.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import springdb1.jdbc.domain.Member;

@Slf4j
class MemberRepositoryTest {

    MemberRepository repository = new MemberRepository();
    @Test
    void member_save_test() throws SQLException {
        Member member = new Member("elly", 50000);
        repository.save(member);

        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}",findMember);
        assertThat(findMember).isEqualTo(member);   //Lombok 내 EqualsAndHashCode에 의해 오버라이딩 되어서 같음
    }

    @Test
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