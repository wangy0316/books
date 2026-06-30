### 项目构成：
1.mysql
2.redis
3.api接口
4.鉴权
5.统一接口信息
6.流文件
7.

### 基础概览

SpringBootApplication标注在某个类，运行这个类的main方法来启动SpringBoot应用
配置文件放在 src/main/resources目录application.yml文件

### 配置类

```
@Configuration: 用于标识一个类为配置类，该类主要用于定义和组织 Spring 应用程序的配置信息
@Override :关键且常用的‌标记型注解（Marker Annotation）‌。它的主要作用是显式地声明某个方法旨在‌重写（Override）‌父类或接口中的方法
@Service：业务层组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
@Component：通用组件注解.用于标识组件的注解，核心作用都是让Spring容器扫描到被标注的类
```

### 层级结构

domain层存放实体类，于数据库中的属性保持一致，存放属性和操作属性的get，set方法
```
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
mapper层针对数据库进行操作，主要实现增删改查等操作。于mybatis中方法一一映射
```
public interface userMapper {
    int insert(user record)
}
```
service层给controller层的类提供接口,仅包含方法声明，不涉及具体实现逻辑.
service文件夹的接口定义了业务逻辑的"做什么"，impl文件夹的实现类负责"怎么做"
service层中还有一个impl层，impl文件夹主要用于存放接口的具体实现类，是代码分层设计里的重要组成部分。
```

```

controller是给前端提交接口
```

```

### Mybatis拦截器
mybatis-config.xml中引入拦截器

```
@Intercepts：标识该类是一个拦截器
@Signature：指明自定义拦截器需要拦截哪一个类型，哪一个方法；
    type：对应四种类型中的一种（Executor、StatementHandler、ParameterHandler、ResultSetHandler）；
    method：对应接口中的哪个方法；
    args：对应哪一个方法参数类型（因为可能存在重载方法）

@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})

```
固定写法
```
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

