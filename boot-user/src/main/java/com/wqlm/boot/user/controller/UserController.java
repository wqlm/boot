package com.wqlm.boot.user.controller;

import com.wqlm.boot.user.dto.RegisterDTO;
import com.wqlm.boot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@Valid RegisterDTO dto) {
       boolean result = userService.register(dto);
       if(result){
           return "注册成功";
       }
       return "注册失败";
    }
}
