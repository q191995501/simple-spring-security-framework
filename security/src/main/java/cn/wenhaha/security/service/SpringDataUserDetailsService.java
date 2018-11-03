package cn.wenhaha.security.service;

import cn.wenhaha.security.bean.User;
import cn.wenhaha.security.repository.SecurityUserConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Author: Wyndem
 * @Description:
 * @Date: Created in  2018-10-31 14:09
 * @Modified By:
 */


public class SpringDataUserDetailsService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecurityUserConfig securityUserConfig;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            String userRole = securityUserConfig.getUserRole(username);
            User user = new User(username, userRole);
            user.setRoleMapping(securityUserConfig.getRoleUserMappingData());
            return user;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw  new UsernameNotFoundException("找不到该用户");
        }
    }



}
