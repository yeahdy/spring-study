package springdb1.jdbc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import springdb1.jdbc.domain.Bank;
import springdb1.jdbc.repository.BankRepository3;

@Slf4j
@SpringBootTest
class BankService3Test {

    public static final String HANA_BANK = "HANA";
    public static final String WOORIE_BANK = "WOORIE";
    public static final String BANK_EX = "ex";

    @Qualifier("bankRepository3Imple")
    @Autowired
    private BankRepository3 bankRepository;
    @Autowired
    private BankService3 bankService;

    @AfterEach
    void afterEach() {
        bankRepository.delete(HANA_BANK);
        bankRepository.delete(WOORIE_BANK);
        bankRepository.delete(BANK_EX);
    }

    @Test
    @DisplayName("정상 이체 테스트")
    void accountTransferTest() {
        //given
        Bank hana = new Bank(HANA_BANK, 20000);
        Bank woorie = new Bank(WOORIE_BANK, 20000);
        bankRepository.save(hana);
        bankRepository.save(woorie);

        //when
        log.info("♣트랜잭션 시작!");
        bankService.accountTransfer(hana.getBankId(), woorie.getBankId(), 5000);
        log.info("♣트랜잭션 종료!");

        //then
        Bank findA = bankRepository.findById(hana.getBankId());
        Bank findB = bankRepository.findById(woorie.getBankId());
        assertThat(findA.getMoney()).isEqualTo(15000);
        assertThat(findB.getMoney()).isEqualTo(25000);
    }

    @Test
    @DisplayName("이체 중 에러가 발생하면 롤백되어 이체가 되지 않는다.")
    void accountTransferRollbackTest() {
        //given
        Bank hana = new Bank(HANA_BANK, 20000);
        Bank bankEX = new Bank(BANK_EX, 20000);
        bankRepository.save(hana);
        bankRepository.save(bankEX);

        //when
        assertThatThrownBy(() ->
                bankService.accountTransfer(hana.getBankId(), bankEX.getBankId(), 5000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Bank findA = bankRepository.findById(hana.getBankId());
        Bank findEX = bankRepository.findById(bankEX.getBankId());
        assertThat(findA.getMoney()).isEqualTo(20000);
        assertThat(findEX.getMoney()).isEqualTo(20000);
    }

    @Test
    void aopCheck(){
        log.info("bankRepository class= {}", bankRepository.getClass());
        log.info("bankService class= {}",bankService.getClass());

        assertThat(AopUtils.isAopProxy(bankRepository)).isTrue();
        assertThat(AopUtils.isAopProxy(bankService)).isTrue();
    }

}