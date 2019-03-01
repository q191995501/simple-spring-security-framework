package cn.wenhaha.security.handler;

import cn.wenhaha.security.tool.ResponseUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;


/**
 * 401 处理 表示没有登陆访问需要登陆的资源
 */

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Autowired(required=true)
    private AuthenticationFailHandler authenticationFailHandler;




     /**
      * @title: 显示
      * @description: 当没有授权的用户想访问有权限的资源，则调用该方法
      * @author:  Wyndem
      * @date:  2018/11/2  16:49
      **/
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ResponseUtils.sendJsonMsg(response, HttpStatus.UNAUTHORIZED.value(),authenticationFailHandler.anonymous(authException));

    }
}