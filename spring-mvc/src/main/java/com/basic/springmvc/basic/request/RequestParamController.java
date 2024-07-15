package com.basic.springmvc.basic.request;

import com.basic.springmvc.basic.dto.UserSelectRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public String requestParam(@RequestParam("name") String name, @RequestParam("age") int age){
        log.info("name {}, age {}", name, age);
        return "HTTP 200 OK";
    }

    @RequestMapping("/request-param-default")
    public String requestDefaultParam(@RequestParam(defaultValue = "none", value = "name") String name,
                               @RequestParam(defaultValue = "0", value = "age") int age){
        log.info("name {}, age {}", name, age);
        return "HTTP 200 OK";
    }

    @RequestMapping("/request-param-map")
    public String requestDefaultParam(@RequestParam Map<String,Object> paramMap){
        log.info("name {}, age {}", paramMap.get("name"), paramMap.get("age"));
        return "HTTP 200 OK";
    }

//    @RequestMapping("/model-attribute")
    public String modelAttributeV1(@ModelAttribute UserSelectRequest userRequest){  //NOTE: setter 메소드 호출을 통해 바인딩
        log.info("UserSelectRequest {}", userRequest);
        return "HTTP 200 OK";
    }

    @RequestMapping("/model-attribute")
    public String modelAttributeV2(UserSelectRequest userRequest){
        log.info("no annotation {}", userRequest);
        return "HTTP 200 OK";
    }

}
