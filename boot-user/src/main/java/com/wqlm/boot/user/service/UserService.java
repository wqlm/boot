package com.wqlm.boot.user.service;

import com.wqlm.boot.user.config.ApplicationProperty;
import com.wqlm.boot.user.dao.UserMapper;
import com.wqlm.boot.user.dto.LoginDTO;
import com.wqlm.boot.user.dto.ModifyPasswordDTO;
import com.wqlm.boot.user.dto.RegisterDTO;
import com.wqlm.boot.user.enums.ApplicationEnum;
import com.wqlm.boot.user.exception.ApplicationException;
import com.wqlm.boot.user.po.User;
import com.wqlm.boot.user.util.redis.RedisOperator;
import com.wqlm.boot.user.vo.LoginVO;
import com.wqlm.boot.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private ApplicationProperty applicationProperty;

    /**
     * 用户注册
     *
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
        user.setPassword(addSaltHash(dto.getPassword(), salt));
        user.setSalt(salt);
        return 1 == userMapper.insert(user);

    }

    /**
     * 加盐hash
     *
     * @param password
     * @param salt
     * @return
     */
    private String addSaltHash(String password, String salt) {
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }

    /**
     * 修改密码
     *
     * @param dto  旧密码与新密码
     * @param user 当前登陆的用户
     * @return
     */
    public boolean modifyPassword(ModifyPasswordDTO dto, User user) {
        User userDB = userMapper.selectByPrimaryKey(user.getId());
        if(userDB == null){
            throw new ApplicationException(ApplicationEnum.USER_NO_EXIST);
        }

        String oldLoginPassword = addSaltHash(dto.getOldPassword(), user.getSalt());
        if (!userDB.getPassword().equals(oldLoginPassword)) {
            // 密码不一致
            throw new ApplicationException(ApplicationEnum.PASSWORD_ERR);
        }

        User updateUser = new User();
        updateUser.setId(userDB.getId());
        updateUser.setPassword(addSaltHash(dto.getNewPassword(), userDB.getSalt()));
        return 1 == userMapper.updateByPrimaryKeySelective(updateUser);
    }

    /**
     * 更加用户ID获取用户
     *
     * @param id
     * @return
     */
    public UserVO getUser(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(vo, user);
        return vo;
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

    /**
     * 登陆
     *
     * @param dto
     */
    public LoginVO login(LoginDTO dto) {
        User user = getUserByName(dto.getUserName());
        if (user == null) {
            //用户不存在
            throw new ApplicationException(ApplicationEnum.USER_NO_EXIST);
        }

        String loginPassword = addSaltHash(dto.getPassword(), user.getSalt());
        if (!user.getPassword().equals(loginPassword)) {
            // 密码不一致
            throw new ApplicationException(ApplicationEnum.PASSWORD_ERR);
        }

        //密码一致登陆成功，将用户信息存储在redis中
        String token = UUID.randomUUID().toString();
        redisOperator.set(token, user, applicationProperty.getSessionTtl());
        LoginVO vo = new LoginVO();
        vo.setUserName(user.getUserName());
        vo.setToken(token);
        return vo;
    }


}
