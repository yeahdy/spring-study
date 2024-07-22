package springdb1.jdbc.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import springdb1.jdbc.domain.Bank;

/**
 * JdbcTemplate 사용
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@Qualifier("bankRepository5Imple")
public class BankRepository5Imple implements BankRepository3 {
    private final JdbcTemplate jdbcTemplate;

    public Bank save(Bank bank) {
        String sql = "insert into bank(bank_id, money) values(?,?)";
        jdbcTemplate.update(sql,bank.getBankId(), bank.getMoney());
        return bank;
    }

    public Bank findById(String bankId) {
        String sql = "select * from bank where bank_id = ?";
        return jdbcTemplate.queryForObject(sql, bankRowMapper(), bankId);
    }

    private RowMapper<Bank> bankRowMapper() {
        return (rs, rowNum) -> {
            return new Bank(
                    rs.getString("bank_id"),
                    rs.getInt("money")
            );
        };
    }

    public void update(String bankId, int money) {
        String sql = "update bank set money=? where bank_id=?";
        jdbcTemplate.update(sql,money,bankId);
    }

    public void delete(String bankId) {
        String sql = "delete from bank where bank_id=?";
        jdbcTemplate.update(sql,bankId);
    }

}
