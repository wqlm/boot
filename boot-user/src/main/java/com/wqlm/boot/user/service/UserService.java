package com.wqlm.boot.user.service;

import com.wqlm.boot.user.dao.UserMapper;
import com.wqlm.boot.user.dto.RegisterDTO;
import com.wqlm.boot.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean register(RegisterDTO dto) {
        // 生成密码的随机盐
        String salt = UUID.randomUUID().toString();
        User user = new User();
        user.setUserName(dto.getUserName());
        // 密码加盐后在md5
        user.setPassword(DigestUtils.md5DigestAsHex((dto.getPassword() + salt).getBytes()));
        user.setSalt(salt);
        return 1 == userMapper.insert(user);

    }
}
