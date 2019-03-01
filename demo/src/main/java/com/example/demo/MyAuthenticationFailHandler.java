package com.example.demo;


import cn.wenhaha.security.handler.AuthenticationFailHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MyAuthenticationFailHandler implements AuthenticationFailHandler {


    /**
     * 当匪名用户企图用错误的token进行验证或尝试访问一个权限不够的资源
     * @param authException  异常
     * @return
     */
    @Override
    public Object anonymous(AuthenticationException authException) {
        Map<String,Object> map=new HashMap<>();
        map.put("error",authException.getMessage());
        map.put("msg","认证失败");
        return map;
    }


    /**
     * 当已认证用户尝试访问一个权限不够的资源
     * @param e 异常
     * @return
     */
    @Override
    public Object forbidden(AccessDeniedException e) {
        Map<String,Object> map=new HashMap<>();
        map.put("error",e.getMessage());
        map.put("msg","权限不足");
        return map;
    }
}
