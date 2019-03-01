package cn.wenhaha.security.filter;

import cn.wenhaha.security.service.SpringDataUserDetailsService;
import cn.wenhaha.security.tool.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @Author: Wyndem
 * @Description: JWT验证过滤器
 * @Date: Created in  2018-11-01 11:08
 * @Modified By:
 */

@Component("jwtValidateSecurityFilter")
public class JwtValidateSecurityFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SpringDataUserDetailsService springDataUserDetailsService;



    @Value("${cn.wenhaha.jwt.header}")
    private String header;

    @Value("${cn.wenhaha.jwt.status}")
    private Boolean status=true;



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        final  String header = httpServletRequest.getHeader(this.header);

        String authToken=null;

        if (header!=null && header.startsWith("Bearer ")){
            authToken=header.substring(7);
        }

        String userName = null;
        try {
            userName = jwtTokenUtil.getSubject(authToken);

            if ((userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) || !status  ){
                UserDetails userDetails = springDataUserDetailsService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken( userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(result);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            if (!status){

                SecurityContextHolder.clearContext();
            }
        }



        logger.info(authToken);

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }










}
