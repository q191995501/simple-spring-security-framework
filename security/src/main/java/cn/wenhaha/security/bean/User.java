package cn.wenhaha.security.bean;

import cn.wenhaha.security.Identity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Wyndem
 * @Description:
 * @Date: Created in  2018-11-01 19:54
 * @Modified By:
 */
public class User  implements UserDetails {




    // ~ Instance fields
    // ================================================================================================
    private Map<String,List<String>> roleMapping;
    private final String password;
    private final String username;
    private final String role;
    //账号是否过期
    private final boolean accountNonExpired;
    //是否冻结
    private final boolean accountNonLocked;
    //密码是否过期
    private final boolean credentialsNonExpired;
    //是否可用
    private final boolean enabled;


    // ~ Constructors
    // ===================================================================================================


    public User(String username, String role) {
        this.username = username;
        this.role = role;
        this.password = "";
        this.accountNonExpired=true;
        this.accountNonLocked=true;
        this.credentialsNonExpired=true;
        this.enabled=true;
    }

    public User(String password, String username, String role) {
        this.password = password;
        this.username = username;
        this.role=role;
        this.accountNonExpired=true;
        this.accountNonLocked=true;
        this.credentialsNonExpired=true;
        this.enabled=true;
    }

    public User(String password,
                String username,
                String role,
                boolean accountNonExpired,
                boolean accountNonLocked,
                boolean credentialsNonExpired,
                boolean enabled) {

        this.password = password;
        this.username = username;
        this.role=role;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }


    public Map<String, List<String>> getRoleMapping() {
        return roleMapping;
    }

    public void setRoleMapping(Map<String, List<String>> roleMapping) {
        this.roleMapping = roleMapping;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        try {
            return getRoleMapping()
                     .get(getRole())
                     .stream()
                     .map(authority ->new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
        } catch (Exception e) {
            //找不到该角色对应的权限
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(Identity.ROLE_ANONYMOUS.toString());
            Collection<GrantedAuthority> collection=new ArrayList(1);
            collection.add(grantedAuthority);
            return collection;
        }


    }

    public String getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
