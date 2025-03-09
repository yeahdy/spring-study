package com.springdb2.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired CallService callService;
    @Autowired InternalService internalService;

    @Test
    void externalCall(){
        callService.external();
    }

    @Test
    void defaultInternalCall(){
        internalService.defaultInternal();
    }

    @TestConfiguration
    static class InternalCallTestConfiguration {
        @Bean
        CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService() {
            return new InternalService();
        }
    }

    @RequiredArgsConstructor
    static class CallService {

        private final InternalService internalService;

        //트랜잭션 없음
        public void external(){
            log.info("call external");
            printTxInfo();  //call external > active=false
            internalService.internal();
        }
    }

    static class InternalService {
        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();  //tx active=true 트랜잭션 적용됨
        }

        @Transactional
        void defaultInternal(){ //스프링3.0 이후 부터는 default,protected 접근제어자에도 트랜잭션이 적용된다.
            log.info("call default internal");
            printTxInfo();  //tx active=true 트랜잭션 적용됨
        }

    }

    private static void printTxInfo(){
        boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("tx active={}", txActive);
    }

}

