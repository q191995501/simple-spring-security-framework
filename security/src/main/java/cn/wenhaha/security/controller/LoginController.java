package cn.wenhaha.security.controller;
import cn.wenhaha.security.service.SpringDataUserDetailsService;
import cn.wenhaha.security.tool.JwtTokenUtil;
import cn.wenhaha.security.tool.ParameterUtils;
import cn.wenhaha.security.tool.RequestBodyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * @Author: Wyndem
 * @Description: 登陆控制器
 * @Date: Created in  2018-10-31 15:00
 * @Modified By:
 */
@RestController
public class LoginController{


    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SpringDataUserDetailsService springDataUserDetailsService;


    @Value("${cn.wenhaha.longinHandle}")
    private String loginClassName;


    @Value("${cn.wenhaha.userNameField}")
    private String userName;

    @Value("${cn.wenhaha.loginMethodName}")
    private String loginMethodName;

    private ParameterUtils parameterUtils;
    private ObjectMapper mapper ;


    public LoginController(){
        mapper = new ObjectMapper();
    }





    @RequestMapping(value = "${cn.wenhaha.loginUrl}",method = {RequestMethod.GET,RequestMethod.POST})
    public  Object login(HttpServletRequest request){


        if (parameterUtils==null){
            try {
                parameterUtils = new ParameterUtils(loginClassName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        Map<String,Object> resertMap=null;
        try {

            //获取参数的key
            List<String> parameters = parameterUtils.analysisParameters(loginMethodName);


            List<String> userParameters=new ArrayList<>(7);

            //通过key，获取值
            parameters.forEach(parameter->{
                Object attribute = request.getParameter(parameter);
                if (Objects.nonNull(attribute)){
                    userParameters.add((String)attribute);
                }

            });


            //获取不到，尝试从body中获取
            if (userParameters.size()==0) {
                Map  parameter_map= ReadAsChars(request);
                parameters.forEach(p->{
                    userParameters.add(String.valueOf(parameter_map.get(p)));
                });
            }

            String[]  parames=new String[userParameters.size()];

            for (int i=0;i<userParameters.size();i++){
                parames[i]=userParameters.get(i);
            }

            Object invoke = parameterUtils.invoke(loginMethodName, parames);

            String json = mapper.writeValueAsString(invoke);
            resertMap = mapper.readValue(json, Map.class);


        } catch (Exception e) {
            logger.error("登录处理出错：",e);
        }


        if (resertMap==null){

            resertMap=new HashMap<>();
            resertMap.put("code",-999);
//            resertMap.put("msg","调用方式异常，请查看日志");
            resertMap.put("msg","登陆失败");

        }


        Object o = resertMap.get(userName);

        //登陆成功
        if (o!=null){

            UserDetails userDetails = springDataUserDetailsService.loadUserByUsername(String.valueOf(o));
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken( userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(result);
            resertMap.put("token",jwtTokenUtil.doGenerateToken((String) resertMap.get(userName)));
        }


        return resertMap;
    }



    public  Map ReadAsChars(HttpServletRequest request) {

        try {
            BufferedReader bufferedReader = request.getReader();
            String read = RequestBodyUtils.read(bufferedReader);
            Map map = mapper.readValue(read, Map.class);
            return map;
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

    }




}
