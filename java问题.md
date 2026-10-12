# 问题

1. 使用构造方法初始化对象和使用get、set方法初始化对象的区别
2. 多态中，什么是变量调用编译看左边，运行看左边。方法调用编译看左边，运行看右边。
3. 什么时候用匿名类，匿名类有什么作用？匿名类和lambda表达式有什么区别？
4. 什么是事务和状态机？事务和状态机有什么区别？
   1. 状态机 state machine
  ```java
  // 状态机,处理那些状态多、流转规则复杂、需要严格控制“什么情况能做什么”的业务场景。其核心思想是把“对象的行为”和“对象的状态”解耦，让代码在状态流转时自动表现不同行为，而不是用一堆冗长的 if-else 去判断
  package com.example.simple;


  // 电灯状态枚举
  // 只有两个状态：开、关

  public enum LightState {
    OFF,   // 关
    ON     // 开
  }
```
   2. 事务 transaction
   ```text
     * 事务就是"一组操作，要么全部成功，要么全部失败，不能只成功一半"
      最经典的例子：银行转账
        例如：用户A向用户B转账100元，这是一组操作，要么全部成功，要么全部失败，不能只成功一半
        事务可以确保这组操作的原子性，即要么都成功，要么都失败
        事务的四大特性（ACID）
        1. 原子性（Atomicity）：事务中的操作要么全部成功，要么全部失败
        2. 一致性（Consistency）：事务开始前和事务结束后的数据库状态必须保持一致
        3. 隔离性（Isolation）：多个事务并发执行时，每个事务的执行操作，不会被其他事务的执行操作所干扰
        4. 持久性（Durability）：事务执行完成后，数据库状态必须持久化存储
   ```



# 坑

## 一、SQL 和数据库相关

### 1. 依赖默认返回顺序
- **坑**：`selectList` 不加 `ORDER BY`，以为顺序稳定。
- **避免**：需要顺序就显式 `ORDER BY`，排序字段最好包含主键。

### 2. 分页不带排序
- **坑**：`LIMIT 10 OFFSET 20` 不带 `ORDER BY`，翻页时数据可能重复或漏。
- **避免**：分页查询必须带稳定排序。

### 3. 大小写敏感
- **坑**：`adapter_type = 'minio'` 匹配不到 `'MINIO'`。
- **避免**：统一存储值大小写，或用 `LOWER()`、`UPPER()`。

### 4. NULL 比较
- **坑**：`WHERE host != 'x'` 会自动排除 `host IS NULL` 的行。
- **避免**：需要包含 NULL 时写 `WHERE host IS NULL OR host != 'x'`。

### 5. 隐式类型转换
- **坑**：`WHERE name = 1`（name 是字符串），可能导致全表扫描或索引失效。
- **避免**：值类型和字段类型保持一致。

### 6. 反引号、双引号混用
- **坑**：`SELECT "table"` 在 MySQL 里是字符串常量，不是表名。
- **避免**：MySQL/Doris 用反引号或不用引号；PostgreSQL 用双引号。

### 7. 先查后写不是原子的
- **坑**：`selectOne` 判断不存在，再 `insert`，并发下会重复插入。
- **避免**：靠唯一索引兜底，或加分布式锁。

### 8. Doris 特有
- 主键表 `UNIQUE KEY` 一张表只能一组，自然键唯一性要应用层保证。
- `DEFAULT FALSE` 要写 `DEFAULT 'false'`。
- 分布列必须是键列子集。
- 单机环境 `replication_num` 要改 `1`。

---

## 二、MyBatis-Plus 相关

### 1. `selectOne` 多条会抛异常
- **坑**：没加 `LIMIT 1`，查到多条直接报错。
- **避免**：确定唯一才用 `selectOne`，否则用 `selectList().stream().findFirst()` 并加 `LIMIT 1`。

### 2. `selectList` 返回空列表不是 null
- **坑**：以为可能为 null，做了多余的 null 判断；或者反过来没判空直接 `.get(0)`。
- **避免**：默认当空列表处理，取元素前判空。

### 3. `LambdaQueryWrapper` 字段名写错
- **坑**：`DataSourceDO::getHost` 写成 `getHosts`，编译不过还好，但逻辑写错难发现。
- **避免**：依赖 IDE 提示，别手写字段名。

### 4. 逻辑删除和物理删除混淆
- **坑**：项目配了逻辑删除，`delete` 变成 `UPDATE deleted=1`，但你以为真删了。
- **避免**：确认项目是否启用逻辑删除，查 `@TableLogic` 注解。

### 5. 字段自动填充
- **坑**：`created_at`、`updated_at` 交给 `MetaObjectHandler` 自动填，手动 set 可能被覆盖。
- **避免**：了解项目的自动填充规则，别重复设置。

### 6. 多数据源 `@DS`
- **坑**：忘记加 `@DS`，走了默认数据源，查错库。
- **避免**：多数据源项目里，每个 Mapper 明确标注数据源。
- **注意**：`@DS` 和 `@Transactional` 同时用可能失效，`@DS` 要在事务开启前生效。

---

## 三、Java 集合和 Stream

### 1. List 没有 filter
- **坑**：`list.filter(...)` 编译不过。
- **避免**：`list.stream().filter(...)` 或 for 循环。

### 2. Stream 只能用一次
- **坑**：一个 Stream 调了 `filter` 又调 `count`，第二次操作报 `IllegalStateException`。
- **避免**：Stream 用完即弃，需要多次操作就重新 `stream()`。

### 3. `collect` 前 Stream 是惰性的
- **坑**：只写 `list.stream().filter(...)` 不 collect，什么都不会执行。
- **避免**：终端操作（`collect`、`forEach`、`count`）才会触发。

### 4. `findFirst` vs `findAny`
- **坑**：并行流里 `findFirst` 有顺序开销，`findAny` 更快但结果不确定。
- **避免**：需要确定第一条用 `findFirst`，并行且不关心哪条用 `findAny`。

### 5. `Collectors.toMap` 键冲突
- **坑**：`toMap` 遇到重复 key 直接抛 `IllegalStateException`。
- **避免**：用三参数版本指定合并策略。

### 6. `Arrays.asList` 不可变
- **坑**：`Arrays.asList(...)` 返回固定大小列表，`add` 会抛异常。
- **避免**：需要可变用 `new ArrayList<>(Arrays.asList(...))`。

### 7. 遍历时修改集合
- **坑**：for-each 里 `list.remove()` 抛 `ConcurrentModificationException`。
- **避免**：用 `Iterator.remove()` 或 `removeIf`。

---

## 四、对象存储和分布式存储

### 1. endpoint 和 console 端口混用
- **坑**：MinIO 的 9000 是 S3 API，9001 是控制台，填错连不上。
- **避免**：程序连接一律用 9000。

### 2. path-style vs virtual-hosted
- **坑**：MinIO 默认只支持 path-style，Doris 默认 virtual-hosted，导致 403。
- **避免**：MinIO 必须加 `use_path_style = true`。

### 3. endpoint 写 localhost
- **坑**：容器里写 `localhost` 指容器自己，不是宿主机。
- **避免**：跨容器用宿主机 IP 或 service 名。

### 4. bucket 不存在
- **坑**：以为创建 Catalog 会自动建 bucket。
- **避免**：提前在 MinIO 控制台建好 bucket。

### 5. AK/SK 硬编码
- **坑**：把密钥写死在代码或配置文件里。
- **避免**：用环境变量、密钥管理服务，或 vended credentials。

### 6. 时间偏差导致签名失败
- **坑**：服务器时间偏差超过 15 分钟，S3 签名校验失败返回 403。
- **避免**：所有节点用 NTP/chrony 同步时间。

### 7. 路径不一致导致 NoSuchKey
- **坑**：元数据里记录的路径和实际存储路径不一致。
- **避免**：统一 warehouse 配置，建表时确认 location。

---

## 五、Spring Boot 和配置

### 1. 配置项写错前缀
- **坑**：`dip.lakehouse.attachment.datasource-id` 写成 `datasourceId` 或 `datasource_id`。
- **避免**：Spring Boot 的 relaxed binding 支持 `datasource-id`、`datasourceId`、`DATASOURCE_ID`，但**不支持下划线混用**，以实际配置为准。

### 2. `@ConfigurationProperties` 没生效
- **坑**：忘了加 `@EnableConfigurationProperties` 或 `@Component`。
- **避免**：确认配置类被 Spring 扫描到。

### 3. 默认数据源和 `@DS` 冲突
- **坑**：不写 `@DS` 走 primary，写了走指定数据源，搞混。
- **避免**：明确项目的数据源约定。

### 4. 配置文件优先级
- **坑**：`application.yml` 和 `application-dev.yml` 同时存在，dev 覆盖了主配置。
- **避免**：了解 profile 优先级，用 `--spring.profiles.active=dev` 明确指定。

### 5. 事务和切面顺序
- **坑**：`@DS` 和 `@Transactional` 顺序不对，切库失效。
- **避免**：`@DS` 加在 Service 方法上，事务外层不切库。

---

## 六、代码习惯

### 1. 硬编码
- **坑**：把 ID、路径、密钥写死在代码里。
- **避免**：走配置或参数。

### 2. 忽略方法参数
- **坑**：方法签名有 `datasourceId`，方法体里写死别的值。
- **避免**：参数要用起来，别让签名和实现脱节。

### 3. 异常信息不清晰
- **坑**：抛 `"未找到数据源"`，实际是“有但字段为空”。
- **避免**：异常信息带上上下文，比如候选数量、具体哪个字段为空。

### 4. 没有日志
- **坑**：关键分支没日志，线上排查全靠猜。
- **避免**：重要决策点打日志，比如选了哪个数据源、走了哪个分支。

### 5. `SELECT *`
- **坑**：查全字段，包括密码等敏感信息。
- **避免**：只查需要的列。

### 6. 空指针
- **坑**：`ds.getHost().isBlank()` 在 host 为 null 时 NPE。
- **避免**：先判 null 再调方法，或用 `StringUtils.isBlank()`。

---

## 七、排查问题的方法论

### 1. 先看原始响应
- **坑**：只看封装后的错误信息，不看原始返回。
- **避免**：用 `curl -i` 看 HTTP 状态码和响应体。

### 2. 先看日志
- **坑**：凭猜测改配置。
- **避免**：BE 日志、应用日志里有完整错误链，先读日志。

### 3. 分层排查
- **坑**：网络、认证、权限、配置混在一起猜。
- **避免**：按“网络通不通 → 认证对不对 → 权限够不够 → 配置对不对”逐层排除。

### 4. 对比法
- **坑**：不知道哪里不对。
- **避免**：拿一个能用的和不能用的对比，差异点就是问题。

### 5. 最小复现
- **坑**：在复杂环境里瞎试。
- **避免**：用 curl、mc、mysql 客户端等最小工具单独验证每一层。

---

## 八、新人最容易忽略的几条

1. **没有 `ORDER BY` 的顺序不可靠**。
2. **先查后写不是原子操作**，并发下会重复。
3. **对象存储的端口和路径要分清**（9000 vs 9001，path-style）。
4. **多数据源项目里 `@DS` 不能忘**。
5. **时间不同步会导致签名失败**。
6. **异常信息要能自解释**，别让人猜。
7. **日志要打关键决策点**。
8. **硬编码迟早会出问题**。

---

## 建议的成长路径

- **遇到问题先读日志**，别急着改代码。
- **每踩一个坑，记一条笔记**，形成自己的 checklist。
- **多看项目的异常处理、日志、配置约定**，跟着规范走。
- **不确定就问**，但问之前先自己排查一遍，带着日志和现象去问。
- **写完代码自测**，尤其是边界情况（空、null、重复、并发）。

这些坑不是一次能全避开的，但每类都了解一点，遇到时就能更快定位。你前面遇到的 Doris 403、NoSuchKey、时间同步、`selectList` 顺序，都是很典型的例子，已经覆盖了数据库、对象存储、网络、代码习惯几个大类。后续再遇到新问题，可以对照这份清单逐项排查。
