package com.adjs.gaoleng_plus.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GLConfig {
    @Value("${gl.file.path}")
    public String FILE_PATH;
}
