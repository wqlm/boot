package com.wqlm.boot.user.dao;

import com.wqlm.boot.user.po.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User> {
}