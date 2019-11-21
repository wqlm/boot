package com.wqlm.boot.user.controller;

import com.wqlm.boot.user.dto.RegisterDTO;
import com.wqlm.boot.user.service.UserService;
import com.wqlm.boot.user.vo.result.FailResult;
import com.wqlm.boot.user.vo.result.Result;
import com.wqlm.boot.user.vo.result.SuccessResult;
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
    public Result register(@Valid RegisterDTO dto) {
        boolean result = userService.register(dto);
        if (result) {
            return new SuccessResult<>();
        }
        return new FailResult();
    }
}