package com.lifengdi.controller;

import com.lifengdi.exception.ApiException;
import com.lifengdi.exception.BaseException;
import com.lifengdi.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author 李锋镝
 * @date Create at 20:00 2018/12/10
 */
@Slf4j
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    /**
     * 获取当前登录用户信息
     *
     * @return user
     * @throws ApiException
     */
    User getCurrentUser() throws ApiException {

        User user;
        if (!StringUtils.isEmpty(request.getHeader("X-userId"))) {
            user = getUser();
        } else {
            throw BaseException.NULL_USER_EXCEPTION.build();
        }
        return user;

    }

    /**
     * 获取用户信息(无token用户，去默认的系统用户)
     *
     * @return user
     */
    User getCurrentUserNullable() {

        User user = new User();
        if (!StringUtils.isEmpty(request.getHeader("X-userId"))) {
            user = getUser();
        } else {
            user.setUserId("-1");
            user.setUserLoginName("SYSTEM");
            user.setPhone("");
            user.setUserName("SYSTEM");
        }
        return user;
    }

    /**
     * 从header中获取用户信息
     *
     * @return user
     */
    private User getUser() {
        User user = new User();
        user.setUserId(request.getHeader("X-userId"));
        user.setUserLoginName(request.getHeader("X-username"));
        user.setPhone(request.getHeader("X-phone"));

        try {
            user.setUserName(URLDecoder.decode(request.getHeader("X-name"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("获取用户名出现异常", e);
        }
        return user;
    }

}
