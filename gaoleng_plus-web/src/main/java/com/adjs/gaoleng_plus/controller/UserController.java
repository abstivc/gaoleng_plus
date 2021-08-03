package com.adjs.gaoleng_plus.controller;

import com.adjs.gaoleng_plus.annoation.UserLoginToken;
import com.adjs.gaoleng_plus.entity.UserDo;
import com.adjs.gaoleng_plus.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    //登录
    @RequestMapping("/login")
    @ResponseBody
    public common.Response login(HttpServletRequest request, HttpServletResponse response, UserDo user) {
        return userService.login(request, response, user);
    }

    //登录
    @UserLoginToken
    @RequestMapping("/logout")
    @ResponseBody
    public common.Response logout(HttpServletRequest request, HttpServletResponse response, UserDo user) {
        return userService.logout(request, response, user);
    }
}
