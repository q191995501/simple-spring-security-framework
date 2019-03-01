package cn.wenhaha.security.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {


    /**
     * 发送的实体内容
     * @param start
     * @param object
     */
    public  static  void sendJsonMsg(HttpServletResponse response, int start, Object object) throws IOException {
        ObjectMapper mapper=new ObjectMapper();

        response.setStatus(start);

        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(mapper.writeValueAsString(object));
            out.flush();

        } finally {
            out.close();
        }
    }




}
