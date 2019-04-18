# Api文档

1. #### 适用场景

   适用基于spring-boot开发的java项目

2. #### 项目介绍

   该项目开发的目的：解决前后端完全分离的项目，在开发过程中，接口不断调整，接口文档经常更新，难以维护的问题。

   该项目基于spring-boot开发，会自动根据spring-boot的注解，自动解析并生成api接口文档，因为该项目是以jar的形式引入，所以会跟随服务一起启动，这样便可以保证当项目发布时，所有的接口文档都会重新生成，从而保证接口文档为当前版本的接口文档

3. #### 使用说明

   1. 后端使用

      1. 在项目的pom.xml文件中添加如下maven坐标（版本为当前最新）

         ```xml
          <!--api在线文档-->
          <dependency>
          <groupId>com.github.15706058532</groupId>
          <artifactId>api-generator</artifactId>
          <version>1.3.1</version>
          </dependency>
         ```

      2. 项目中需添加如下配置

         ```java
         @Configuration
         public class ApiGeneratorConfig extends WebMvcConfigurationSupport {
             @Override
             protected void addResourceHandlers(ResourceHandlerRegistry registry) {
                 registry.addResourceHandler("/api/**").addResourceLocations("classpath:/api/");
             }
         }
         ```

      3. 在`application.properties`配置文件中配置登录密码 [**建议修改**]

         配置如下：

         ```properties
         # 登录名 默认为：cleancode
         api-username=cleancode
         # 登录密码  默认为：cleancode
         api-password=cleancode123456
         ```

      4. 在启动类上配置`@EnableLzfApiGenerator`  [**必须**]

      5. 在Controller类上使用`@LzfApiDescribe`添加类描述   [**建议添加**]

      6. 在Controller类中的方法上使用`@LzfApiDescribe`添加方法描述   [**建议添加**]

      7. 根据需要在Controller类中的方法上使用`@LzfApiResponse`配置返回类   [**建议添加**]

      8. 在入参类，返回类，分页类中的属性中使用`@LzfApiDescribe`添加属性描述   [**建议添加**]

   2. 前端使用

      1. 进入api首页方式

         http://项目IP+端口号+项目名+/api

         示例：

      2. 登录页面（进入首页未登录时则会进入到登录页面）

         若后台未设置账户密码，则默认`账户`:cleancode,`密码`：cleancode

         登录成功后会跳转到Api首页

      3. 查看请求方式及参数

         首次点击对应的方法url或点击`请求参数`

      4. 查看返回形式及参数

         点击对应方法的url后点击`响应参数`

      5. 查看属性具体属性

         如果对应的属性最后的操作栏有`查看`按钮则可以点击查看该属性的具体属性

4. #### 完整demo

   [更多demo请移步](https://github.com/15706058532/api-demo)

5. ####注意事项

   后端配置注意事项

   1. 如果配有拦截器请在拦截其中将`/api/**`放行

      示例：

      ```java
       //1.记录拦截器
       registry.addInterceptor(logInterceptor)
       		 //需要跳过的uri
               .excludePathPatterns( "/api/**")
               .order(0);
       //2.登录拦截器
       registry.addInterceptor(loginInterceptor)
               //需要跳过的uri
               .excludePathPatterns("/user/login", "/user/logout", "/api/**")
               //拦截器的执行顺序
               .order(3);
      ```

   2. 出于安全的考虑，该项目在`v1.3.0`版本后添加了登录才能访问api列表的功能

6. #### 注解介绍

   1. **`@EnableLzfApiGenerator`**

      **使用说明：** 在main函数所在的类上

      **功能：** 激活Api

      **参数说明：** 无

      **示例：**

      ```java
      @SpringBootApplication
      @EnableLzfApiGenerator
      public class ApiDemoApplication {
        public static void main(String[] args) {
           SpringApplication.run(ApiDemoApplication.class, args);
        }
      }
      ```

   2. **`@LzfApiDescribe`**

      **使用说明：** 在类上、方法上、属性上、参数上都可以使用

      **功能：** 给类、方法、属性、参数附加说明

      **参数说明：** `value `= ‘说明内容‘，`must` = true/false(在属性上使用时)

      示例：

      1. 类上使用
         ```java
         @RestController
         @RequestMapping("/user")
         @LzfApiDescribe("用户类")
         public class UserController {
             private Logger logger = LoggerFactory.getLogger(JsonResponseUserController.class);
             private final UserService userService;
         
             @Autowired
             public UserController(UserService userService) {
                 this.userService = userService;
             }
         ```

      2. 方法上使用

         ```java
          @LzfApiDescribe("返回demoResult 无分页")
          @GetMapping("/demoResult")
          @LzfApiResponse(name = "data", clazz = TestUserResp.class)
          public DemoResult testFunction6() {
              DemoResult demoResult = new DemoResult();
              demoResult.setCode(0);
              demoResult.setMsg("OK");
              demoResult.setData(new TestUserResp());
              return demoResult;
          }
         
         ```

      3. 在属性上使用

         ```java
         /**
          * 主键Id
          */
         @LzfApiDescribe(value = "主键Id ")
         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         @Column(name = "id")
         private Long id;
         ```

      4. 在参数上使用

         ```java
         @LzfApiDescribe("uri参数形式")
         @GetMapping("/url/default/{id}")
         public UserResp findByid(@PathVariable @LzfApiDescribe("主键Id") String id) {
               ....
               return userResp;
          }
         ```

   3. **`@LzfApiRequest`**

      **使用说明：**  在方法上使用

      **功能： ** 用来配置前端入参参数属性的配置，减少不必要的属性

      **参数说明：** `musts`={必须的属性多个逗号隔开，\*代表全部}， `noMusts`={非必须的属性多个逗号隔开，\*代表全部}，musts的优先级高于noMusts

      **示例：**

      1. 无配置

         ```java
         @LzfApiRequest()
         public TestUserReq[][] testFunction12(@RequestBody TestUserReq[][] testUserReq) {
             return testUserReq;
         }
         ```

      2. 全部必须 musts={"\*"} 

         ```java
         @LzfApiRequest(musts = {"*"})
         public List<List<TestUserReq>> testFunction12(@RequestBody List<List<TestUserReq>> testUserReqs) {
             return testUserReqs;
         }
         ```

      3. 全部非必须  noMusts = {"\*"}

         ```java
         @LzfApiRequest(noMusts = {"*"})
         public Collection<List<List<TestUserReq>>> testFunction12(@RequestBody Collection<List<List<TestUserReq>>> testUserReqs) {
             return testUserReqs;
         }
         ```

      4. musts={"id","name"},noMusts = {"createTime","updateTime"}

         ```java
         @LzfApiRequest(musts = {"id","name"}, noMusts = {"createTime","updateTime"})
         public TestUserReq testFunction13(@PathVariable(value = "id") Integer id, @PathVariable(value = "name") String name, TestUserReq userReq, TestReq testReq, @RequestBody TestUserReq testUserReq) {
             return testUserReq;
         }
         ```

   4. **`@LzfApiResponse`**

      **使用说明：** 在方法上使用

      **功能： **辅助说明返回字段的类型，在前后端分离的项目中，通常会使用｛code:code,msg:msg,data:data｝的形式向前端传递数据，这时候，由于data的类型为Object类型，无法通过自动解析获得，这时候就需要借助**`@LzfApiResponse`**注解来辅助说明

      **参数说明：** `name`='属性名称' ，`clazz`=(有分页时这里写分页的类名)类名.class，`clazzType`=集合或数组类型,`pageName`='分页类中属性名',`pageClazz`=分页类中属性的类名.class，`pageClazzType`=分页类中属性为集合或数组类型

      **示例：**

      1. 返回类

         ```java
          @LzfApiDescribe("返回demoResult 无分页")
          @GetMapping("/demoResult")
          @LzfApiResponse(name = "data", clazz = TestUserResp.class)
          public DemoResult testFunction6() {
              DemoResult demoResult = new DemoResult();
              demoResult.setCode(0);
              demoResult.setMsg("OK");
              demoResult.setData(new TestUserResp());
              return demoResult;
          }
         ```

      2. 返回分页类

         ```java
         @LzfApiDescribe("返回分页")
         @GetMapping("/page")
         @LzfApiResponse(name = "data", clazz = TestUserResp.class)
         public DemoPage testFunction8() {
             DemoPage<TestUserResp> demoPage = new DemoPage<>();
             List<TestUserResp> testUserResps = new ArrayList<>();
             TestUserResp testUserResp = new TestUserResp();
             testUserResps.add(testUserResp);
             demoPage.setData(testUserResps);
             return demoPage;
         }
         ```

      3. 返回类中含有分页类

         ```java
          @LzfApiDescribe("返回demoResult 有分页")
          @GetMapping("/demoResult/page")
          @LzfApiResponse(name = "data", clazz = DemoPage.class, pageName = "data", pageClazz = TestUserResp.class, pageClazzType = ClassType.List)
          public DemoResult pageDemoResult() {
              DemoResult demoResult = new DemoResult();
              demoResult.setCode(0);
              demoResult.setMsg("OK");
              DemoPage<TestUserResp> demoPage = new DemoPage<>();
              List<TestUserResp> testUserResps = new ArrayList<>();
              TestUserResp testUserResp = new TestUserResp();
              testUserResps.add(testUserResp);
              demoPage.setData(testUserResps);
              demoResult.setData(demoPage);
              return demoResult;
          }
         ```

7. #### 配套工具

      使用[数据库逆向生成工具](https://github.com/15706058532/db-reverse-java)可以方便的根据数据库的表自动生成带有api注解的实体类

      [点击直接下载数据库逆向生成工具](https://raw.githubusercontent.com/15706058532/db-reverse-java/master/%E6%95%B0%E6%8D%AE%E5%BA%93%E9%80%86%E5%90%91%E7%94%9F%E6%88%90%E5%B7%A5%E5%85%B7-3.2.0.jar)

8. #### 联系方式

      **使用过程中有什么问题、建议或意见可通过下面的方式联系我**

      QQ：**964210628**

      Email：**[15706058532@163.com](mailto:15706058532@163.com)**

