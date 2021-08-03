package com.adjs.gaoleng_plus.Interceptor;

import com.adjs.gaoleng_plus.annoation.PassToken;
import com.adjs.gaoleng_plus.annoation.UserLoginToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    public static ThreadLocal<String> threadLocalUserId = new ThreadLocal<>();
    @Autowired
    JedisPool jedisPool;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                String jessionId = httpServletRequest.getSession().getId();

                // 执行认证
                if (jessionId == null) {
                    falseResult(httpServletResponse, "无token，请重新登录");
                    return false;
                }
                // 获取 token 中的 user id
                String redisToken = jedisPool.getResource().get(jessionId);
                if (StringUtils.isEmpty(redisToken)) {
                    falseResult(httpServletResponse, "登陆失效,请重新登陆");
                    return false;
                }

                String userId;
                try {
                    userId = JWT.decode(redisToken).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }

                threadLocalUserId.set(userId);
                /*UserDo user = userService.findUserById(userId);
                if (user == null) {
                    falseResult(httpServletResponse, "用户不存在，请重新登录");
                    return false;
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    falseResult(httpServletResponse, "验证异常");
                    return false;
                }*/
            }
        }
        return true;
    }


    public void falseResult(HttpServletResponse response, String failMsg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        common.Response resultBody = new Response("999999", failMsg);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(resultBody));
        return;
    }
}
