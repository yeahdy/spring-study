package springdb1.jdbc.connection;

import static springdb1.jdbc.connection.ConnectionConst.*;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
public class ConnectionTest {

    //NOTE:각자의 역할에만 집중할 수 있도록 설정과 사용을 분리.
    // 한곳에서 설정을 해두면 사용에서는 설정을 알필요 없이 사용만 하면 된다.
    @Test
    void dataSourceDriverManager() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);  //DataSource 설정 후 객체 생성
        getDataSource(dataSource);  //사용
    }

    private void getDataSource(DataSource dataSource) throws SQLException {
        Connection connection0 = dataSource.getConnection();
        Connection connection1 = dataSource.getConnection();
        log.info("▶ DataSource :: connection0= {}", connection0, connection0.getClass());
        log.info("▶ DataSource :: connection1= {}", connection1, connection1.getClass());
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setMaximumPoolSize(5);   //NOTE: 커넥션풀의 커넥션 갯수보다 많이 필요할 경우 계속해서 대기한다.
        hikariDataSource.setPoolName("HIKARI");   //default: 공백
        hikariDataSource.setConnectionTimeout(3000);

        useDataSource(hikariDataSource);
        Thread.sleep(1000);
    }

    //NOTE: connection pool은 커넥션을 반납한 것 부터 재사용하는 Queue 구조
    private void useDataSource(DataSource dataSource) throws SQLException, InterruptedException {
        Connection connection0 = dataSource.getConnection();
        Connection connection1 = dataSource.getConnection();
        try (Connection connection2 = dataSource.getConnection();) {    //conn2 커넥션 사용
            log.info("♣ DataSource :: connection2= {}", connection2);    //class: HikariProxyConnection
        }   //conn2 커넥션 반납
        try (Connection connection3 = dataSource.getConnection();) {    //반납한 conn2 커넥션 재사용
            log.info("♣ DataSource :: connection3= {}", connection3);
        }   //conn2 커넥션 반납
        try (Connection connection4 = dataSource.getConnection();) {    //반납한 conn2 커넥션 재사용
            log.info("♣ DataSource :: connection4= {}", connection4);
        }   //conn2 커넥션 반납
        Connection connection5 = dataSource.getConnection();
        Connection connection6 = dataSource.getConnection();
        Connection connection7 = dataSource.getConnection();

        log.info("▶ DataSource :: connection5= {}", connection5);
        log.info("▶ DataSource :: connection6= {}", connection6);
        log.info("▶ DataSource :: connection7= {}", connection7);
    }

}
