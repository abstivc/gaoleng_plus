package com.adjs.gaoleng_plus.service;

import com.adjs.gaoleng_plus.Interceptor.AuthenticationInterceptor;
import org.springframework.stereotype.Component;

@Component
public class LoginInfo {

    public String getUserId() {
        return AuthenticationInterceptor.threadLocalUserId.get();
    }
}
