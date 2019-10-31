package com.wqlm.boot.user.po;

import javax.persistence.*;

@Table(name = "user")
public class User {
    @Id
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码的盐
     */
    private String salt;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取密码的盐
     *
     * @return salt - 密码的盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置密码的盐
     *
     * @param salt 密码的盐
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }
}