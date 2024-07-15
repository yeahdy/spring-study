package com.basic.springmvc.basic.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private int age;
    private String address;
    private String phone;

    public UserDto(String username, int age, String address, String phone) {
        this.username = username;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }
}
