package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.UUID;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
//value= 클라이언트가 HTTP Request 요청하는 시점에 스프링 컨테이너에 생성
public class MyLogger {
    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.printf("[%s] [%s] %s\n",uuid, requestURL, message);
    }

    @PostConstruct
    public void init(){
        uuid = UUID.randomUUID().toString();
        System.out.printf("[%s] request scope bean create: %s\n",uuid, this);
    }

    // 클라이언트 요청이 끝나는 시점에 자동호출
    @PreDestroy
    public void close(){
        System.out.printf("[%s] request scope bean close: %s\n",uuid, this);
    }
}
