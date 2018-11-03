package com.example.demo;

import cn.wenhaha.security.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author: Wyndem
 * @Description:
 * @Date: Created in  2018-11-02 09:24
 * @Modified By:
 */

@RestController
public class DemoController {

    public  class User{
        private String name;
        private  String paw;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPaw() {
            return paw;
        }

        public void setPaw(String paw) {
            this.paw = paw;
        }
    }

    @Parameter(parameters = {"name","pwd"})
    public Object login(String name,String pwd){
        User user = new User();
        if ("wen".equals(name)&&name.equals(pwd)){
            user.setName("wen");
            return user;
        }else{

            return user;
        }

    }

    @GetMapping("/hi")
    public Object demo1(){
        return "你好，wyndem安全框架";
    }

}
