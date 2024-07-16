package springdb1.jdbc.connection;

//NOTE: abstract키워드로 객체 생성을 막고, 단순히 상수 값을 제공하는 용도로만 사용되도록 명시
public abstract class ConnectionConst {
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
