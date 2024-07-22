package springdb1.jdbc.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Configuration
public class AppConfig {

    @Bean
    public SQLExceptionTranslator sqlExceptionTranslator(DataSource dataSource){
        return new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

}
