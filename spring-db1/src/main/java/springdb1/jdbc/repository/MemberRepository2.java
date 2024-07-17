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
import springdb1.jdbc.connection.DBConnectionUtil;
import springdb1.jdbc.domain.Member;

/**
 * JDBC- DataSource, JdbcUtils 사용
* */
@Slf4j
@RequiredArgsConstructor
public class MemberRepository2 {

    private final DataSource dataSource;

    public Member save(Member member) throws SQLException{
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            pstmt.executeUpdate();
        }catch (SQLException se){
            log.error("DB ERROR", se);
            throw se;
        }finally {
            close(con,pstmt,null);
        }
        return member;
    }

    public Member findById(String memberId) throws SQLException{
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return new Member(
                        rs.getString("member_id"),
                        rs.getInt("money")
                );
            }else{
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        }catch (SQLException se){
            log.error("DB ERROR", se);
            throw se;
        }finally {
            close(con,pstmt,null);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("DB ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
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
