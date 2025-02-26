package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {
    private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;
    public void logic(String id) {
        System.out.println("SERVICE Proxy myLogger = "+myLogger.getClass());
//        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.log("service id = "+id);
    }
}
