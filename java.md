
# 快捷键

1. shift + alt + o 导入类排序
2. ctrl + t 查询接口方法(必须启动项目)
3. ctrl+shift+o 查看当前文件的大纲
4. alt+方向键左右 历史前进后退
5. ctrl + f12 查看方法定义
6. 在service中的方法，选中后鼠标右键“转到实现”可进入impl中的方法

# 设计

1. 在java中，一个单独的java文件是无法通过java xxx.java运行的，需要先编译，再运行。通过javac xxx.java编译，生成一个xxx.class文件，这个class文件称为字节码文件，再通过java xxx运行。
2. 一个 Spring Boot 服务从命令到可访问，大致经历这些阶段：Maven 阶段、JVM 阶段、Spring 容器阶段、Web 服务器阶段、外部资源连接阶段、业务初始化阶段

# 基础概览

1. SpringBootApplication标注在某个类，运行这个类的main方法来启动SpringBoot应用
2. 配置文件放在 src/main/resources目录application.yml文件
3. 实体类: 通常位于 entity 或 model 包下,User.java, System.java.用于存储数据库中的数据的类，通常对应数据库中的一个表。
4. 后端项目则先找 Spring Boot 启动类。当前启动类是 FullstackMallApplication.java，它所在包名是 com.example.fullstackmall.service。Spring Boot 默认会从启动类所在包向下扫描组件，因此 Controller、Facade、Mapper、Config 等类如果放在这个包的子包下，就更容易被自动发现。你以后新增后端类时，也要有“包扫描范围”的意识，不要随便放到完全无关的包路径里。

## 类
我们通常根据职责（职责单一原则）来给类分类。下面按你最容易理解的业务流从外到内列举：
1. Controller（控制器类）
作用：对应前端的 API 接口。负责接收前端传来的请求参数，调用 Service 处理业务，然后把结果返回给前端。
特点：里面通常只有简单的参数校验和转发，不写复杂的业务逻辑。

2. Service（业务逻辑类）
作用：系统的核心。所有的业务规则、计算、判断都在这里。
特点：通常分为接口（UserService）和实现类（UserServiceImpl）。它负责调用 Mapper 查数据库，或者调用其他 Service。

3. Mapper / DAO（数据访问类）
作用：专门负责和数据库打交道（增删改查）。
特点：通常在 Spring Boot + MyBatis-Plus 中，它就是一个接口（UserMapper），继承框架的 BaseMapper，不需要写实现类。

4. Entity（实体类）
作用：和数据库表字段一一对应的类。表里有什么字段，它里面就有什么属性。**如果不做vo，那么entity的数据都会返回给前端**
特点：通常放在 entity 或 domain 包下，使用 @Data 注解。

5. DTO（Data Transfer Object，数据传输对象）
作用：前端传给后端的参数，或者后端微服务之间传递的数据，通常封装成 DTO。
例子：前端注册时只传了手机号和密码，但 Entity 里还有创建时间、状态等字段。这时候我们用一个 UserRegisterDTO 来接收前端参数。这样前端就可以通过参数来修改创建时间和状态。

6. VO（View Object，视图对象）
作用：后端返回给前端展示的数据。
例子：前端查用户列表，需要展示用户的订单数量，但数据库 User 表里没有订单数量。这时候查出来后，组装成一个 UserInfoVO 返回给前端。

7. Config（配置类）
作用：替代传统的 XML 配置文件。比如你写的 MinioConfig，用来在启动时生成并注入一些全局对象（Bean）。

8. Utils（工具类）
作用：存放静态方法（static），比如日期格式化、加密解密、字符串处理。不需要实例化，直接用 类名.方法名() 调用。

9. Exception（异常类）
作用：定义业务报错。比如 BusinessException，当用户余额不足时抛出，由全局异常处理器统一捕获并返回给前端友好的提示。

## 配置类（注解）
注解功能上都是"把类交给 Spring 管"，区别只是语义标签——就像前端把文件放进 services/、components/、api/ 一样，是为了让人一眼知道职责。
```java
@Configuration: 用于标识一个类为配置类，该类主要用于定义和组织 Spring 应用程序的配置信息
@Override :关键且常用的‌标记型注解（Marker Annotation）‌。它的主要作用是显式地声明某个方法旨在‌重写（Override）‌父类或接口中的方法
@Service：业务层组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
@Component：通用组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
@Repository：
@Controller：
@RestController：
@Bean
```

# spring核心
1. IoC 和 DI
IoC（控制反转）：对象的创建和依赖管理交给 Spring 容器。
DI（依赖注入）：容器把依赖注入到对象里。
注入方式：构造器注入（推荐）、setter 注入、字段注入（@Autowired）。

2. Bean 生命周期
实例化 → 属性注入 → 初始化（@PostConstruct）→ 使用 → 销毁（@PreDestroy）。

3. Bean 作用域
   
  | 作用域 | 说明 |
  | -- | -- |
  | singleton |	默认，容器里只有一个实例 |
  | prototype |	每次获取都创建新实例 |
  | request	| 每个 HTTP 请求一个 |
  | session	| 每个会话一个 |

4. 常用注解
    | 注解 |	作用 |
    |--|--|
    |@Component	|通用组件 |
    |@Service	| 业务层|
    |@Repository	|数据访问层|
    |@Controller / @RestController |	控制层 |
    |@Configuration	|配置类|
    |@Bean	声明 | Bean |
    |@Autowired	| 注入 |
    |@Value	| 注入配置值 |
    |@ConfigurationProperties	| 批量绑定配置 |


## 文件层级结构

主流模式
cn.dip.platform
├── common          # 通用组件（所有业务共用）
│   ├── config      # 配置类
│   ├── exception   # 异常定义
│   ├── enums       # 枚举
│   ├── utils       # 工具类
│   ├── result      # 统一返回结果
│   └── constant    # 常量
├── datasource      # 业务模块1：数据源管理
│   ├── controller
│   ├── service
│   ├── mapper
│   ├── domain
│   │   ├── do      # 数据库实体
│   │   ├── dto     // 传输对象
│   │   └── vo      // 视图对象(将前端需要的数据从dto中筛选)
│   └── vo
├── cluster         # 业务模块2：集群管理
├── job             # 业务模块3：作业管理
└── adapter         # 基础设施层：适配器（你之前的数据源适配器就放这）
├── spi         # 适配器接口
├── registry    # 注册中心
└── impl        # 各数据源实现

按业务模块将controller、service、mapper、domain放在一起，改起来方便

传统写法，小型项目
cn.dip.platform
├── controller
│   ├── DataSourceController.java
│   └── ClusterController.java
├── service
│   ├── DataSourceService.java
│   └── ClusterService.java
├── mapper
│   ├── DataSourceMapper.java
│   └── ClusterMapper.java
├── entity
│   ├── DataSourceDO.java
│   └── ClusterDO.java
├── dto
├── vo
├── config
├── utils
└── exception

结构简单，新手容易理解。

## ‌四层架构
┌────────────────────────────────────────┐
│  Controller（控制层）                    │  ← Vue Router / API 路由
│  ├─ 接收请求、参数校验                    │
│  └─ 调用 Service                         │
├────────────────────────────────────────┤
│  Service（业务层）                        │  ← services/ 业务逻辑
│  ├─ 业务规则、编排                        │
│  └─ 调用 Repository                      │
├────────────────────────────────────────┤
│  Repository / Mapper（数据访问层）         │  ← api/ 请求封装
│  ├─ 拼 SQL / ORM                         │
│  └─ 访问数据库                            │
├────────────────────────────────────────┤
│  Entity / DO / DTO（数据模型）             │  ← TypeScript interface
│  └─ 定义数据结构                          │
└────────────────────────────────────────┘
### domain

domain层，文件夹名称不固定（domain，entity‌，model‌），可能文件夹随便取，文件名叫XXXDO.java。文件中通常使用@Data等Lombok注解
存放实体类，与数据库中的属性保持一致，存放属性和操作属性的get，set方法
@Data  Lombok 注解，自动生成 getter/setter
@Data 实际上是一个“全家桶”，它等价于以下五个注解的组合：
1. @Getter（生成所有属性的 get 方法）
2. @Setter（生成所有属性的 set 方法）
3. @ToString（生成 toString 方法）
4. @EqualsAndHashCode（生成 equals 和 hashCode 方法）
5. @RequiredArgsConstructor（为 final 和 @NonNull 字段生成构造方法）

```java
public class user {
    private String id;
    private String name;

    public String getName(){
        return this.name
    }

    public void setName(String name){
        this.name = name
    }
}
```

mapper层，文件夹名称不固定（mapper，dao ），可能文件夹随便取，文件名叫XXXMapper.java\
针对数据库进行操作，主要实现增删改查等操作。于mybatis中方法一一映射。mapper接口中定义的方法，会自动被mybatis-plus实现。mybatis-plus有Service Interface，用于定义业务逻辑。Mapper Interface，用于定义数据库操作。

```java
// 这里的<User>就是实例domain层
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus 已提供基础 CRUD，复杂 SQL 可在此定义
    User selectById(Long id);
    // 上面的方法是MyBatis-Plus提供的基础方法，如果自定义了 SQL 逻辑并且是同名方法，‌会覆盖 BaseMapper 里的默认实现‌，调用时走你自己写的 SQL
    // User selectById(@Param("id") Long id);
}
```

对应 XML 里会有自定义的 SQL：

```xml
<select id="selectById" resultType="User">
  SELECT u.*, r.role_name 
  FROM sys_user u
  LEFT JOIN sys_user_role ur ON u.id = ur.user_id
  WHERE u.id = #{id} AND u.deleted = 0
</select>
```

### service层

给controller层的类提供接口,仅包含方法声明，不涉及具体实现逻辑.
service文件夹的接口定义了业务逻辑的"做什么"，impl文件夹的实现类负责"怎么做"
service层中还有一个impl层，impl文件夹主要用于存放接口的具体实现类，是代码分层设计里的重要组成部分。

```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        // 业务逻辑：例如权限校验、数据过滤
        return userMapper.selectList(null);
    }
}
```

### controller层

给前端提供接口的类，主要负责接收前端的请求，调用service层的方法，返回结果。
关键字： @RestController、@GetMapping、@PostMapping、@PathVariable、@RequestBody

```java
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> list() {
      // 只负责调用 Service 并返回结果
      return userService.getAllUsers();
    }
}

// 如何查看后端接收的接口参数-PathVariable
  /**
   * 接收路径中的 ID
   * URL: /api/user/1001
   */
  @GetMapping("/{id}")
  public String getUserById(@PathVariable("id") Long userId) {
      return "正在查询用户ID: " + userId;
  }
  /**
   * 接收查询参数-RequestParam
   * URL: /api/search/list?keyword=Java&page=1
   */
  @GetMapping("/list")
  public String searchList(
    @RequestParam(value = "keyword", required = true) String keyword,
    @RequestParam(value = "page", defaultValue = "1") Integer page) {
      return "搜索关键词: " + keyword + ", 当前页: " + page;
  }

  // 接收 JSON 请求体参数
  // 定义实体类dto，文件名UserDTO.java
  public class UserDTO {
    private String name;
    private Integer age;
  }
  
  @PostMapping("/create")
  public String createUser(@RequestBody UserDTO user) {
    return "正在创建用户: " + user;
  }

  // 如果前端传递的是数组，比如[1,2,3]
  @PostMapping("/create")
  public String createUser(@RequestBody List<Long> ids) {
    return "正在创建用户: " + ids;
  }

  /**
   * 如果不想定义实体类，也可以用 Map 接收 (不推荐复杂场景使用)
   * 不推荐原因：
   * 1. 本来定义age字段为数字，前端手滑传了个字符串 "18"，你强转 (Integer) 直接崩溃。如果用 DTO，Spring 会自动帮你把类型转换好。
   * 2. 字段名毫无提示，全靠猜
   * 3. 无法配合校验注解，DTO 直接加 @NotBlank 就行
   */
  @PostMapping("/add/map")
  public String addUserMap(@RequestBody Map<String, Object> params) {
    String name = (String) params.get("name");
    String age = (String) params.get("age");
    return "收到参数: " + params.toString();
  }

  // get请求不能使用@RequestBody接收参数
```

```java
// 不分层架构：
@RestController
public class UserController {

  // 直接在 Controller 中注入 SqlSessionFactory 或 JdbcTemplate，甚至硬编码 SQL
  @Autowired
  private JdbcTemplate jdbcTemplate; 

  @GetMapping("/users")
  public List<User> getUsers() {
    // 1. 表现层逻辑：接收请求（此处省略参数处理）
    
    // 2. 业务层逻辑：假设这里有一些复杂的判断
    System.out.println("正在查询用户...");
    
    // 3. 持久层逻辑：直接编写 SQL 并执行
    String sql = "SELECT * FROM user";
    List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    
    return users;
  }
}
```

## Mybatis拦截器

mybatis-config.xml中引入拦截器

```java
@Intercepts：标识该类是一个拦截器
@Signature：指明自定义拦截器需要拦截哪一个类型，哪一个方法；
    type：对应四种类型中的一种（Executor、StatementHandler、ParameterHandler、ResultSetHandler）；
    method：对应接口中的哪个方法；
    args：对应哪一个方法参数类型（因为可能存在重载方法）

@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})

```

固定写法

```java
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class MyInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1. 前置处理：例如获取 SQL，修改参数，记录日志等
        System.out.println("Before executing method...");
        
        // 2. 执行原始方法
        Object result = invocation.proceed();
        
        // 3. 后置处理：例如处理结果集，清理资源等
        System.out.println("After executing method...");
        
        return result;
    }

    @Override
    public Object plugin(Object target) {
        // 生成代理对象，通常直接使用 Plugin.wrap
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 获取配置文件中配置的属性
    }
}
```

## 概念
```text
什么是注解，注解有什么作用，有哪些注解？
- 在 Java（尤其是 Spring Boot）里，注解（Annotation）本质上就是一种“贴在代码上的标签”。它本身没有任何逻辑，但底层的框架（比如 Spring）看到这个标签，就会给这个类或方法加上对应的“魔法（额外功能）”。
1. 标识类（给类贴标签）
@Component / @Service / @Controller / @Repository
作用：告诉 Spring：“启动的时候，把我也变成一个 Bean 放进容器里！”
区别：底层功能一模一样，只是语义不同。写业务逻辑用 @Service，写接口用 @Controller（如果你用前后端分离，一般用 @RestController）。
@Configuration
作用：告诉 Spring：“这是一个配置类，里面会有 @Bean 组装对象的方法”。
@Data / @Getter / @Setter（来自 Lombok）
作用：在编译时自动帮你生成 Get/Set 方法，不用手写。就像前端 TS 里自动生成的 interface 映射。
2. 标识方法/属性（处理请求和注入）
@Autowired / @Resource
作用：自动注入。把 Spring 容器里的 Bean 拿过来赋值给这个变量。
@Bean
作用：贴在方法上，把方法 return 的对象交给 Spring 管理。
@RequestMapping / @GetMapping / @PostMapping
作用：路由映射。告诉前端：“你发这个 URL 的 GET/POST 请求，就来找我这个方法处理”。这相当于前端 Vue Router 里的路由配置。
@RequestBody / @RequestParam / @PathVariable
作用：告诉 Spring：“从请求的哪个位置拿参数”。（拿 JSON、拿 Query 参数、拿 URL 路径参数）。
3. 增强功能（AOP 和校验）
@Transactional
作用：数据库事务。方法里如果有多个数据库操作（比如下单扣库存+扣钱），只要有一个报错，全部回滚，保证数据一致。
@Override
作用：告诉编译器：“我这个方法是重写父类的”。如果你名字拼错了，编译器会立刻标红报错。
@Valid 或 @Validated
作用：配合 DTO 里的 @NotBlank、@NotNull 等，自动校验前端传过来的参数是否合法。
```

1. spring mvc框架
   1. Spring 3.1及以后版本中，推荐使用Java配置而非XML配置。springMvc注解，主要用于‌控制请求映射‌、‌绑定请求参数‌以及‌处理响应数据‌，是开发 Java Web 应用时的核心工具。常用的核心注解包括 ‌@Controller‌、‌@RestController‌、‌@RequestMapping‌、‌@RequestParam‌、‌@PathVariable‌ 和 ‌@ResponseBody‌ 等。
      @Controller 控制器注释，返回视图，以html和jsp等视图引擎渲染
      ‌@RestController‌ 控制器注释，返回json数据，前后端分离。
      @RequestMapping 控制器方法注释，指定请求路径
      @RequestParam 控制器方法参数注释，绑定请求参数
      @PathVariable 控制器方法参数注释，绑定路径变量
      @ResponseBody 控制器方法返回值注释，返回json数据
2. springBoot框架
   将spring, spring mvc框架和mybatis-plus框架整合起来，形成一个完整的spring boot应用。
   @Component 注解，将类标记为组件，自动扫描并加载到spring容器中。
3. mybatis-plus框架
   1. updateById方法，根据id更新数据。不需要在路径中拼接id，直接在RequestBody中传递id。
   2. 从3.4.0开始，mybatis-plus支持分页插件。需要用户在配置类中配置分页插件。
   3. 常用字段注解：
      @TableField(value = "create\_time", fill = FieldFill.INSERT)：在插入时自动填充字段值
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")：在返回json数据时，格式化时间字段
4. bean
   ‌Bean‌ 是由 ‌Spring IoC 容器‌（控制反转容器）管理的一个对象。在传统的 Java 开发中，对象通常由开发者通过 new 关键字手动创建；而在 Spring Boot 中，对象的创建、初始化、销毁等生命周期全部交给 Spring 容器统一管理。这些**被容器管理的对象就称为 ‌Bean‌**。类似前端的vuex，一个地方定义，后续在其他组件中使用定义都是同一个实例，保证了状态一致和共享
   - 怎么注入，注入了如何使用？
   1. 使用@Autowired注解，自动注入依赖。类似前端的vuex引入store和方法。@Autowired就是注入和使用
   - @Bean的用法
   1. 这是另外的概念，自己动手做组装，告诉 Spring“把我这个方法 return 出来的对象变成 Bean”
   
5. maven项目
6. pom.xml可解读java版本，项目几个模块（modules）
7. Lombok 框架
   Lombok是一个Java库，它消除了重复的代码，使开发人员能够更快速地编写代码。它通过注解的方式自 动为 Java 类生成常用的样板代码（Boilerplate Code），从而极大地简化了 Java 开发过程  
   常用注解示例:
    1. @Data‌：最常用的注解，等同于同时使用 @Getter、@Setter、@RequiredArgsConstructor、@ToString 和。
    | 组成部分 | 作用 |
    | --- | --- |
    | @Getter | 所有字段自动生成 getter 方法 |
    | @Setter | 所有非 final 字段自动生成 setter 方法 |
    | @ToString | 自动生成 toString 方法，打印所有字段值 |
    | @EqualsAndHashCode | 自动生成 equals 和 hashCode 方法 |
    | @RequiredArgsConstructor | 自动生成包含「final 字段 + @NonNull 字段」的构造方法 |
      
   2. @EqualsAndHashCode。适用于大多数数据载体类（如 Entity、DTO）。
   ‌3. @Getter / @Setter‌：仅为指定字段生成 getter 或 setter 方法。
   ‌4. @NoArgsConstructor / @AllArgsConstructor‌：生成无参构造器或全参构造器。
   5. @Builder‌：提供构建者模式（Builder Pattern）的支持，方便链式调用创建对象，特别适用于字段较多的类。
   ‌6. @Slf4j‌：自动生成一个名为 log 的 SLF4J 日志对象，方便直接在类中进行日志记录。
   ‌7. @NonNull‌：在方法参数或字段上使用，自动生成空值检查代码，若传入 null 则抛出
   8. @Accessors(chain = true) // 🔑 开启链式调用


# MyBatis-Plus基础概览
1. MyBatis-Plus 条件构造器（Wrapper）是一个用 Java 代码来拼 SQL 查询条件的工具。你不用手写 WHERE、AND、OR、LIKE 这些 SQL 片段，而是调用它的方法，它帮你生成对应的 SQL。
```java
// java写法
@Select("SELECT * FROM dip_system_datasource WHERE adapter_type = #{type}")
List<DataSourceDO> selectByType(String type);

// 条件构造器
dataSourceMapper.selectList(
  new LambdaQueryWrapper<DataSourceDO>()
    .eq(DataSourceDO::getAdapterType, "minio")
);
```
在什么场景用
单表查询：用 LambdaQueryWrapper 最方便。
多表联查：条件构造器不擅长，还是写 XML 或 @Select。
复杂 SQL：比如子查询、窗口函数，建议手写 SQL。
简单更新：LambdaUpdateWrapper 可以只更新指定字段。

# Java 8+ 新特性

1. Lambda 表达式
   list.forEach(x -> System.out.println(x));
2. 方法引用
   DataSourceDO::getHost   // 等价于 ds -> ds.getHost()
3. Stream API
    list.stream()
    .filter(x -> x > 0)
    .map(String::valueOf)
    .collect(Collectors.toList());
  中间操作：filter、map、sorted、distinct
  终端操作：collect、forEach、count、reduce
  惰性求值：没有终端操作，中间操作不会执行


# java基础

## 设计

1. Service 只写接口，逻辑写在 Impl 里，是 Java 面向对象设计的核心机制。

```java
  // 接口里的普通方法，默认就是"抽象方法"：抽象方法 = 只有声明，没有方法体
  public interface DataSourceAdapter {
    void initialize(ConnectionConfig config) throws Exception;
    ConnectionTestResult testConnection();
    void close();
  }
  // 这样设计的好处是可以实现方式可能多种多样，逻辑如何写死在方法中，那么需要根据不同条件去判断，失去了扩展性

  // 如何根据"对象实际类型 + 方法签名"找逻辑的实现方法
  // 某文件a,接口里声明方法
  public interface DataSourceAdapter {
    void initialize(ConnectionConfig config) throws Exception;
  }
  // 某文件b，通过 implements DataSourceAdapter 完成绑定
  public class MysqlAdapter implements DataSourceAdapter {
    @Override
    public void initialize(ConnectionConfig config) throws Exception {
        // 你的具体逻辑
    }
  }
  
```

那么service中的方法名和impl中的方法实际逻辑是如何绑定的？
编译期通过 implements 导致的绑定

2. Class\<? extends DataSourceAdapter> clazz = adapterClasses.get(type)
   1. 反射实例化：根据运行时的信息动态决定 new 哪个类
   2. 反射是如何拿到实例和加载对应的实现方法？反射后就拿到了对应的实例，然后就可以调用实例的方法
3. SPI（Service Provider Interface，服务提供者接口）是 Java 内置的一套"插件发现机制"：接口方只定义规范，实现方把实现类写在一个约定好的文件里，运行时由 JVM 自动发现并加载。

你的项目里，适配器框架就是靠 SPI 把十几个 adapter-xxx 模块"插"进来的。


## 集合类

Java 集合框架中还有List、 ‌Set‌、‌Map‌ 和 ‌Queue‌ 四大类常用接口

1. Set 系列（无序、不可重复）

```java
HashSet‌,基于哈希表，无序，查询速度极快（O(1)）
适用于需要‌去重‌或‌快速判断元素是否存在‌的场景
Set<String> fruitSet = new HashSet<>();

‌TreeSet,基于红黑树，元素会自动‌排序‌
TreeSet<Integer> numSet = new TreeSet<>();
numSet.add(5);
numSet.add(1);
numSet.add(3);
System.out.println(numSet); // 输出 [1, 3, 5]，自动升序

‌LinkedHashSet‌,保持元素的‌插入顺序
LinkedHashSet<String> linkSet = new LinkedHashSet<>();
```

1. Map 系列（键值对 Key-Value）

```java
1. HashMap‌,最常用，无序，允许 Key 和 Value 为 null，性能高。
Map<String, Integer> scoreMap = new HashMap<>();

2. TreeMap‌,Key 会按照自然顺序或自定义规则‌排序‌
TreeMap<String, String> treeMap = new TreeMap<>();

3. LinkedHashMap‌,保持 Key 的‌插入顺序‌，常用于实现 LRU 缓存。
LinkedHashMap<String, String> lhm = new LinkedHashMap<>();
```

1. Queue 系列（队列）

## 数组

java中数组是一种特殊的变量，它可以存储多个相同类型的变量。数组的定义和使用如下：

```java
// 已知元素个数
int[] arr = {1,2,3};
String[] arr2 = {"a","b","c"};

// 已知个数，具体后续填充。这里arr是一个int类型的数组，它可以存储5个int类型的变量。
int[] arr = new int;

// 未知个数，具体后续填充。这里arr是一个int类型的数组，它可以存储任意多个int类型的变量。
int[] arr = new int[0];

// 已知个数，限定后续填充的元素类型。这里arr是一个int类型的数组，它可以存储5个int类型的变量。
int[] arr = new int[5];
```

## 构造方法

1. 方法名与类名相同，大小写一致
2. 没有返回值
3. 可以有多个构造方法
4. 默认构造方法
5. 有参构造方法
6. 无参构造方法
7. 构造方法可以调用其他构造方法

## 构造方法调用

1. 每次new一个对象，就会调用一个构造方法，不需要手动调用
2. 构造方法可以调用其他构造方法

## 静态方法

1. 方法名前加static关键字
2. 静态方法只能调用静态方法和静态变量，不能调用非静态变量、方法。
3. 非静态方法可以调用静态方法和静态变量
4. 静态方法没有this关键字
   静态随着类的加载而加载（加载静态的时候可能都没有非静态变量和方法），非静态随着对象的创建而加载

## 继承

1. 输入本类的变量使用this.变量名
2. 输入父类的变量使用super.变量名
3. override重写父类的方法,需要重写的方法名和参数列表与父类的方法名和参数列表相同

## 多态

```java
Fu z = new Zi();
// 这里的Fu就是多态形式，z只是一个变量，这里调用的是Fu类的方法
Zi z = new Zi();
// 这里的Zi就是多态形式，z只是一个变量，这里调用的是Zi类的方法
// 多态可以实现不同的对象调用不同的方法
```

1. 为什么需要多态，多态能解决什么问题？
2. 什么是类型转换，类型转换解决了什么问题？

## 接口(implements)

1. 接口就是一个规则，独立于继承体系之外
2. 接口是一种特殊的类，它没有方法体，只有方法的声明
3. 接口可以实现多态，因为接口是一个抽象类，所以可以被实现类实现

## 内部类

1. 有几种内部类

```java
静态内部类只能访问外部类的静态变量和方法，如果想访问非静态变量和方法，需要创建外部类的对象
public class Outer {
  int a = 1;
  static int b = 2;
  public class Inner {
    public static void fn() {
      Outer o = new Outer();
      System.out.println(o.a);
      // 静态内部类只能访问外部类的静态变量和方法
      System.out.println(b);
    }
  }
}
```
## enum的定义和使用

```java

public enum TaskStatus {
  DRAFT("DRAFT", "草稿"),
  DISABLE(0, "禁用");

  /**
   * 状态码（存数据库用）
   */
  private final Integer code;

  /**
   * 描述（前端展示用）
   */
  private final String desc;

  // 一般每个枚举类里都会加几个静态工具方法，方便根据 code 找枚举、判断是否存在等

  // 获取code
  public String getCode() {
    return code;
  }

  /**
   * 判断 code 是否存在
   */
  public static boolean isValid(Integer code) {
    return getByCode(code) != null;
  }

}

// 使用的时候

// 获取code
DataSourceStatusEnum.DRAFT.getCode()

// 使用Lombok帮你自动生成 getter 方法和全参构造方法‌，不用手写一堆样板代码

// 普通方式
public enum DataSourceStatusEnum {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private final Integer code;
    private final String desc;

    // 👇 这些 getter 全是手写的样板代码，又长又无聊
    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    DataSourceStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

// Lombok
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter // 🔑 就加这一行，上面那一堆 getter 自动生成
@AllArgsConstructor // 🔑 自动生成全参构造
public enum DataSourceStatusEnum {
  ENABLE(1, "启用"),
  DISABLE(0, "禁用");

  private final Integer code;
  private final String desc;
}

```
## Map

```java
// 典型的定义方式：适配器类型 -> 采集类型 的映射表
private static final Map<String, Integer> ADAPTER_TO_COLLECTOR = new HashMap<>();
// 或者
private static final Map<DataSourceTypeEnum, Integer> ADAPTER_TO_COLLECTOR = new ConcurrentHashMap<>();
// Map 的标准方法，往映射表里加一条键值对
ADAPTER_TO_COLLECTOR.put(key, value)

// 根据适配器类型快速判断采集类型
Integer collectionType = ADAPTER_TO_COLLECTOR.get(adapterType);

```
## Stream
Stream是**Java 8** 引入的「数据处理管道」‌，它不是集合、不存数据，而是一套‌专门用来处理集合/数组数据的「流水线操作工具」‌——你告诉它要对数据做什么（过滤、转换、排序、分组……），它帮你把循环、判断这些底层细节都做了。本质是 Java 提供的一种‌「声明式数据处理工具」‌——用它写出来的代码更简洁、更易读、更接近「我想要什么」，而不是「我要怎么做」。
简单说：‌for 循环是「**命令式**」，Stream 是「**声明式**」‌。

* for 循环：一步步告诉机器怎么干（怎么循环、怎么判断、怎么装集合）
* Stream：直接说我要什么结果（过滤、转换、分组），具体怎么循环交给 JDK

传统for循环
```java
List list = dataSourceService.list();

// 1. 先建一个结果集合
List result = new ArrayList<>();
for (DataSourceDO ds : list) {
  // 3. 判断是不是 MySQL
  if ("mysql".equals(ds.getType())) {
    ...
  }
}
```

Stream（声明式）

```java
// 将list转为stream
List result = list.stream()
    .filter(ds -> "mysql".equals(ds.getType()))   // 只要 MySQL

// 将集合转
```

## for循环
```java
  // Java 枚举的 values() 返回的是"所有枚举常量的数组, 提供数据源
  for (TaskStatus status : values()) {
    if (status.code.equals(code)) {
      return status;
    }
  }
  // TaskStatus的数据源，然后获取数据源的code
```

# 后端概念

1. BCrypt 校验密码的原理
   BCrypt在校验时，会从存储的哈希值中提取出盐值（Salt）和工作因子（Cost Factor），然后用这些参数对用户输入的明文密码进行重新哈希，最后将新生成的哈希值与存储的哈希值进行比对
   数据库中存储的BCrypt哈希值格式通常如下：
   $2a$12$R9h/cIPz0gi.URNNX3kh2OPb9M3nS8J6pVZ7f1w6F5jKlx2yA4k6i
     $2a\$：算法版本
   12：工作因子（2^12轮迭代）
   R9h/cIPz0gi.URNNX3kh2O：盐值（22个字符）
   Pb9M3nS8J6pVZ7f1w6F5jKlx2yA4k6i：实际哈希值
   校验时，BCrypt会自动取出盐值部分，用相同的盐和工作因子对输入密码进行哈希，然后与后面的哈希值比较。
2. 什么是 JWT
   JWT 是一种紧凑的、自包含的网络令牌格式，用于在各方之间安全地传输信息。因为它本身是 JSON 格式的数据，经过签名后可以保证内容未被篡改。
   简单来说，JWT 就是一个加密签名的 JSON 字符串，常用于身份认证和信息交换。
   JWT 通常由 3 段组成：Header（头部）、Payload（载荷）、Signature（签名），中间用点号连接。
   Payload 默认是 Base64Url 编码，不是加密！任何人都可以解码查看内容，所以绝对不要存放敏感信息（如密码、信用卡号）。
   当前项目即使 JWT 还没过期，也会在 Filter 里重新查数据库用户，并检查 status == 1。这意味着管理员停用某个账号后，这个账号即使手里还有未过期 token，也会被拒绝访问。这是比“完全相信 JWT 内容”更稳妥的设计。

# 开发中注意事项

1. java不同于js，有类和实例的概念，本着单一原则，创建的实例需要缓存起来，每次直接去缓存中读取创建的实例，而不是每次去new
2. 反射实例化: 平时我们 new 对象是"写死在代码里的"，反射实例化是"根据运行时的信息动态决定 new 哪个类"

```java
// 普通实例化：编译期就知道要 new 谁
DataSourceAdapter a = new MysqlAdapter();

// 反射实例化：运行时才知道要 new 谁
Class<?> clazz = Class.forName("cn.dip.adapter.mysql.MysqlAdapter");
DataSourceAdapter a = (DataSourceAdapter) clazz.getDeclaredConstructor().newInstance();

// 普通 new 有个致命限制：类名必须写死在代码里
public DataSourceAdapter getAdapter(String type) {
    if (type.equals("mysql")) return new MysqlAdapter();
    if (type.equals("dm")) return new DmAdapter();
    if (type.equals("postgresql")) return new PostgresAdapter();
    ...
}
```

这种写法繁琐，反射就是解决这个问题的：把"要 new 哪个类"从编译期推迟到运行期。

```java
  // 第 1 步：拿到 Class 对象
  Class<?> clazz = Class.forName("cn.dip.adapter.mysql.MysqlAdapter");
  // 或者
  Class<?> clazz = adapterClasses.get(type);

  // 第 2 步：拿到构造器
  Constructor<?> constructor = clazz.getDeclaredConstructor();

  // 第 3 步：调用构造器创建实例
  Object instance = constructor.newInstance();
```

3. Jackson 是 Java 生态里最主流的 JSON 处理库，负责在 Java 对象和 JSON 字符串之间来回转换。

前端发送:  {"name": "张三", "age": 25}
后端接收:  转成 Java 对象 User(name="张三", age=25)
后端响应:  Java 对象转成 JSON 返回
最常用的两个类: ObjectMapper(主入口，JSON 与对象互转), TypeReference(携带泛型信息，解决类型擦除)

4. selectList 本身不保证任何顺序。SQL 标准里，没有 ORDER BY 的查询，返回顺序由数据库实现决定，可能受执行计划、索引、并发、分片、副本等因素影响，每次都可能不一样。要保证每次返回顺序一致，必须显式指定 ORDER BY，并且排序字段最好能唯一确定一行。
```text
为什么默认顺序不稳定
没有 ORDER BY：数据库可以按任何顺序返回，比如按存储顺序、索引顺序、并行扫描顺序。
分布式数据库（如 Doris）：数据分散在多个 BE、多个分片，合并结果的顺序更不固定。
并发和缓存：不同时间执行，执行计划可能不同，顺序也可能变。
索引变化：加索引、删索引、统计信息更新，都可能改变返回顺序。
所以，不要依赖“看起来稳定”的顺序，比如“平时都是按 id 返回的”，这不可靠。
```

# mysql

CRUD
1. Create（插入），核心是 INSERT INTO。自增主键可以省略。
  - 指定列插入（推荐，字段清晰）
    - INSERT INTO students (sn, name, qq) VALUES (10001, '孙悟空', '11111');
  - 多行插入
    - INSERT INTO students (sn, name) VALUES (10002, '猪悟能'), (10003, '沙悟净');
  - 如果遇到唯一键冲突想“存在则更新”，用 ON DUPLICATE KEY UPDATE
    - INSERT INTO students (sn, name) VALUES (10004, '沙悟净') ON DUPLICATE KEY UPDATE name = VALUES(name);
    
2. Retrieve（查询），不要用 SELECT *，明确写列名。WHERE 过滤、ORDER BY 排序、LIMIT 分页是日常三件套。
  SELECT id, name, english FROM exam_result WHERE math > 80 ORDER BY english DESC LIMIT 10;
  WHERE 和 HAVING 的区别——WHERE 在分组前过滤行，HAVING 在 GROUP BY 后过滤组

3. Update（更新）
  UPDATE exam_result SET math = 80 WHERE name = '孙悟空';
  不带 WHERE 的 UPDATE 会更新全表。线上操作前先 SELECT 验证条件
4. Delete（删除）
  DELETE FROM exam_result WHERE name = '猪悟能';
  不带 WHERE 会清空表。面试常对比 DELETE / TRUNCATE / DROP：DELETE 是 DML，可回滚（InnoDB），逐行删；TRUNCATE 是 DDL，不可回滚，重置自增，速度快；DROP 直接删表结构

# redis

Redis 在你全栈项目里最可能的角色是缓存，其次是分布式锁和简单计数/排行榜。面试问 Redis，核心是：五种数据类型分别对应什么场景。
String（字符串）
最基础的类型。命令：SET key value、GET key、INCR key（原子自增）、SETEX key seconds value（带过期时间）。
场景：缓存对象 JSON、计数器（点赞数、访问量）、分布式锁（SET key value NX EX 30）。

Hash（哈希）
一个 key 下存多个 field-value，适合存对象的各个字段，可以单独更新某个字段。
命令：HSET user:1 name "张三" age 25、HGET user:1 name、HGETALL user:1。
场景：用户信息、商品信息。相比 String 存整个 JSON，Hash 可以只读/改某个字段，不用整体序列化反序列化。

List（列表）
双向链表，可以从左/右插入弹出。
命令：LPUSH、RPUSH、LPOP、RPOP、LRANGE key 0 -1。
场景：简单消息队列（LPUSH + BRPOP）、最新消息列表。

Set（集合）
无序、自动去重。命令：SADD、SMEMBERS、SINTER（交集）、SUNION（并集）。
场景：共同好友（交集）、去重统计（独立 IP）、标签系统。

Sorted Set（有序集合，ZSet）
每个元素带一个 score，按 score 排序。命令：ZADD key score member、ZRANGE key 0 -1 WITHSCORES、ZREVRANGE。
场景：排行榜（score 是分数/时间戳）、延迟队列（score 是执行时间戳）


# 代码写法

1. 使用@Resource还是@Autowried？
   1. 处于纯 Spring/Spring Boot 生态中，且团队没有特殊规定，则使用@Autowired.如果你希望代码‌减少对 Spring 特定注解的依赖或者你更习惯‌按名称（By Name）‌进行精确注入则使用@Resource。但是Spring 4.3+ 开始推荐final 写法
   ```java
     @Service
     public class OrderService {
         private final PaymentService paymentService;

         // 推荐：构造函数注入，无需注解（Spring 4.3+ 自动识别单构造函数）
         public OrderService(PaymentService paymentService) {
             this.paymentService = paymentService;
         }
     }
   ```
2. Class\<? extends DataSourceAdapter> clazz = adapterClasses.get(type);
  上面的写法是反射实例化
3. private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
```java
// 创建 Jackson 的 ObjectMapper 实例，并关闭"遇到未知字段就报错"的严格模式,这是 Spring Boot / Jackson 项目里最常见的配置之一
public class User {
    private String name;
    private Integer age;
    // getter/setter
}
{
  "name": "张三",
  "age": 25,
  "email": "zhangsan@example.com"   // ← User 类里没有 email 字段
}

ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

User user = mapper.readValue(json, User.class);
// ✅ 不报错，email 被忽略，user.name = "张三"，user.age = 25
```
4. collectors.stream().map(DataCollector::getType).toList() 中DataCollector::getType 。Lambda 表达式的简写形式，等价于c -> c.getType()。调用c元素的getType()方法.
  ```java
    // 整段代码等价于
    List<String> result = new ArrayList<>();
    for (DataCollector collector : collectors) {
        String type = collector.getType();   // 对每个对象调用 getType()
        result.add(type);                     // 把返回值加入 list
    }
  ```
5. .toList() 方法将流转换为列表。
   ```java
    List<String> types = collectors.stream()
        .map(DataCollector::getType)
        .toList();
   ```
6. 构造只带 id + taskContent 的更新对象
   ```java
    @PostMapping("/saveTaskContent")
    @Operation(summary = "保存任务编排结果")
    public Result<Boolean> saveTaskContent(@Valid @RequestBody EtlTaskEditDto param) {
      // 只更新任务内容字段，避免覆盖其它字段
      EtlTaskEntity update = new EtlTaskEntity(); // 手动new一个空对象
      update.setId(param.getId());
      update.setTaskContent(param.getTaskContent());
      return Result.success(taskService.update(update));
    }
   ```
   setTaskContent只更改taskContent字段

7. StringUtils.hasText()，它同时判断 null、空串、纯空白，更严谨的判断
8. 幂等接口的设计，即多次调用相同参数，结果相同
9. OBJECT_MAPPER.convertValue() 方法可以将一个对象转换为另一个对象，而不需要手动设置每个字段。
  ```java
    // convertValue 是 Jackson ObjectMapper 的一个普通方法
    import com.fasterxml.jackson.databind.ObjectMapper;
    // DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES 是 Jackson 的一个反序列化开关，控制“JSON 里出现了 Java 类没有的字段时，要不要报错”。
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    User user = OBJECT_MAPPER.convertValue(json, User.class);
    // 推荐使用private static final而不是private ObjectMapper mapper = new ObjectMapper()
    // 更推荐定义一个全局 ObjectMapper Bean，所有类注入使用

    @Configuration
    public class JacksonConfig {

        @Bean
        public ObjectMapper objectMapper() {
            return JsonMapper.builder()
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .build();
        }
    }

    // 业务代码
    @Component
    public class ElasticsearchOutputSink implements OutputSink {

        @Autowired
        private ObjectMapper objectMapper; // 全局同一个
    }
  ```
9. Java Bean 属性访问写法
    ```java
      // properties.getDatasourceId() 是典型的 Java Bean 属性访问写法，在 Spring Boot 里通常用来读取配置文件中绑定的配置项。
      @Data
      @ConfigurationProperties(prefix = "dip.lakehouse.attachment")
      public class AttachmentProperties {
          private String datasourceId;   // 对应配置项 datasource-id
      }
      // Lombok 的 @Data 会自动生成 getDatasourceId() 和 setDatasourceId()。
    ```
    在application.yml中配置datasource-id
    ```yml
      dip:
        lakehouse:
          attachment:
            datasource-id: minio-107
    ```
    Spring 启动时自动绑定,Spring 会读取 dip.lakehouse.attachment.datasource-id，调用 setDatasourceId("minio-107") 把值塞进对象。
10. 查询数据库
    ```java
      dataSourceMapper.selectList(
        new LambdaQueryWrapper<DataSourceDO>()
            .eq(DataSourceDO::getAdapterType, 'minio'));
    // 等价于
    SELECT * FROM dip_system_datasource WHERE adapter_type = 'minio';
    ```
    dataSourceMapper：MyBatis-Plus 的 Mapper，操作 DataSourceDO 对应的表
    selectList(...)：查列表
    LambdaQueryWrapper：用 lambda 方法引用来写条件，避免手写字段名。
11. a::b写法
    | 写法 | 含义 | 等价lambda |
    |--|--|--|
    | 类名::静态方法	| 引用静态方法 |	Math::abs → x -> Math.abs(x)|
    | 类名::实例方法	| 引用实例方法，第一个参数作为调用者 |	DataSourceDO::getAdapterType → ds -> ds.getAdapterType() |
    | 对象::实例方法	| 引用某个具体对象的方法 |	System.out::println → x -> System.out.println(x) |
    | 类名::new	| 引用构造器 |	ArrayList::new → () -> new ArrayList<>()|
    
    
  什么是类名？
  类名就是你在代码里给“类”起的名字，
  ```java
    public class DataSourceDO {
      private String host;
      private Integer port;

      public String getHost() {
          return host;
      }
    }
  ```
  这里的 DataSourceDO 就是类名。
  类名的作用：
  用来声明变量类型：DataSourceDO ds;
  用来创建对象：new DataSourceDO()
  用来调用静态方法/静态字段：DataSourceDO.staticMethod()
  用来做方法引用：DataSourceDO::getHost
  **类名代表的是“类”本身，不是具体的某个数据。**

  什么是对象？
  对象是根据类创建出来的具体实例。类只是模板，对象才是真正有数据、占内存的实体。平常口语中的**实例**说的就是对象。
  DataSourceDO ds = new DataSourceDO();
  new DataSourceDO()：根据 DataSourceDO 这个类，创建一个对象。
  ds：是一个引用变量，指向这个对象。
  这个对象有自己独立的 host、port 等字段值。

# Service + Impl vs 适配器：关键对比表

## 一、核心对比表

| 对比维度          | Service + Impl                     | 适配器（AdapterRegistry）             |
| ------------- | ---------------------------------- | -------------------------------- |
| **注册者**       | Spring 容器自动扫描                      | 自定义 `AdapterRegistry` 手动/SPI 注册  |
| **触发注册的标记**   | `@Service`、`@Component` 等注解        | `META-INF/services/` SPI 文件      |
| **注册 key**    | **类型（Class 对象）**                   | **字符串 type（"mysql"）**            |
| **注册 value**  | Bean 实例                            | Class 对象（延迟到用时才实例化）              |
| **注册时机**      | 应用启动时自动完成                          | 应用启动时 SPI 扫描完成                   |
| **查找依据**      | **类型匹配**（`@Autowired UserService`） | **字符串查表**（`getAdapter("mysql")`） |
| **查找时机**      | **编译期**就确定要哪个类型                    | **运行期**才能确定 type 的值              |
| **决策者**       | Spring 容器                          | `AdapterRegistry.getAdapter()`   |
| **匹配失败报错**    | `NoSuchBeanDefinitionException`    | `IllegalArgumentException`       |
| **多实现冲突**     | `@Primary` / `@Qualifier` 解决       | 用不同 type 字符串区分，天然不冲突             |
| **实例化方式**     | Spring 反射创建，可带依赖注入                 | `clazz.newInstance()`，无参构造       |
| **实例缓存**      | 默认单例，容器级缓存                         | `adapterInstances` Map 缓存        |
| **方法调用绑定**    | JVM 虚方法表 + 动态分派                    | JVM 虚方法表 + 动态分派（**相同**）          |
| **依赖 Spring** | ✅ 强依赖                              | ❌ 不依赖（纯 Java + SPI）              |
| **扩展方式**      | 加 `@Service` 类即可                   | 加实现类 + SPI 文件注册                  |
| **外部 jar 支持** | 需在 Spring 扫描路径内                    | ✅ 支持外挂 jar（放 SPI 文件即可）           |

***

## 二、按"维度分类"的对比

### 1. 注册阶段

| 对比项     | Service + Impl   | 适配器                        |
| ------- | ---------------- | -------------------------- |
| 谁负责注册   | Spring 容器        | `AdapterRegistry`          |
| 如何发现    | 扫描 `@Service` 注解 | 读取 `META-INF/services/` 文件 |
| 注册什么    | 实例（Bean）         | Class 对象（延迟实例化）            |
| key 是什么 | 类型（接口 + 实现类两种）   | 字符串 type                   |
| 何时注册    | 启动时（一次性）         | 启动时（一次性）                   |

### 2. 查找阶段

| 对比项    | Service + Impl                    | 适配器                          |
| ------ | --------------------------------- | ---------------------------- |
| 谁查找    | Spring（处理 `@Autowired`）           | 调用方（显式调 `getAdapter`）        |
| 依据什么查  | 字段类型                              | type 字符串                     |
| 何时确定   | 编译期（写死在代码里）                       | 运行期（数据驱动）                    |
| 查不到怎么办 | 抛 `NoSuchBeanDefinitionException` | 抛 `IllegalArgumentException` |

### 3. 执行阶段

| 对比项    | Service + Impl | 适配器                       |
| ------ | -------------- | ------------------------- |
| 谁创建实例  | Spring 反射创建    | `AdapterRegistry` 反射创建    |
| 支持构造注入 | ✅ 支持           | ❌ 只能用无参构造                 |
| 缓存机制   | 单例 Bean 缓存     | `adapterInstances` Map 缓存 |
| 方法调用   | 多态 + 虚方法表      | 多态 + 虚方法表（**完全一样**）       |

***

## 三、代码层面直观对比

### Service + Impl

```java
// 1. 接口
public interface UserService {
    User getById(Long id);
}

// 2. 实现类（加注解自动注册）
@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }
}

// 3. 使用（按类型注入）
@RestController
public class UserController {
    @Autowired
    private UserService userService;   // 按 UserService.class 查找
}
```

### 适配器

```java
// 1. 接口
public interface DataSourceAdapter {
    void initialize(ConnectionConfig config);
    ConnectionTestResult testConnection();
}

// 2. 实现类（SPI 文件里注册）
public class MysqlAdapter implements DataSourceAdapter {
    @Override
    public void initialize(ConnectionConfig config) { ... }
}

// 3. SPI 文件 META-INF/services/cn.dip.adapter.spi.DataSourceAdapter
// cn.dip.adapter.mysql.MysqlAdapter
// cn.dip.adapter.dm.DmAdapter

// 4. 使用（按字符串查表）
public void testConnection(String id) {
    DataSourceDO ds = dataSourceMapper.selectById(id);
    String type = ds.getAdapterType();               // "mysql"
    DataSourceAdapter adapter = getAdapter(type);    // 按字符串查表
    adapter.initialize(config);
}
```

***

## 四、本质区别（一句话版）

| 对比                 | 本质                                        |
| ------------------ | ----------------------------------------- |
| **Service + Impl** | **静态绑定**：编译期按类型确定，Spring 容器负责注入           |
| **适配器**            | **动态绑定**：运行期按字符串查表，`AdapterRegistry` 负责派发 |

***

## 五、为什么会有这种差异

| 原因                | Service + Impl         | 适配器                |
| ----------------- | ---------------------- | ------------------ |
| **调用方是否知道用哪个**    | 写代码时就知道（`UserService`） | 运行时才知道（`type` 是变量） |
| **是否需要外挂扩展**      | 一般不需要                  | 需要（支持插件 jar）       |
| **是否需要脱离 Spring** | 不需要                    | 需要（SPI 更通用）        |
| **实现类数量**         | 通常 1 个                 | 十几个，还持续增加          |

**一句话：Service 是"我要 UserService"；适配器是"用户选了什么，我就给什么"。**

***

***

## 八、一句话总结

> - **Service + Impl**：按**类型**注册、按**类型**注入，**编译期**确定，Spring 容器负责。
> - **适配器**：按**字符串**注册、按**字符串**查找，**运行期**确定，`AdapterRegistry` 负责。
> - **相同点**：都面向接口、都靠多态、都反射创建、都缓存实例。
> - **本质区别**：一个是**静态类型绑定**，一个是**动态字符串派发**。

