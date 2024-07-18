package springdb1.jdbc.service;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springdb1.jdbc.domain.Bank;
import springdb1.jdbc.repository.BankRepository2;

/**
 * 트랙잭션 - Spring의 트랜잭션 매니저
* */
@Slf4j
@RequiredArgsConstructor
public class BankService2 {

    private final PlatformTransactionManager transactionManager;
    private final BankRepository2 bankRepository;


    public void accountTransfer(String fromId, String toId, int money) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            transfer(fromId, toId, money);
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }

    }

    private void transfer(String fromId, String toId, int money) throws SQLException {
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
