package com.adjs.gaoleng_plus.service;

import com.adjs.gaoleng_plus.entity.UserDo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl {
    public String getToken(UserDo userDo) {
        String token="";
        token= JWT.create().withAudience(userDo.getId())
                .sign(Algorithm.HMAC256(userDo.getPassword() + new Date().getTime()));
        return token;
    }
}
