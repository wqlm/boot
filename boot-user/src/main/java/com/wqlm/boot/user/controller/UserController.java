package com.wqlm.boot.user.controller;

import com.wqlm.boot.user.dto.LoginDTO;
import com.wqlm.boot.user.dto.ModifyPasswordDTO;
import com.wqlm.boot.user.dto.RegisterDTO;
import com.wqlm.boot.user.po.User;
import com.wqlm.boot.user.service.UserService;
import com.wqlm.boot.user.util.redis.RedisOperator;
import com.wqlm.boot.user.vo.LoginVO;
import com.wqlm.boot.user.vo.UserVO;
import com.wqlm.boot.user.vo.result.FailResult;
import com.wqlm.boot.user.vo.result.Result;
import com.wqlm.boot.user.vo.result.SuccessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 注册
     * 参数通过表单传递
     *
     * @param dto
     * @return
     */
    @PostMapping("/register")
    public Result register(@Valid RegisterDTO dto) {
        boolean result = userService.register(dto);
        if (result) {
            return new SuccessResult<>();
        }
        return new FailResult();
    }


    /**
     * 登陆接口
     *
     * @return
     */
    @PostMapping("/login")
    public Result login(LoginDTO dto) {
        LoginVO vo = userService.login(dto);
        if (vo == null) {
            return new FailResult();
        }
        return new SuccessResult<>(vo);
    }


    /**
     * 修改密码
     * 参数通过 json 传递
     *
     * @param dto
     * @return
     */
    @PutMapping("/password")
    public Result modifyPassword(@Valid @RequestBody ModifyPasswordDTO dto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        User user = (User) redisOperator.get(token);
        boolean result = userService.modifyPassword(dto, user);
        if (result) {
            return new SuccessResult<>();
        }
        return new FailResult();
    }

    /**
     * 获取用户信息
     * 参数通过url传递
     *
     * @param id
     * @return
     */
    @GetMapping
    public Result getUser(@NotNull(message = "id不能为空") Integer id) {
        UserVO vo = userService.getUser(id);
        return new SuccessResult<>(vo);
    }

}