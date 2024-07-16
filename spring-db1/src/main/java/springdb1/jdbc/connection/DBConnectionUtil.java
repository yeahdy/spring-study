package springdb1.jdbc.connection;

import static springdb1.jdbc.connection.ConnectionConst.PASSWORD;
import static springdb1.jdbc.connection.ConnectionConst.URL;
import static springdb1.jdbc.connection.ConnectionConst.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection(){
        try{
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class={}",connection, connection.getClass());
            return connection;
        }catch (SQLException se){
            throw new IllegalStateException(se);
        }
    }

}
