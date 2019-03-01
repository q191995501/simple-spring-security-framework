package cn.wenhaha.security.repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: Wyndem
 * @Description:
 * @Date: Created in  2018-11-01 20:24
 * @Modified By:
 */
public interface SecurityUserConfig {


    /**
     * 通过用户名返回该用户的角色
     * @param userName 用户名
     * @return 角色
     */
    public String getUserRole(String userName);

    /**
     * 获取角色和可访问url的映射
     * @return  角色为key和该角色所可访问的url资源
     */
    public Map<String,List<String>> getRoleUserMappingData();

}
