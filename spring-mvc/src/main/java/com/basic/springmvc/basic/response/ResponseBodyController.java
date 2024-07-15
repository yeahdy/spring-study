package com.basic.springmvc.basic.response;

import com.basic.springmvc.basic.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ResponseBodyController {

    @GetMapping("/response-body-string")
    public ResponseEntity<String> responseBody(){
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/response-body-json")
    public ResponseEntity<UserDto> responseJsonBody(){
        UserDto userDto = new UserDto("ellie", 20, "korea", "01012345678");
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }


    /**
     * Http 요청/응답 시 MappingJackson2HttpMessageConverter 가 변환
     * 클래스 타입: 객체 또는 HashMap
     * 미디어타입 application/json 관련 (text/html 일 경우 400에러)
    * */
    @RequestMapping("/response-body")
    public ResponseEntity<UserDto> responseBody(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

}
