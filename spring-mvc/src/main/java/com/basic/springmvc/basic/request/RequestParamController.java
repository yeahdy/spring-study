package com.basic.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
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

}
