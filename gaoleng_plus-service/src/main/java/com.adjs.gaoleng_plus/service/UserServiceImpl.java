package com.adjs.gaoleng_plus.service;

import common.Response;
import com.adjs.gaoleng_plus.api.UserDao;
import com.adjs.gaoleng_plus.entity.UserDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseService {

    private static final Logger logger  = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Autowired
    TokenServiceImpl tokenService;

    @Autowired
    JedisPool jedisPool;

    @ResponseBody
    public Response login(HttpServletRequest request, HttpServletResponse response, UserDo userDo) {
        UserDo userBase = userDao.queryUser(userDo);
        Map<String, Object> data = new HashMap<>();
        if (userBase != null) {
            // 返回token
            String token = tokenService.getToken(userBase);
            Jedis jedis = jedisPool.getResource();
            try {
                String jsessionid = request.getSession().getId();

                // 创建一个 cookie对象
                jedis.setex(jsessionid, 60 * 60 * 2, token);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("step5 err", e);
            } finally {
                jedis.close();
            }


            data.put("token", token);
            userBase.setPassword("");
            data.put("user", userBase);
            Response success = retSuccessResponse();
            success.setData(data);
            return success;
        } else {
            return retFailedResponse();
        }
    }

    public Response logout(HttpServletRequest request, HttpServletResponse response, UserDo user) {
        try {
            String jsessionid = request.getSession().getId();
            // 创建一个 cookie对象
            jedisPool.getResource().del(jsessionid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response success = retSuccessResponse();
        success.setRespDesc("退出成功");
        return success;
    }

    public UserDo findUserById(String userId) {
        UserDo userBase = userDao.queryUserById(userId);
        return userBase;
    }

}
