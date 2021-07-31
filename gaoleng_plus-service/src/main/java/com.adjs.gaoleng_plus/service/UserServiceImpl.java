package com.adjs.gaoleng_plus.service;

import common.Response;
import com.adjs.gaoleng_plus.api.UserDao;
import com.adjs.gaoleng_plus.entity.UserDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseService {
    @Autowired
    UserDao userDao;

    @Autowired
    TokenServiceImpl tokenService;

    @Autowired
    JedisPool jedisPool;

    @PostMapping("/login")
    @ResponseBody
    public Response login(UserDo userDo) {
        UserDo userBase = userDao.queryUser(userDo);
        Map<String, Object> data = new HashMap<>();

        if (userBase != null) {
            // 返回token
            String id = userBase.getId();
            String token = tokenService.getToken(userBase);

            try {
                jedisPool.getResource().setex(id, 60 * 60 * 2, token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            data.put("token", token);
            data.put("user", userBase);
            Response success = retSuccessResponse();
            success.setData(data);
            return success;
        } else {
            return retFailedResponse();
        }
    }

    public UserDo findUserById(String userId) {
        UserDo userBase = userDao.queryUserById(userId);
        return userBase;
    }
}
