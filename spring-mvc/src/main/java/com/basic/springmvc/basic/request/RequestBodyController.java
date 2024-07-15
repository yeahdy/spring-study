package com.basic.springmvc.basic.request;

import com.basic.springmvc.basic.dto.UserSelectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RequestBodyController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-string")
    public String requestBodyString(@RequestBody String messageBody){
        log.info("messageBody= {}",messageBody);
        return "HTTP OK 200";
    }

    @PostMapping("/request-body-json")
    public String requestBodyJson(@RequestBody String messageBody) throws JsonProcessingException {
        log.info("messageBody= {}",messageBody);
        UserSelectRequest userSelectRequest = objectMapper.readValue(messageBody, UserSelectRequest.class);
        log.info("{}",userSelectRequest);
        return "HTTP OK 200";
    }

    @PostMapping("/request-body-json-object1")
    public String requestBodyObject1Json(@RequestBody UserSelectRequest request) {
        log.info("{}",request);
        return "HTTP OK 200";
    }

    @PostMapping("/request-body-json-object2")
    public UserSelectRequest requestBodyObject2Json(@RequestBody UserSelectRequest request) {
        log.info("{}",request);
        return request;
    }

}
