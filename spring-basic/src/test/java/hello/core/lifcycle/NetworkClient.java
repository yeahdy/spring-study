package hello.core.lifcycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NetworkClient {

    private String url;

    public NetworkClient(String url) {
        System.out.println("생성자 호출 url = "+url);
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect(){
        System.out.println("connect = "+url);
    }

    public void call(String message){
        System.out.println("call = "+url + ", message = "+message);
    }

    public void disconnect(){
        System.out.println("close = "+url);
    }

    //의존관계 주입이 끝나면 호출
    @PostConstruct
    public void init() {
        System.out.println("--------INIT--------");
        connect();
        call("초기화 연결 메세지");
    }

    // 빈이 소멸될때 호출
    @PreDestroy
    public void close() {
        System.out.println("--------CLOSE--------");
        disconnect();
    }
}
