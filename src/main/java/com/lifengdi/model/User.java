package com.lifengdi.model;

import lombok.Data;

/**
 * 用户信息
 * @author 李锋镝
 * @date Create at 20:33 2018/12/10
 */
@Data
public class User {

    private String userId;
    /**
     * 用户账号
     */
    private String userLoginName;
    private String phone;
    /**
     * 用户名
     */
    private String userName;

}
