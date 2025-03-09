package com.springdb2.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InternalCallV1Test {

    @Autowired CallService callService;

    @Test
    void internalCall(){
        callService.internal(); //트랜잭션 적용한 메소드 호출
    }

    @Test
    void externalCall(){
        callService.external();
    }

    @TestConfiguration
    static class InternalCallTestConfiguration {
        @Bean
        CallService callService() {
            return new CallService();
        }
    }

    @Slf4j
    static class CallService {
        //트랜잭션 없음
        public void external(){
            log.info("call external");
            printTxInfo();  //call external > active=false
            internal(); //내부에서 트랜잭션이 적용된 메소드 호출
        }

        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();  //call internal > tx active=false
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

}

