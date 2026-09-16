### 快捷键

1. shift + alt + o 导入类排序
2. ctrl + t 查询接口方法(必须启动项目)
3. ctrl+shift+o 查看当前文件的大纲
4. alt+方向键左右 历史前进后退
5. ctrl + f12 查看方法定义

### 设计

1. 在java中，一个单独的java文件是无法通过java xxx.java运行的，需要先编译，再运行。通过javac xxx.java编译，生成一个xxx.class文件，这个class文件称为字节码文件，再通过java xxx运行。
2. 一个 Spring Boot 服务从命令到可访问，大致经历这些阶段：Maven 阶段、JVM 阶段、Spring 容器阶段、Web 服务器阶段、外部资源连接阶段、业务初始化阶段

### 基础概览

1. SpringBootApplication标注在某个类，运行这个类的main方法来启动SpringBoot应用
2. 配置文件放在 src/main/resources目录application.yml文件
3. 实体类: 通常位于 entity 或 model 包下,User.java, System.java.用于存储数据库中的数据的类，通常对应数据库中的一个表。
4. 后端项目则先找 Spring Boot 启动类。当前启动类是 FullstackMallApplication.java，它所在包名是 com.example.fullstackmall.service。Spring Boot 默认会从启动类所在包向下扫描组件，因此 Controller、Facade、Mapper、Config 等类如果放在这个包的子包下，就更容易被自动发现。你以后新增后端类时，也要有“包扫描范围”的意识，不要随便放到完全无关的包路径里。

### 配置类

```java
@Configuration: 用于标识一个类为配置类，该类主要用于定义和组织 Spring 应用程序的配置信息
@Override :关键且常用的‌标记型注解（Marker Annotation）‌。它的主要作用是显式地声明某个方法旨在‌重写（Override）‌父类或接口中的方法
@Service：业务层组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
@Component：通用组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
```

## springboot

### 层级结构

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
│   │   └── vo      // 视图对象
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

#### domain

domain层，文件夹名称不固定（domain，entity‌，model‌），可能文件夹随便取，文件名叫XXXDO.java。文件中通常使用@Data等Lombok注解
存放实体类，与数据库中的属性保持一致，存放属性和操作属性的get，set方法

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

#### service层

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

#### controller层

给前端提供接口的类，主要负责接收前端的请求，调用service层的方法，返回结果。\
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

// 如何查看后端接收的接口参数
  /**
   * 接收路径中的 ID
   * URL: /api/user/1001
   */
  @GetMapping("/{id}")
  public String getUserById(@PathVariable("id") Long userId) {
      return "正在查询用户ID: " + userId;
  }
  /**
   * 接收查询参数
   * URL: /api/search/list?keyword=Java&page=1
   */
  @GetMapping("/list")
  public String searchList(
          @RequestParam(value = "keyword", required = true) String keyword,
          @RequestParam(value = "page", defaultValue = "1") Integer page) {
      
      return "搜索关键词: " + keyword + ", 当前页: " + page;
  }

  // 接收 JSON 请求体参数

  // 定义实体类UserDTO.java
  @Data // Lombok 注解，自动生成 getter/setter
  public class UserDTO {
    private String name;
    private Integer age;
  }
  
  @PostMapping("/create")
  public String createUser(@RequestBody UserDTO user) {
    return "正在创建用户: " + user;
  }

  /**
   * 如果不想定义实体类，也可以用 Map 接收 (不推荐复杂场景使用)
   */
  @PostMapping("/add/map")
  public String addUserMap(@RequestBody Map<String, Object> params) {
    return "收到参数: " + params.toString();
  }
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

### Mybatis拦截器

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

### 概念

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
   ‌Bean‌ 是由 ‌Spring IoC 容器‌（控制反转容器）管理的一个对象。在传统的 Java 开发中，对象通常由开发者通过 new 关键字手动创建；而在 Spring Boot 中，对象的创建、初始化、销毁等生命周期全部交给 Spring 容器统一管理。这些被容器管理的对象就称为 ‌Bean‌。类似前端的vuex，一个地方定义，后续在其他组件中使用定义都是同一个实例，保证了状态一致和共享
   使用@Autowired注解，自动注入依赖。类似前端的vuex引入store和方法。
5. maven项目
6. pom.xml可解读java版本，项目几个模块（modules）
7. Lombok框架
   1. Lombok是一个Java库，它消除了重复的代码，使开发人员能够更快速地编写代码。它通过注解的方式自 动为 Java 类生成常用的样板代码（Boilerplate Code），从而极大地简化了 Java 开发过程.
      常用注解示例:
      @Data‌：最常用的注解，等同于同时使用 @Getter、@Setter、@RequiredArgsConstructor、@ToString 和 @EqualsAndHashCode。适用于大多数数据载体类（如 Entity、DTO）。
      ‌@Getter / @Setter‌：仅为指定字段生成 getter 或 setter 方法。
      ‌@NoArgsConstructor / @AllArgsConstructor‌：生成无参构造器或全参构造器。
      ‌@Builder‌：提供构建者模式（Builder Pattern）的支持，方便链式调用创建对象，特别适用于字段较多的类。
      ‌@Slf4j‌：自动生成一个名为 log 的 SLF4J 日志对象，方便直接在类中进行日志记录。
      ‌@NonNull‌：在方法参数或字段上使用，自动生成空值检查代码，若传入 null 则抛出

## java基础

### 设计

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
Spring 容器启动时扫描所有 @Service / @Component 类，将扫描到的类，准备注册成一个 Bean
把 Bean 注册进容器，key 是"类型", 注册时用的 key 不是一个字符串名，而是一个类型信息

1. Class\<? extends DataSourceAdapter> clazz = adapterClasses.get(type)
   1. 反射实例化：根据运行时的信息动态决定 new 哪个类
   2. 反射是如何拿到实例和加载对应的实现方法？反射后就拿到了对应的实例，然后就可以调用实例的方法

### 集合类

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

### 数组

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

### 构造方法

1. 方法名与类名相同，大小写一致
2. 没有返回值
3. 可以有多个构造方法
4. 默认构造方法
5. 有参构造方法
6. 无参构造方法
7. 构造方法可以调用其他构造方法

#### 构造方法调用

1. 每次new一个对象，就会调用一个构造方法，不需要手动调用
2. 构造方法可以调用其他构造方法

### 静态方法

1. 方法名前加static关键字
2. 静态方法只能调用静态方法和静态变量，不能调用非静态变量、方法。
3. 非静态方法可以调用静态方法和静态变量
4. 静态方法没有this关键字
   静态随着类的加载而加载（加载静态的时候可能都没有非静态变量和方法），非静态随着对象的创建而加载

### 继承

1. 输入本类的变量使用this.变量名
2. 输入父类的变量使用super.变量名
3. override重写父类的方法,需要重写的方法名和参数列表与父类的方法名和参数列表相同

### 多态

```java
Fu z = new Zi();
// 这里的Fu就是多态形式，z只是一个变量，这里调用的是Fu类的方法
Zi z = new Zi();
// 这里的Zi就是多态形式，z只是一个变量，这里调用的是Zi类的方法
// 多态可以实现不同的对象调用不同的方法
```

1. 为什么需要多态，多态能解决什么问题？
2. 什么是类型转换，类型转换解决了什么问题？

### 接口(implements)

1. 接口就是一个规则，独立于继承体系之外
2. 接口是一种特殊的类，它没有方法体，只有方法的声明
3. 接口可以实现多态，因为接口是一个抽象类，所以可以被实现类实现

### 内部类

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

### 后端概念

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

### 问题

1. 使用构造方法初始化对象和使用get、set方法初始化对象的区别
2. 多态中，什么是变量调用编译看左边，运行看左边。方法调用编译看左边，运行看右边。
3. 什么时候用匿名类，匿名类有什么作用？匿名类和lambda表达式有什么区别？
4. 什么是事务和状态机

### 开发中注意事项

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

1. Jackson 是 Java 生态里最主流的 JSON 处理库，负责在 Java 对象和 JSON 字符串之间来回转换。

前端发送:  {"name": "张三", "age": 25}
后端接收:  转成 Java 对象 User(name="张三", age=25)
后端响应:  Java 对象转成 JSON 返回
最常用的两个类: ObjectMapper(主入口，JSON 与对象互转), TypeReference(携带泛型信息，解决类型擦除)

### 写法

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
   1. 反射实例化

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

