package springdb1.jdbc.exception.translator;

import static springdb1.jdbc.connection.ConnectionConst.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.exeception.DbException;
import springdb1.jdbc.repository.exeception.DuplicateKeyException;

@Slf4j
public class ExTranslatorTest {

    private Repository repository;
    private Service service;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySave(){
        service.create("myId");
        service.create("myId");
    }


    @Slf4j
    @RequiredArgsConstructor
    static class Service {
        private final Repository repository;

        public void create(String memberId) {
            try {
                repository.save(new Member(memberId, 0));
            } catch (DuplicateKeyException de) {
                String retryId = generateNewId(memberId);
                log.info("키 중복 복구 :: {}", retryId);
                repository.save(new Member(retryId, 0));
            }catch (DbException e){
                throw e;
            }
        }

        private String generateNewId(String memberId) {
            return memberId + new Random().nextInt(1000);
        }
    }

    @RequiredArgsConstructor
    static class Repository {
        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";

            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            } catch (SQLException se) {
                if (se.getErrorCode() == 23505) {
                    throw new DuplicateKeyException(se);    //JDBC 예외를 커스텀예외로 변환해서 던짐 > 의존성 제거
                }
                throw new DbException(se);
            } finally {
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(con);
            }
        }
    }

}
