package springdb1.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static springdb1.jdbc.connection.ConnectionConst.PASSWORD;
import static springdb1.jdbc.connection.ConnectionConst.URL;
import static springdb1.jdbc.connection.ConnectionConst.USERNAME;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootTest
class BankRepositoryTest {
    @Autowired
    private BankRepository2 bankRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        DataSource dataSource(){
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setJdbcUrl(URL);
            hikariDataSource.setUsername(USERNAME);
            hikariDataSource.setPassword(PASSWORD);
            return hikariDataSource;
        }
//        @Bean
//        BankRepository2 bankRepository(){
//            return new BankRepository2(dataSource());
//        }
    }


    @Test
    @DisplayName("BankRepository는 AOP 프록시를 생성한다")
    void aopCheck(){
        log.info("bankRepository class= {}", bankRepository.getClass());
        assertThat(AopUtils.isAopProxy(bankRepository)).isTrue();
    }

}