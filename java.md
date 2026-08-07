
### 快捷键

1. shift + alt + o 导入类排序
2. ctrl + t 查询接口方法
3. ctrl+shift+o 查看当前文件的大纲

### 设计

在java中，一个单独的java文件是无法通过java xxx.java运行的，需要先编译，再运行。通过javac xxx.java编译，生成一个xxx.class文件，这个class文件称为字节码文件，再通过java xxx运行。

### 基础概览

1. SpringBootApplication标注在某个类，运行这个类的main方法来启动SpringBoot应用
2. 配置文件放在 src/main/resources目录application.yml文件
3. 实体类: 通常位于 entity 或 model 包下,User.java, System.java.用于存储数据库中的数据的类，通常对应数据库中的一个表。

### 配置类

```java
@Configuration: 用于标识一个类为配置类，该类主要用于定义和组织 Spring 应用程序的配置信息
@Override :关键且常用的‌标记型注解（Marker Annotation）‌。它的主要作用是显式地声明某个方法旨在‌重写（Override）‌父类或接口中的方法
@Service：业务层组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
@Component：通用组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
```

## springboot

### 层级结构

domain层存放实体类，于数据库中的属性保持一致，存放属性和操作属性的get，set方法

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

mapper层针对数据库进行操作，主要实现增删改查等操作。于mybatis中方法一一映射。mapper接口中定义的方法，会自动被mybatis-plus实现。mybatis-plus有Service Interface，用于定义业务逻辑。Mapper Interface，用于定义数据库操作。

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus 已提供基础 CRUD，复杂 SQL 可在此定义
    User selectById(Long id);
}
```

service层给controller层的类提供接口,仅包含方法声明，不涉及具体实现逻辑.
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

controller是给前端提交接口

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
      @TableField(value = "create_time", fill = FieldFill.INSERT)：在插入时自动填充字段值
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")：在返回json数据时，格式化时间字段
4. bean
  ‌Bean‌ 是由 ‌Spring IoC 容器‌（控制反转容器）管理的一个对象。在传统的 Java 开发中，对象通常由开发者通过 new 关键字手动创建；而在 Spring Boot 中，对象的创建、初始化、销毁等生命周期全部交给 Spring 容器统一管理。这些被容器管理的对象就称为 ‌Bean‌。
  使用@Autowired注解，自动注入依赖。
5. maven项目
6. Lombok框架
    1. Lombok是一个Java库，它消除了重复的代码，使开发人员能够更快速地编写代码。它通过注解的方式自 动为 Java 类生成常用的样板代码（Boilerplate Code），从而极大地简化了 Java 开发过程.
    常用注解示例:
    @Data‌：最常用的注解，等同于同时使用 @Getter、@Setter、@RequiredArgsConstructor、@ToString 和 @EqualsAndHashCode。适用于大多数数据载体类（如 Entity、DTO）。
    ‌@Getter / @Setter‌：仅为指定字段生成 getter 或 setter 方法。
    ‌@NoArgsConstructor / @AllArgsConstructor‌：生成无参构造器或全参构造器。
    ‌@Builder‌：提供构建者模式（Builder Pattern）的支持，方便链式调用创建对象，特别适用于字段较多的类。
    ‌@Slf4j‌：自动生成一个名为 log 的 SLF4J 日志对象，方便直接在类中进行日志记录。
    ‌@NonNull‌：在方法参数或字段上使用，自动生成空值检查代码，若传入 null 则抛出

## java基础

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

2. Map 系列（键值对 Key-Value）

```java
1. HashMap‌,最常用，无序，允许 Key 和 Value 为 null，性能高。
Map<String, Integer> scoreMap = new HashMap<>();

2. TreeMap‌,Key 会按照自然顺序或自定义规则‌排序‌
TreeMap<String, String> treeMap = new TreeMap<>();

3. LinkedHashMap‌,保持 Key 的‌插入顺序‌，常用于实现 LRU 缓存。
LinkedHashMap<String, String> lhm = new LinkedHashMap<>();
```

3. Queue 系列（队列）

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
1. 接口是一种特殊的类，它没有方法体，只有方法的声明
2. 接口可以实现多态，因为接口是一个抽象类，所以可以被实现类实现

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

### 问题

1. 使用构造方法初始化对象和使用get、set方法初始化对象的区别
2. 多态中，什么是变量调用编译看左边，运行看左边。方法调用编译看左边，运行看右边。
3. 什么时候用匿名类，匿名类有什么作用？匿名类和lambda表达式有什么区别？
