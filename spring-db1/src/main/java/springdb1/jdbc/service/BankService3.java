package springdb1.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springdb1.jdbc.domain.Bank;
import springdb1.jdbc.repository.BankRepository3;

/**
 * 예외 누수 문제 해결 (throws SQLException 제거)
 * 순수한 비즈니스 로직만 남게됨
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BankService3 {

    private final BankRepository3 bankRepository;

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        transfer(fromId, toId, money);
    }

    private void transfer(String fromId, String toId, int money) {
        Bank fromBank = bankRepository.findById(fromId);
        Bank toBank = bankRepository.findById(toId);

        bankRepository.update(fromId, fromBank.getMoney() - money);
        validation(toBank);
        bankRepository.update(toId, toBank.getMoney() + money);
    }


    private void validation(Bank toBank) {
        if(toBank.getBankId().equals("ex")){
            throw new IllegalStateException("Not enough money");
        }
    }

}
