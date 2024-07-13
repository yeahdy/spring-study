package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;
    private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String getLog(HttpServletRequest request){
        String requestURL = request.getRequestURI();
        System.out.println("Proxy myLogger = "+myLogger.getClass());    //가짜 프록시 객체가 대신 주입됨
//        MyLogger myLogger = myLoggerProvider.getObject();

        //클라이언트는 가짜 프록시 객체의 메소드 호출. 실제 동작은 프록시 객체가 실제 객체를 찾아서 실제 메소드를 호출
        myLogger.setRequestURL(requestURL);
        myLogger.log("controller log");

        logService.logic("ellie");
        return "HTTP 200";
    }
}
