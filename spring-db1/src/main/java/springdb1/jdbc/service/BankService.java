package springdb1.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springdb1.jdbc.domain.Bank;
import springdb1.jdbc.repository.BankRepository;

/**
 * 트랙잭션 - 파라미터 연동, 풀을 고려한 종료
* */
@Slf4j
@RequiredArgsConstructor
public class BankService {

    private final DataSource dataSource;
    private final BankRepository bankRepository;


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try{
            con.setAutoCommit(false);   //트랜잭션 시작
            transfer(con, fromId, toId, money);
            con.commit();   //트랜잭션 종료
        }catch (Exception e){
            con.rollback();
            throw new IllegalStateException(e);
        }finally {
            release(con);
        }

    }

    private void transfer(Connection con, String fromId, String toId, int money) throws SQLException {
        Bank fromBank = bankRepository.findById(con, fromId);
        Bank toBank = bankRepository.findById(con, toId);

        bankRepository.update(con, fromId, fromBank.getMoney() - money);
        validation(toBank);
        bankRepository.update(con, toId, toBank.getMoney() + money);
    }

    private void release(Connection con){
        if(con != null){
            try{
                con.setAutoCommit(true);    //커넥션 풀에 반납할때 재사용을 위해 자동커밋으로 변경 해준다.
            }catch (Exception e){
                log.info("ERROR",e);
            }
        }
    }

    private void validation(Bank toBank) {
        if(toBank.getBankId().equals("ex")){
            throw new IllegalStateException("Not enough money");
        }
    }

}
