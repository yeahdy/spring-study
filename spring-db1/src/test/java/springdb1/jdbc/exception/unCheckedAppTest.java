package springdb1.jdbc.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;

/**
 * 기본적으로 언체크 예외를 사용하자 체크예외는 의도적인 비즈니스 로직에만 사용한다.
 */
public class UnCheckedAppTest {

    @Test
    void unchecked(){
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(RuntimeSQLException.class);
    }


    static class Controller {
        Service service = new Service();
        public void request(){
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic(){
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException se) {
                throw new RuntimeSQLException(se);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }


    //RuntimeException 언체크 예외 생성
    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

}
