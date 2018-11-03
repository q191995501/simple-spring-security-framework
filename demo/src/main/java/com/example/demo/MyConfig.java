package com.example.demo;

import cn.wenhaha.security.Identity;
import cn.wenhaha.security.repository.SecurityUserConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wyndem
 * @Description:
 * @Date: Created in  2018-11-02 14:29
 * @Modified By:
 */
@Component
public class MyConfig implements SecurityUserConfig {
    @Override
    public String getUserRole(String userName) {
        return "user";
    }

    @Override
    public Map<String, List<String>> getRoleUserMappingData() {
        Map<String,List<String>> map=new HashMap<>();

        List<String> urls=new ArrayList<>();
        urls.add("/hi/**");

//        List<String> anonymous=new ArrayList<>();
//        anonymous.add("/test");
//        map.put(Identity.ROLE_ANONYMOUS.name(),anonymous);
        map.put("user",urls);

        return map;
    }
}
