package cn.wenhaha.security.handler;

import cn.wenhaha.security.tool.ResponseUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 403状态处理
 */

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private AuthenticationFailHandler authenticationFailHandler;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        ResponseUtils.sendJsonMsg(httpServletResponse, HttpStatus.FORBIDDEN.value(),authenticationFailHandler.forbidden(e));

    }
}
