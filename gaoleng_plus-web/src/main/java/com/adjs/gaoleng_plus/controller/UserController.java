package com.adjs.gaoleng_plus.controller;

import com.adjs.gaoleng_plus.entity.UserDo;
import com.adjs.gaoleng_plus.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    //登录
    @PostMapping("/login")
    @ResponseBody
    public common.Response login(UserDo user) {
        return userService.login(user);
    }
}
