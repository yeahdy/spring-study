package springdb1.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import springdb1.jdbc.domain.Bank;
import springdb1.jdbc.repository.exeception.DbException;

/**
 * 예외 누수 문제 해결
 * 체크 예외를 런타임 예외로 변경 (throws SQLException 제거)
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class BankRepository3Imple implements BankRepository3 {

    private final DataSource dataSource;

    public Bank save(Bank bank) {
        String sql = "insert into bank(bank_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bank.getBankId());
            pstmt.setInt(2, bank.getMoney());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            throw new DbException(se);
        } finally {
            close(con, pstmt, null);
        }
        return bank;
    }

    public Bank findById(String bankId) {
        String sql = "select * from bank where bank_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bankId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Bank(
                        rs.getString("bank_id"),
                        rs.getInt("money")
                );
            } else {
                throw new NoSuchElementException("bank not found bankId=" + bankId);
            }
        } catch (SQLException se) {
            throw new DbException(se);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void update(String bankId, int money) {
        String sql = "update bank set money=? where bank_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, bankId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete(String bankId) {
        String sql = "delete from bank where bank_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bankId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            close(con, pstmt, null);
        }
    }


    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);  //트랜잭션 동기화: 커넥션 이후 로직을 위해 트랜잭션 종료할때까지 유지
    }

    private Connection getConnection() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);  //트랜잭션 동기화: 트랜잭션 반환, 없으면 생성 후 반환
        log.info("connection= {}, class= {}", connection, connection.getClass());
        return connection;
    }

}
