package springdb1.jdbc.service;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import springdb1.jdbc.domain.Bank;
import springdb1.jdbc.repository.BankRepository2;

/**
 * 트랙잭션 - Spring의 트랜잭션 매니저
* */
@Slf4j
@RequiredArgsConstructor
public class BankService2_1 {

    private final TransactionTemplate transactionTemplate;
    private final BankRepository2 bankRepository;

    //NOTE: TransactionTemplate을 빈으로 등록하고 주입받아도 되지만, TransactionTemplate은 Class 이기 때문에 유연성이 떨어진다.
    // PlatformTransactionManager는 interface로 여러 의존성을 주입받을수있다.
    public BankService2_1(PlatformTransactionManager transactionManager, BankRepository2 bankRepository) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.bankRepository = bankRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) {
        transactionTemplate.executeWithoutResult((status)-> {
            try {
                transfer(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
        //NOTE: 템플릿 콜백패턴 (트랜잭션 시작, 커밋or롤백, 트랙잭션 종료 수행)
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
