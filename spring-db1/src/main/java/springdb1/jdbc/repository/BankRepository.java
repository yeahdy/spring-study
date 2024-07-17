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
import org.springframework.jdbc.support.JdbcUtils;
import springdb1.jdbc.domain.Bank;

/**
 * JDBC- DataSource, JdbcUtils 사용
* */
@Slf4j
@RequiredArgsConstructor
public class BankRepository {

    private final DataSource dataSource;

    public Bank save(Bank bank) throws SQLException{
        String sql = "insert into bank(bank_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,bank.getBankId());
            pstmt.setInt(2,bank.getMoney());
            pstmt.executeUpdate();
        }catch (SQLException se){
            log.error("DB ERROR", se);
            throw se;
        }finally {
            close(con,pstmt,null);
        }
        return bank;
    }

    public Bank findById(String bankId) throws SQLException{
        String sql = "select * from bank where bank_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,bankId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return new Bank(
                        rs.getString("bank_id"),
                        rs.getInt("money")
                );
            }else{
                throw new NoSuchElementException("bank not found bankId=" + bankId);
            }
        }catch (SQLException se){
            log.error("DB ERROR", se);
            throw se;
        }finally {
            close(con,pstmt,null);
        }
    }

    /**
     * Connection 파라미터 전달
    * */
    public Bank findById(Connection con, String bankId) throws SQLException{
        String sql = "select * from bank where bank_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,bankId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return new Bank(
                        rs.getString("bank_id"),
                        rs.getInt("money")
                );
            }else{
                throw new NoSuchElementException("bank not found bankId=" + bankId);
            }
        }catch (SQLException se){
            log.error("DB ERROR", se);
            throw se;
        }finally {
            //Connection 종료X
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void update(Connection con, String bankId, int money) throws SQLException {
        String sql = "update bank set money=? where bank_id=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, bankId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete(String bankId) throws SQLException {
        String sql = "delete from bank where bank_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bankId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private Connection getConnection() throws SQLException{
        Connection connection = dataSource.getConnection();
        log.info("connection= {}, class= {}", connection, connection.getClass());
        return connection;
    }

}
