package cn.wenhaha.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationFailHandler {

    /**
     * 无登陆状态
     * @return
     */
    public  Object anonymous(AuthenticationException authException);


    /**
     * 无权限状态
     * @param e
     * @return
     */
    public Object forbidden(AccessDeniedException e);

}
