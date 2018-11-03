package cn.wenhaha.security.controller;
import cn.wenhaha.security.tool.JwtTokenUtil;
import cn.wenhaha.security.tool.ParameterUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

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
            List<String> parameters = parameterUtils.analysisParameters(loginMethodName);


            List<String> userParameters=new ArrayList<>(7);

            parameters.forEach(parameter->{
                Object attribute = request.getParameter(parameter);
                if (Objects.nonNull(attribute)){
                    userParameters.add((String)attribute);
                }

            });
            String[]  parames=new String[userParameters.size()];

            for (int i=0;i<userParameters.size();i++){
                parames[i]=userParameters.get(i);
            }

            Object invoke = parameterUtils.invoke(loginMethodName, parames);

            String json = mapper.writeValueAsString(invoke);
            resertMap = mapper.readValue(json, Map.class);


        } catch (Exception e) {
            logger.error(e.getMessage());
        }


        if (resertMap==null){

            resertMap=new HashMap<>();
            resertMap.put("code",-2);
            resertMap.put("msg","调用方式异常");

        }


        Object o = resertMap.get(userName);

        if (o!=null)
            resertMap.put("token",jwtTokenUtil.doGenerateToken((String) resertMap.get(userName)));


        return resertMap;
    }




}
