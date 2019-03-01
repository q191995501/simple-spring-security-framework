package cn.wenhaha.security.voter;

import cn.wenhaha.security.Identity;
import cn.wenhaha.security.repository.SecurityUserConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wyndem
 * @Description: 角色url权限控制过滤器
 * @Date: Created in  2018-09-18 10:01
 * @Modified By:
 */
public class RoleBasedVoter implements AccessDecisionVoter<Object> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private String loginUrl;

    private SecurityUserConfig securityUserConfig;

    public RoleBasedVoter(SecurityUserConfig securityUserConfig,String loginUrl) {
        this.securityUserConfig = securityUserConfig;
        this.loginUrl=loginUrl;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {

        if(authentication == null) {
            return ACCESS_DENIED;
        }


        int result=ACCESS_DENIED;
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        logger.info(authentication.getName());

        HttpServletRequest request = fi.getRequest();
        String method = request.getMethod();

        if (method.contains("OPTIONS")){
            return ACCESS_GRANTED;
        }

        for (GrantedAuthority attribute : authentication.getAuthorities()) {
            if(antPathMatcher.match(attribute.getAuthority(),url)){
                return ACCESS_GRANTED;
            }else if (accessPublic(url)&&accessCertification(url)){
                result=ACCESS_GRANTED;
            }
        }
        return result;
    }


    private boolean accessPublic(String url){
        //判断是个是否是登录的url
        if (loginUrl.equals(url))   return true;
        if (url.startsWith((loginUrl+"?")))    return true;

        List<String> publicUrls = securityUserConfig.getRoleUserMappingData().get(Identity.ROLE_ANONYMOUS.name());
        if (publicUrls!=null){
            for (String u :
                    publicUrls) {
                if (antPathMatcher.match(u,url)){
                    return true;
                }
            }
            return false;
        }


        return  true;
    }



    private boolean accessCertification(String url){

        boolean access=true;

        Map<String, List<String>> mappingData = securityUserConfig.getRoleUserMappingData();


        for (String key : mappingData.keySet()) {
           if (key.equals(Identity.ROLE_ANONYMOUS.name()))  continue;

            for (String u : mappingData.get(key)) {
                if (antPathMatcher.match(u, url)) {
                    return  false;
                }
            }
        }




        return access;
    }

}
