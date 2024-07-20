package springdb1.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
class TestRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        TestRepository testRepository(){
            return new TestRepository();
        }
    }

    @Test
    @DisplayName("TestRepository는 AOP 프록시를 생성한다")
    void aopCheck(){
        log.info("testRepository class= {}", testRepository.getClass());
        assertThat(AopUtils.isAopProxy(testRepository)).isTrue();
    }

}