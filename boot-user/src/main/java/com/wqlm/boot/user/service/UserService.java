package com.wqlm.boot.user.service;

import com.wqlm.boot.user.dao.UserMapper;
import com.wqlm.boot.user.dto.RegisterDTO;
import com.wqlm.boot.user.enums.ApplicationEnum;
import com.wqlm.boot.user.exception.ApplicationException;
import com.wqlm.boot.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param dto
     * @return
     */
    public boolean register(RegisterDTO dto) {

        if (getUserByName(dto.getUserName()) != null) {
            //用户名已存在
            throw new ApplicationException(ApplicationEnum.USER_NAME_REPETITION);
        }

        // 生成密码的随机盐
        String salt = UUID.randomUUID().toString();
        User user = new User();
        user.setUserName(dto.getUserName());
        // 密码加盐后在md5
        user.setPassword(DigestUtils.md5DigestAsHex((dto.getPassword() + salt).getBytes()));
        user.setSalt(salt);
        return 1 == userMapper.insert(user);

    }


    /**
     * 根据用户名查询用户
     *
     * @param name
     * @return
     */
    private User getUserByName(String name) {
        User user = new User();
        user.setUserName(name);
        return userMapper.selectOne(user);
    }
}
