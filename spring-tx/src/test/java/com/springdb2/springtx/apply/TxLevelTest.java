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
public class TxLevelTest {

    @Autowired LevelService levelService;

    @Test
    void orderTest (){
        levelService.write();
        levelService.read();
    }

    @TestConfiguration
    static class TxLevelTestConfig {
        @Bean
        LevelService levelService(){
            return new LevelService();
        }
    }

    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {
        @Transactional  //(readOnly = false) 붙이지 않아도됨
        public void write(){
            log.info("call write");
            printInfo();    //readOnly active=false
        }

        //클래스에 선언된 트랜잭션이 우선순위를 가져서 메소드에 자동적용된다.
        public void read(){
            log.info("call read");
            printInfo();    //readOnly active=true
        }

        private void printInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}",txActive);
            boolean readOnlyActive = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("readOnly active={}",readOnlyActive);
        }

    }
}
