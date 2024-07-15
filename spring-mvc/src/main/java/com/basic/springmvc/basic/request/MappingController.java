package com.basic.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MappingController {

    @GetMapping("/basic")
    public String basic(){
        log.info("hello spring mvc!");
        return "OK";
    }

    //localhost:8080/mapping-param?mode=TYPE&data=A
    @GetMapping(value = "/mapping-param", params = {"mode=TYPE","data=A"})
    public String mappingParam(){
        log.info("params=\"!mode\" 특정파라미터 key가 아닐 경우 호출 가능");
        log.info("params=\"mode!=TYPE\" 특정파라미터 key의 value가 아닐 경우 호출 가능");
        return "OK";
    }

    @GetMapping(value = "/mapping-param", headers = "mode")
    public String mappingHeader(){
        log.info("headers=\"!mode\" 특정파라미터 key가 아닐 경우 호출 가능");
        log.info("headers=\"mode!=TYPE\" 특정파라미터 key의 value가 아닐 경우 호출 가능");
        return "OK";
    }

    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "content-type json";
    }

    @PostMapping(value = "/mapping-produce", produces =  MediaType.TEXT_PLAIN_VALUE)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "Accept text/html";
    }

}
