package com.springdb2.springtx.apply;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxApplyBasicTest {

    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck(){
        log.info("AOP class={}",basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue(); //AOP 프록시 존재여부 확인
    }

    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {
        @Transactional
        public void tx(){
            log.info("call transaction");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();   //트랜잭션 활성화 여부 확인
            log.info("transaction active={}", txActive);
        }

        public void nonTx(){
            log.info("call Non transaction");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("transaction active={}", txActive);
        }
    }

}
