

### 介绍
这是我自用的一个框架，暂且给它命名为:wyndem-security框架，小小的自恋一下，那么这个框架适用于**前后端分离**的项目。对于需要跳转页面的需求暂时没有。




### 特点

 - 动态权限设置
 - 5分钟即可搭建出一个完整的权限控制功能
 - 快速、简单、对数据库表无限制
 - 自定义登录逻辑
 - 对于新手非常友好
 
 

### 依赖

 - Spring Boot
 - Spring security
 - jjwt

### Maven

不包含Spring Boot，基本上创建Spring Boot项目的时候就自己生成依赖。
不会的话可用参考这篇:[Spring Boot 2.x 入门](https://blog.csdn.net/qq_31403321/article/details/82633420)
```
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.10.5</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.10.5</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.10.5</version>
            <scope>runtime</scope>
	 </dependency>
```



### 快速开始(四步完成)

#### 第一步:实现`SecurityUserConfig`接口

`SecurityUserConfig`的接口中需要实现2个方法:

 - getUserRole(String userName)			--根据用户来返回该用户是什么角色
 -  getRoleUserMappingData()			--返回角色对应的可访问的url

**Example:**
MyConfig .java
```
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
    	//指hi下的任何路径都可访问
        urls.add("/hi/**");

 		//这里是设置user角色可访问的url
        map.put("user",urls);

        return map;
    }
}

```
---

#### 第二步:创建登录的Controller：
关于用户登录，还是自己实现比较放心。

**Example:**
DemoController .java

```
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
    public Object example(){
        return "你好，wyndem安全框架";
    }
```
User.java
```
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
```


这里的`login`方法不要写Spring Mvc 上的Mapping映射，在后面会说到如何访问。

**关于login方法的限制**

 - **参数中必须为`String`类型**
 - **需要把参数的名字写到Parameter注解上，多个这样写：@Parameter(parameters = {"参数1","参数2"})，单个直接写:@Parameter("参数1")**
 - **返回的类型可以是Map，或者是对象，但是该对象一定要有个用户名的字段**
 - **登录成功，将用户名填充，登录失败，将用户名为Null**

---

#### 第三步:自定义权限访问失败接口：
如果用户尝试去访问一个需要权限不够或者尝试用错误的token进行认证，这时我们需要实现`AuthenticationFailHandler`接口，具体说明情况下面例子，已经做了很好的解释


**Example:**
MyAuthenticationFailHandler .java
```

@Component
public class MyAuthenticationFailHandler implements AuthenticationFailHandler {


    /**
     * 当匪名用户企图用错误的token进行验证或尝试访问一个权限不够的资源
     * @param authException  异常
     * @return
     */
    @Override
    public Object anonymous(AuthenticationException authException) {
        Map<String,Object> map=new HashMap<>();
        map.put("error",authException.getMessage());
        map.put("msg","认证失败");
        return map;
    }


    /**
     * 当已认证用户尝试访问一个权限不够的资源
     * @param e 异常
     * @return
     */
    @Override
    public Object forbidden(AccessDeniedException e) {
        Map<String,Object> map=new HashMap<>();
        map.put("error",e.getMessage());
        map.put("msg","权限不足");
        return map;
    }
}

```



#### 第四步:填写配置：

```
#验证头
cn.wenhaha.jwt.header=Authorization
#jwt的过期时间(单位：秒)
cn.wenhaha.jwt.expiration=100
#登录的url
cn.wenhaha.loginUrl=/login
#处理登录的Controller
cn.wenhaha.longinHandle=com.example.demo.DemoController
#用户名字段
cn.wenhaha.userNameField=name
#处理登录的方法
cn.wenhaha.loginMethodName=login
#是否使用继续记忆处理(记忆处理解释在下方有说明)
cn.wenhaha.jwt.status=false
```

有些同学可能会有疑问:

 1. 验证头是什么？
	
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181103142939260.png)
加入请求头部的key

2. 用户名字段是什么意思？
	
	就是保存用户名的字段名啦。上面配置中用户名字段为:name，那是因为我User对象中name字段是保存用户名的。如果你在登录方法中返回Map，那么保存用户名的key就是用户名字段
3. 什么是记忆处理？
	
	记忆处理：当用户认证成功后，第一次访问需要加入认证后的`token`，后面再次访问任何资源都无需加`token`直到失效或用户更换了客户端为止。

	无记忆处理：当用户认证成功后，不管是任何资源都需要带入`token`，否则为视为匪名用户

**启动类：**

```
@SpringBootApplication
@ComponentScan(basePackages={"com.example.demo","cn.wenhaha.security"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

```

**注意:**

> ComponentScan:需要将cn.wenhaha.security包让Spring Boot 扫描到


### 效果：

在之前配置中，只有user角色可以访问/hi的url。那么我直接访问看看效果：

**没有认证**：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181103144020797.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMxNDAzMzIx,size_16,color_FFFFFF,t_70)


**登录**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181103144731675.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMxNDAzMzIx,size_16,color_FFFFFF,t_70)

然后把登录拿到的Token，在进行一次请求
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181103144901441.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMxNDAzMzIx,size_16,color_FFFFFF,t_70)


就到这里了，有问题可以发送邮件wyndem@qq.com或者用Chat问答都可以联系到我。