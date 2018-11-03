package cn.wenhaha.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

/**
 * @Author: Wyndem
 * @Description:
 * @Date: Created in  2018-11-02 17:26
 * @Modified By:
 */
public class LogoutSuccessHandlerWen implements LogoutSuccessHandler {

    ObjectMapper mapper=new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Hashtable<String, Object> stringObjectHashtable = new Hashtable<>();
            stringObjectHashtable.put("code",200);
            stringObjectHashtable.put("msg","logout success!");
            out.print(mapper.writeValueAsString(stringObjectHashtable));
            out.flush();
        } finally {
            out.close();
        }

    }
}
