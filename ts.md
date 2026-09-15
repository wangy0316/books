## TypeScript 术语

1.数组和元组在 TS 里是两个不同的概念，不是同一个东西的两种叫法
```typescript
// 数组：长度不定，所有元素同类型
let arr: number[] = [1, 2, 3];        
// 元组：长度固定，每个位置类型可以不同
let tuple: [number, string] = [1, "a"]; 
```

## type 类型

```typescript
// 联合类型
type Status = "idle" | "loading" | "success" | "error";

// 元组
type Point = [number, number];

// 原始类型别名
type ID = string | number;

// 函数类型（interface 也能写，但 type 更常见）
type Handler = (e: Event) => void;

// 映射类型
type Partial<T> = { [K in keyof T]?: T[K] };

// 条件类型
type NonNullable<T> = T extends null | undefined ? never : T;

// 模板字面量类型
type EventName = `on${Capitalize<string>}`;
```

## interface 类型

描述对象/函数等

```typescript
// 定义一个接口
interface IUser {
  name: string;
  age: number;
}
```


## 类型别名


# ts常用概念简介

TypeScript 里常用且容易混淆的概念，按“日常写业务/写库都会遇到”的程度梳理一遍。

## 一、类型操作工具

### 1. 泛型（Generics）

```ts
function identity<T>(x: T): T { return x; }

// 约束
function getLen<T extends { length: number }>(x: T) { return x.length; }

// 默认值
type Box<T = string> = { value: T };
```

### 2. 内置工具类型（Utility Types）

这些几乎天天用：

```ts
interface User { id: number; name: string; email: string }

Partial<User>        // 全部变可选
Required<User>       // 全部变必填
Readonly<User>       // 全部只读
Pick<User, "id">     // 挑出部分属性
Omit<User, "email">  // 排除部分属性
Record<string, User> // 构造键值对类型
Exclude<T, U>        // 联合类型中排除
Extract<T, U>        // 联合类型中提取
NonNullable<T>       // 排除 null/undefined
ReturnType<F>        // 函数返回值类型
Parameters<F>        // 函数参数元组
Awaited<T>           // 解包 Promise
```

`Partial`、`Pick`、`Omit`、`Record`、`ReturnType` 是最高频的。

### 3. 键操作：`keyof`、`typeof`、索引访问

```ts
interface User { id: number; name: string }

type K = keyof User;            // "id" | "name"
type V = User["id"];            // number
type V2 = User[keyof User];     // number | string

const config = { host: "x", port: 8080 };
type Config = typeof config;    // 从值反推类型
```

`as const` 配合 `typeof` 很常用：

```ts
const routes = ["home", "about"] as const;
type Route = typeof routes[number]; // "home" | "about"
```

### 4. 映射类型（Mapped Types）

工具类型的底层原理：

```ts
type MyPartial<T> = { [K in keyof T]?: T[K] };

// 用 as 重映射键
type Getters<T> = {
  [K in keyof T as `get${Capitalize<string & K>}`]: () => T[K]
};
```

### 5. 条件类型 + `infer`

```ts
type IsString<T> = T extends string ? true : false;

// infer 提取类型
type ElementType<T> = T extends (infer U)[] ? U : never;
type FuncReturn<T> = T extends (...args: any[]) => infer R ? R : never;
```

## 二、类型系统概念

### 6. 联合与交叉

```ts
type A = { a: 1 };
type B = { b: 2 };
type C = A | B; // 联合：满足其一
type D = A & B; // 交叉：同时满足
```

### 7. 可辨识联合（Discriminated Union）

TS 里最重要的建模手段之一：

```ts
type Result =
  | { status: "ok"; data: string }
  | { status: "error"; message: string };

function handle(r: Result) {
  if (r.status === "ok") r.data;   // 自动收窄
  else r.message;
}
```

### 8. 类型守卫与断言函数

```ts
function isString(x: unknown): x is string {
  return typeof x === "string";
}

function assertString(x: unknown): asserts x is string {
  if (typeof x !== "string") throw new Error("not string");
}
```

### 9. 字面量类型与模板字面量类型

```ts
type Dir = "left" | "right";
type Event = `on${"Click" | "Focus"}`; // "onClick" | "onFocus"
type Hex = `#${string}`;
```

### 10. 索引签名与映射到任意键

```ts
interface Dict { [key: string]: number }
type Dict2 = Record<string, number>;

// 更安全的写法
type SafeDict = { [k: string]: number; length: number };
```

## 三、修饰符与断言

### 11. 可选、只读、非空

```ts
interface U {
  name?: string;      // 可选
  readonly id: number; // 只读
}

let s: string | null = null;
s!.length;   // 非空断言（慎用）
s?.length;   // 可选链
```

### 12. 类型断言

```ts
const el = document.getElementById("app") as HTMLDivElement;
const el2 = <HTMLDivElement>document.getElementById("app"); // 另一种写法
```

优先用类型守卫，`as` 是绕过检查，`as unknown as T` 是双重断言（更危险）。

### 13. 类型谓词与 `satisfies`

`satisfies` 是 TS 4.9 引入的高频新特性，既校验类型又保留字面量推断：

```ts
const config = {
  host: "localhost",
  port: 8080,
} satisfies Record<string, string | number>;

// config.host 仍是字面量 "localhost"，而不是 string
```

对比 `: Record<...>` 注解会丢失字面量类型。

## 四、模块与声明

### 14. 模块相关

```ts
import type { User } from "./types";   // 只导入类型，编译后消失
export type { User };                   // 只导出类型

declare module "*.css";                 // 声明模块
declare global { interface Window { x: number } } // 扩展全局
```

`import type` / `export type` 很重要，能避免运行时循环依赖。

### 15. 命名空间与声明文件

```ts
// .d.ts
declare namespace MyLib {
  function init(): void;
}

// 给第三方库补类型
declare module "some-lib" {
  export function foo(): void;
}
```

现代代码更推荐 ES 模块，`namespace` 主要出现在 `.d.ts` 里。

## 五、类型编程进阶

### 16. 递归类型

```ts
type DeepReadonly<T> = {
  readonly [K in keyof T]: T[K] extends object ? DeepReadonly<T[K]> : T[K];
};

type Json =
  | string | number | boolean | null
  | Json[] | { [k: string]: Json };
```

### 17. 分发条件类型

条件类型遇到裸类型参数会自动分发：

```ts
type ToArray<T> = T extends any ? T[] : never;
type R = ToArray<string | number>; // string[] | number[]
```

用 `[T] extends [U]` 可以阻止分发。

### 18. 变型（协变/逆变）

函数参数是逆变的，开启 `strictFunctionTypes` 后：

```ts
type Fn = (x: string | number) => void;
let f: Fn = (x: string) => {}; // ❌ 参数不能更窄
```

`in` / `out` 修饰符（TS 4.7）可显式标注：

```ts
interface Producer<out T> { get(): T }
interface Consumer<in T> { set(x: T): void }
```

## 六、编译配置相关（影响写法）

`tsconfig.json` 里几个关键选项直接决定能用哪些写法：

```jsonc
{
  "strict": true,              // 开启全部严格检查
  "noUncheckedIndexedAccess": true, // 索引访问返回 T | undefined
  "exactOptionalPropertyTypes": true,
  "verbatimModuleSyntax": true,     // 强制 import type
  "moduleResolution": "bundler"
}
```

## 优先级建议

如果按“最该先掌握”排序：

1. **泛型 + 约束**
2. **常用工具类型**：`Partial` / `Pick` / `Omit` / `Record` / `ReturnType`
3. **`keyof` / `typeof` / 索引访问 / `as const`**
4. **可辨识联合 + 类型收窄**
5. **条件类型 + `infer`**
6. **`satisfies`**
7. **映射类型（理解工具类型原理）**

前 4 项覆盖了日常 90% 的场景，后面几项在写库、写复杂类型时才会频繁用到。

# 泛型 + 约束

泛型是 TypeScript 类型系统里最核心的能力：**让类型像参数一样被传入**，从而在保留类型信息的同时复用逻辑。约束（`extends`）则是对传入的类型参数加限制。

## 一、为什么需要泛型

不用泛型时，只能这样：

```ts
function identityString(x: string): string { return x; }
function identityNumber(x: number): number { return x; }
// 或者丢失类型
function identityAny(x: any): any { return x; }
```

用泛型：

```ts
function identity<T>(x: T): T { return x; }

identity("a");  // T 推断为 "a"
identity(1);    // T 推断为 1
```

`T` 是**类型参数**，调用时由实参自动推断，也可显式指定 `identity<string>("a")`。

## 二、泛型出现的位置

```ts
// 1. 函数
function f<T>(x: T): T {}

// 2. 接口
interface Box<T> { value: T }

// 3. 类型别名
type Pair<A, B> = { first: A; second: B };

// 4. 类
class Stack<T> {
  private items: T[] = [];
  push(x: T) { this.items.push(x); }
  pop(): T | undefined { return this.items.pop(); }
}

// 5. 箭头函数（.tsx 里要加逗号）
const f2 = <T,>(x: T): T => x;
```

## 三、约束：`extends`

不加约束时 `T` 可以是任何类型，无法访问任何属性：

```ts
function getLength<T>(x: T) {
  return x.length; // ❌ T 上不存在 length
}
```

加约束后就能安全访问：

```ts
function getLength<T extends { length: number }>(x: T) {
  return x.length; // ✅
}

getLength("abc");     // ✅
getLength([1, 2]);    // ✅
getLength(123);       // ❌ number 没有 length
```

### 用 `keyof` 约束键

```ts
function get<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

const user = { id: 1, name: "a" };
get(user, "name"); // string
get(user, "age");  // ❌ "age" 不在 keyof User 里
```

这是最常见的约束模式之一，返回值 `T[K]` 会自动匹配具体键的类型。

### 约束联合类型

```ts
function create<T extends "a" | "b">(t: T) {}

// 或
type Status = "idle" | "loading" | "done";
function setStatus<T extends Status>(s: T) {}
```

### 约束中引用另一个类型参数

```ts
function merge<T, U extends T>(target: T, source: U): T & U {
  return { ...target, ...source };
}
```

### 多约束（交叉）

```ts
function f<T extends { a: number } & { b: string }>(x: T) {}
```

## 四、默认类型参数

```ts
type Box<T = string> = { value: T };

type A = Box;          // Box<string>
type B = Box<number>;  // Box<number>

// 配合约束
type Container<T extends object = {}> = { data: T };
```

默认值必须满足约束。

## 五、常见进阶模式

### 1. 条件类型 + 约束

```ts
type MessageOf<T> = T extends { message: infer M } ? M : never;

interface Email { message: string }
type M = MessageOf<Email>; // string
```

### 2. 约束配合 `infer`

```ts
function getProp<T, K extends keyof T>(obj: T, key: K) {
  return obj[key];
}
```

### 3. 泛型工具类型

```ts
type MyPick<T, K extends keyof T> = {
  [P in K]: T[P];
};

type MyRecord<K extends keyof any, T> = {
  [P in K]: T;
};
```

`keyof any` 等价于 `string | number | symbol`。

### 4. 约束为构造函数

```ts
function createInstance<T>(Ctor: new () => T): T {
  return new Ctor();
}

class Foo {}
createInstance(Foo); // Foo
```

### 5. 约束为函数类型

```ts
function call<T extends (...args: any[]) => any>(fn: T): ReturnType<T> {
  return fn();
}
```

## 六、泛型推断的细节

### 推断来源

```ts
function pair<A, B>(a: A, b: B): [A, B] { return [a, b]; }

pair(1, "a"); // [number, string]
pair<number, string>(1, "a"); // 显式
```

### 推断失败时用默认值

```ts
function f<T = unknown>() {}
```

### 保留字面量的两种方式

```ts
// 方式一：const 类型参数（TS 5.0+）
function tuple<const T extends readonly unknown[]>(x: T): T { return x; }
const t = tuple([1, "a"]); // readonly [1, "a"]，而不是 (string | number)[]

// 方式二：调用时 as const
```

### 协变位置与逆变位置

```ts
type Fn<T> = (x: T) => void;
// T 在参数位置是逆变，在返回位置是协变
```

## 七、易错点

**1. 泛型参数只出现一次 = 没必要用泛型**

```ts
// ❌ T 只用了一次，等于 any
function f<T>(x: T) { console.log(x); }

// ✅ 改为
function f(x: unknown) { console.log(x); }
```

判断标准：**泛型参数是否在至少两个位置出现，建立了类型关联**。

**2. 约束不等于收窄**

```ts
function f<T extends string | number>(x: T) {
  x.toUpperCase(); // ❌ T 可能是 number
}
```

约束是“上限”，不是“具体类型”。

**3. 箭头函数在 `.tsx` 里**

```tsx
const f = <T>(x: T) => x;   // ❌ 被当成 JSX
const f = <T,>(x: T) => x;  // ✅ 加逗号
```

**4. 泛型默认值不满足约束**

```ts
type A<T extends string = number> = T; // ❌
```

## 八、实战示例

### 类型安全的事件系统

```ts
type EventMap = {
  click: { x: number; y: number };
  keydown: { key: string };
};

function on<K extends keyof EventMap>(
  event: K,
  handler: (payload: EventMap[K]) => void
) {}

on("click", (p) => p.x);    // p 推断为 { x: number; y: number }
on("keydown", (p) => p.key); // p 推断为 { key: string }
```

### 类型安全的 `get`

```ts
function get<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}
```

### API 响应包装

```ts
type ApiResponse<T> = {
  code: number;
  data: T;
  message: string;
};

async function request<T>(url: string): Promise<ApiResponse<T>> {
  return fetch(url).then(r => r.json());
}

const res = await request<{ id: number }>("/api/user");
res.data.id; // number
```

## 小结

| 概念 | 写法 | 作用 |
|------|------|------|
| 类型参数 | `<T>` | 让类型可变 |
| 约束 | `<T extends U>` | 限制 T 的范围 |
| 键约束 | `<K extends keyof T>` | 安全访问属性 |
| 默认值 | `<T = string>` | 不传时用默认 |
| const 参数 | `<const T>` | 保留字面量 |
| 条件 + infer | `T extends X<infer U>` | 提取内部类型 |

核心心法：

1. **泛型参数要在多处出现**，建立类型之间的关联，否则没意义。
2. **约束是上限不是具体类型**，别把它当收窄用。
3. **`K extends keyof T` + `T[K]`** 是最高频的黄金组合。
4. **工具类型的底层就是映射类型 + 约束 + `keyof`**，理解了这套就能自己造工具类型。

需要的话，下一步可以展开**条件类型 + `infer`**，那是泛型约束之后最该掌握的一环。

# 常用的工具类型

这五个是日常业务里出现频率最高的工具类型。下面按“**用途 → 签名 → 实现原理 → 注意事项 → 实战**”逐个讲。

## 一、Partial：全部变可选

### 用途
把一个类型的所有属性变成可选，常用于**更新（PATCH）**、**表单草稿**、**配置默认值合并**。

### 签名
```ts
type Partial<T> = {
  [P in keyof T]?: T[P];
};
```

### 示例
```ts
interface User {
  id: number;
  name: string;
  email: string;
}

type UserUpdate = Partial<User>;
// { id?: number; name?: string; email?: string }

function updateUser(id: number, patch: Partial<User>) {}

updateUser(1, { name: "a" }); // ✅ 只传部分字段
```

### 注意
- 只影响**第一层**，嵌套对象不会变可选。需要深层用递归：
```ts
type DeepPartial<T> = {
  [K in keyof T]?: T[K] extends object ? DeepPartial<T[K]> : T[K];
};
```
- 开启 `exactOptionalPropertyTypes` 后，`Partial` 的 `?:` 表示“可以不存在”，而不是“可以赋 undefined”，行为更严格。

## 二、Pick：挑出部分属性

### 用途
从一个大类型里**选取**若干属性，构造子类型。常用于 DTO、视图模型。

### 签名
```ts
type Pick<T, K extends keyof T> = {
  [P in K]: T[P];
};
```

关键在 `K extends keyof T`：**只允许传 T 上真实存在的键**。

### 示例
```ts
interface User {
  id: number;
  name: string;
  email: string;
  password: string;
}

type UserPreview = Pick<User, "id" | "name">;
// { id: number; name: string }

type Bad = Pick<User, "age">; // ❌ "age" 不在 keyof User 里
```

### 注意
- 第二个参数必须是**字面量联合**，不能是宽泛的 `string`。
- 想选“除了某几个”，用 `Omit`。

## 三、Omit：排除部分属性

### 用途
从类型里**去掉**若干属性。常用于“去掉敏感字段”“去掉服务端生成的字段”。

### 签名
```ts
type Omit<T, K extends keyof any> = Pick<T, Exclude<keyof T, K>>;
```

拆解：
1. `keyof T` 拿到所有键；
2. `Exclude<keyof T, K>` 去掉 K；
3. `Pick` 剩下的。

### 示例
```ts
type UserInput = Omit<User, "id" | "password">;
// { name: string; email: string }
```

### Pick vs Omit
| | 语义 | 适合场景 |
|---|---|---|
| `Pick` | 白名单，挑出想要的 | 字段少、明确要哪些 |
| `Omit` | 黑名单，排除不要的 | 字段多、只想去掉几个 |

### 注意
- `Omit` 的 `K extends keyof any` **不校验键是否存在**，写错键名不报错：
```ts
type Bad = Omit<User, "age">; // ✅ 不报错，但 age 本来就不存在
```
这是 `Omit` 相比 `Pick` 的一个弱点。需要严格校验可以自己写：
```ts
type StrictOmit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;
```
- `Omit` 作用在联合类型上会**丢失分发**，需要手动分发：
```ts
type DistributiveOmit<T, K extends keyof any> = T extends any ? Omit<T, K> : never;
```

## 四、Record：构造键值对类型

### 用途
描述“**键 → 值**”的映射结构，常用于字典、枚举映射、缓存表。

### 签名
```ts
type Record<K extends keyof any, T> = {
  [P in K]: T;
};
```

`K` 可以是 `string | number | symbol` 的子集。

### 示例
```ts
// 1. 字符串字典
type Dict = Record<string, number>;
const d: Dict = { a: 1, b: 2 };

// 2. 枚举映射（最常用）
type Status = "idle" | "loading" | "success" | "error";
type StatusText = Record<Status, string>;

const text: StatusText = {
  idle: "空闲",
  loading: "加载中",
  success: "成功",
  error: "失败",
}; // ✅ 少一个就报错

// 3. 数字键
type Index = Record<number, string>;

// 4. 配合泛型
function groupBy<T, K extends keyof any>(
  list: T[],
  getKey: (item: T) => K
): Record<K, T[]> {
  return list.reduce((acc, item) => {
    const k = getKey(item);
    (acc[k] ??= []).push(item);
    return acc;
  }, {} as Record<K, T[]>);
}
```

### 注意
- `Record<string, T>` 表示“任意字符串键”，但**不保证键存在**，访问可能返回 `undefined`（尤其开 `noUncheckedIndexedAccess`）。
- 想表达“固定几个键都必填”，用字面量联合：`Record<Status, string>`。
- `Record` 和 `{ [k: string]: T }` 基本等价，但 `Record` 更灵活（键可以是联合/数字/符号）。

## 五、ReturnType：取函数返回值类型

### 用途
从一个函数类型里**提取返回值类型**，常用于包装函数、复用已有函数的返回结构。

### 签名
```ts
type ReturnType<T extends (...args: any) => any> =
  T extends (...args: any) => infer R ? R : any;
```

核心是 `infer R`：在条件类型里推断返回值。

### 示例
```ts
function getUser() {
  return { id: 1, name: "a" };
}

type User = ReturnType<typeof getUser>;
// { id: number; name: string }
```

注意要配合 **`typeof`**：`ReturnType` 接受的是**函数类型**，不是函数本身。

```ts
type A = ReturnType<getUser>;        // ❌ getUser 是值不是类型
type B = ReturnType<typeof getUser>; // ✅
```

### 异步函数要配合 `Awaited`
```ts
async function fetchUser() {
  return { id: 1 };
}

type P = ReturnType<typeof fetchUser>;      // Promise<{ id: number }>
type U = Awaited<ReturnType<typeof fetchUser>>; // { id: number }
```

### 注意
- 对重载函数，`ReturnType` 取**最后一个重载签名**的返回值。
- 对泛型函数，返回的是**未实例化**的类型（可能含泛型参数）。
- 参数是 `any` 或非函数会报错（约束 `T extends (...args: any) => any`）。

## 六、五个类型的组合实战

### 1. 更新接口：`Partial` + `Omit`
```ts
interface User { id: number; name: string; email: string; password: string }

// 更新时不允许改 id 和 password，且都可选
type UserUpdate = Partial<Omit<User, "id" | "password">>;
// { name?: string; email?: string }
```

### 2. 创建接口：`Omit` + `Pick`
```ts
// 创建时不需要 id，但必须有 name/email/password
type UserCreate = Omit<User, "id">;
```

### 3. 列表项：`Pick`
```ts
type UserListItem = Pick<User, "id" | "name">;
type UserList = UserListItem[];
```

### 4. 状态文案：`Record`
```ts
type Status = "idle" | "loading" | "success" | "error";
const statusText: Record<Status, string> = { /* ... */ };
```

### 5. 从 API 函数推导类型：`ReturnType` + `Awaited`
```ts
async function apiGetUser() {
  return { id: 1, name: "a", email: "a@b.c" };
}

type User = Awaited<ReturnType<typeof apiGetUser>>;
type UserPreview = Pick<User, "id" | "name">;
```

## 七、易错点汇总

| 工具类型 | 易错点 |
|---|---|
| `Partial` | 只作用第一层；不理解 `exactOptionalPropertyTypes` |
| `Pick` | 第二个参数必须是 `keyof T` 的子集，不能传宽泛 `string` |
| `Omit` | 不校验键是否存在；对联合类型丢失分发 |
| `Record` | `Record<string, T>` 不保证键存在；键类型要合法 |
| `ReturnType` | 必须配 `typeof`；异步要 `Awaited`；重载取最后一个 |

## 八、记忆口诀

- **Partial**：全变可选 → 更新、补丁
- **Pick**：白名单挑选 → 视图、子集
- **Omit**：黑名单排除 → 去敏感、去只读字段
- **Record**：键值映射 → 字典、枚举文案
- **ReturnType**：反推返回 → 复用函数结构

这五个覆盖了日常 80% 的类型变换需求。剩下 20% 多是它们的组合，或用 `Parameters`、`Exclude`、`Extract`、`NonNullable`、`Readonly`、`Required` 补齐。

需要的话，下一步可以讲**映射类型 + 键重映射（`as`）**，那是理解这些工具类型底层、并自己造工具类型的最后一块。

# 从值/对象反推类型”的核心工具

这四个是 TS 类型系统里“**从值/对象反推类型**”的核心工具，配合使用能少写很多重复类型。逐个讲清楚。

## 一、`keyof`：取对象的键

### 作用
拿到一个类型的所有键，组成**字面量联合**。

```ts
interface User {
  id: number;
  name: string;
  email: string;
}

type UserKey = keyof User;
// "id" | "name" | "email"
```

### 常见用法

**1. 约束函数参数**
```ts
function get<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

const user = { id: 1, name: "a" };
get(user, "name"); // string
get(user, "age");  // ❌ "age" 不在 keyof 里
```

**2. 遍历键**
```ts
type UserFlags = {
  [K in keyof User]: boolean;
};
// { id: boolean; name: boolean; email: boolean }
```

**3. 配合条件类型过滤键**
```ts
type FunctionKeys<T> = {
  [K in keyof T]: T[K] extends (...args: any[]) => any ? K : never;
}[keyof T];

type StringKeys<T> = {
  [K in keyof T]: T[K] extends string ? K : never;
}[keyof T];
```

### 特例

```ts
type A = keyof any;        // string | number | symbol
type B = keyof unknown;    // never
type C = keyof never;      // string | number | symbol（never 特殊）

// 数组
type D = keyof string[];   // number | "length" | "push" | "pop" | ...
// 想只拿元素索引用 number
```

## 二、`typeof`：从值反推类型

### 作用
在**类型位置**使用，取一个变量/属性的类型。

```ts
const user = { id: 1, name: "a" };
type User = typeof user;
// { id: number; name: string }
```

### 和 JS 的 `typeof` 区别

| | JS 的 `typeof` | TS 的 `typeof` |
|---|---|---|
| 位置 | 表达式 | 类型位置 |
| 返回值 | 字符串 `"string"` 等 | 类型 |
| 用途 | 运行时判断 | 编译时反推 |

```ts
const x = "a";
const y = typeof x;        // JS：y = "string"（值）
type Z = typeof x;         // TS：Z = "a"（类型，注意是字面量！）
```

### 常见用法

**1. 复用已有值的类型**
```ts
const defaultConfig = {
  host: "localhost",
  port: 8080,
  debug: false,
};

type Config = typeof defaultConfig;
// { host: string; port: number; debug: boolean }
```

**2. 取函数类型**
```ts
function getUser() {
  return { id: 1, name: "a" };
}

type GetUser = typeof getUser;
// () => { id: number; name: string }

type User = ReturnType<typeof getUser>;
// { id: number; name: string }
```

**3. 取枚举/常量的类型**
```ts
const STATUS = {
  IDLE: "idle",
  LOADING: "loading",
} as const;

type StatusKey = keyof typeof STATUS;   // "IDLE" | "LOADING"
type StatusValue = typeof STATUS[keyof typeof STATUS]; // "idle" | "loading"
```

注意 `as const` 很关键，否则 `typeof STATUS` 会变成 `{ IDLE: string; LOADING: string }`。

### 注意
- 只能对**值**用，不能对类型用：`typeof User`（User 是类型）会报错。
- 对 `let` 变量推断的是**宽泛类型**，对 `const` 是字面量：
```ts
let a = "x";   // typeof a = string
const b = "x"; // typeof b = "x"
```

## 三、索引访问类型（Indexed Access）

### 作用
用类似 JS 的 `obj[key]` 语法，从类型里取某个属性的类型。

```ts
interface User {
  id: number;
  name: string;
  address: {
    city: string;
    zip: string;
  };
}

type Id = User["id"];              // number
type Name = User["name"];          // string
type City = User["address"]["city"]; // string

// 用联合键取多个
type IdOrName = User["id" | "name"]; // number | string

// 用 keyof 取所有值
type AllValues = User[keyof User];   // number | string | { city: string; zip: string }
```

### 常见用法

**1. 配合泛型**
```ts
function get<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}
// T[K] 就是“obj 上 key 对应的值的类型”
```

**2. 配合数组**
```ts
type Arr = string[];
type Elem = Arr[number]; // string

type Tuple = [number, string, boolean];
type T0 = Tuple[0];      // number
type T1 = Tuple[1];      // string
type Union = Tuple[number]; // number | string | boolean
```

`T[number]` 是取数组/元组元素类型的标准写法，非常常用。

**3. 配合 `typeof`**
```ts
const colors = ["red", "green", "blue"] as const;
type Color = typeof colors[number]; // "red" | "green" | "blue"
```

这一句是“**从数组常量提取字面量联合**”的黄金组合，几乎每个项目都会用到。

## 四、`as const`：保留字面量

### 作用
让 TS 把值推断成**最窄的字面量类型**，并加上 `readonly`。

```ts
// 不加 as const
const a = { x: 1, y: "a" };
// { x: number; y: string }

// 加 as const
const b = { x: 1, y: "a" } as const;
// { readonly x: 1; readonly y: "a" }
```

### 对不同类型的表现

```ts
// 字符串
const s1 = "a";          // "a"（const 本来就是字面量）
let s2 = "a";            // string
const s3 = "a" as const; // "a"

// 对象
const o = { a: 1 } as const;
// { readonly a: 1 }

// 数组
const arr1 = [1, 2, 3];           // number[]
const arr2 = [1, 2, 3] as const;  // readonly [1, 2, 3]（元组！）

// 嵌套
const nested = { a: [1, 2], b: { c: "x" } } as const;
// { readonly a: readonly [1, 2]; readonly b: { readonly c: "x" } }
```

### 常见用法

**1. 从数组提取字面量联合**
```ts
const ROLES = ["admin", "user", "guest"] as const;
type Role = typeof ROLES[number]; // "admin" | "user" | "guest"

function checkRole(r: Role) {}
checkRole("admin"); // ✅
checkRole("root");  // ❌
```

**2. 配置对象保留字面量**
```ts
const CONFIG = {
  host: "localhost",
  port: 8080,
} as const;

type Host = typeof CONFIG.host; // "localhost"，而不是 string
```

**3. 配合 `satisfies` 既校验又保留字面量**
```ts
const ROUTES = {
  home: "/",
  about: "/about",
} as const satisfies Record<string, string>;

type Route = typeof ROUTES[keyof typeof ROUTES]; // "/" | "/about"
```

这里 `satisfies` 保证结构合法，`as const` 保证字面量不丢失。

**4. 枚举的替代方案**
```ts
// 用 as const 替代 enum（现代推荐，避免 enum 的运行时开销）
const Status = {
  Idle: "idle",
  Loading: "loading",
  Success: "success",
} as const;

type Status = typeof Status[keyof typeof Status]; // "idle" | "loading" | "success"
```

### 注意
- `as const` 会让对象变 `readonly`，赋值给可变类型会报错：
```ts
const arr = [1, 2] as const;
const x: number[] = arr; // ❌ readonly 不能赋给可变
const y: readonly number[] = arr; // ✅
```
- 只能用在**字面量表达式**上，不能用在变量上：
```ts
const a = [1, 2];
const b = a as const; // ❌ 报错
```

## 五、四者组合：黄金搭配

### 1. 从常量数组提取联合
```ts
const FRUITS = ["apple", "banana", "orange"] as const;
type Fruit = typeof FRUITS[number]; // "apple" | "banana" | "orange"
```
**`as const` + `typeof` + 索引访问 `[number]`**

### 2. 从对象提取键和值
```ts
const MAP = {
  a: 1,
  b: 2,
} as const;

type Key = keyof typeof MAP;              // "a" | "b"
type Value = typeof MAP[keyof typeof MAP]; // 1 | 2
```
**`keyof` + `typeof` + `keyof` + 索引访问**

### 3. 类型安全的 `get`
```ts
function get<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}
```
**`keyof` + 索引访问 `T[K]`**

### 4. 配置对象类型复用
```ts
const config = {
  api: { baseUrl: "/api", timeout: 5000 },
  features: { darkMode: true },
} as const;

type Config = typeof config;
type ApiConfig = Config["api"];        // { readonly baseUrl: "/api"; readonly timeout: 5000 }
type Timeout = Config["api"]["timeout"]; // 5000
```
**`as const` + `typeof` + 索引访问**

### 5. 事件名映射
```ts
const EVENTS = {
  click: (e: MouseEvent) => {},
  keydown: (e: KeyboardEvent) => {},
} as const;

type EventName = keyof typeof EVENTS;    // "click" | "keydown"
type EventPayload<T extends EventName> = Parameters<typeof EVENTS[T]>[0];
```
**`keyof` + `typeof` + `Parameters` + 索引访问**

## 六、易错点汇总

| 工具 | 易错点 |
|---|---|
| `keyof` | `keyof any` 是 `string \| number \| symbol`；`keyof unknown` 是 `never` |
| `typeof` | 只能对值用；`let` 推断宽泛，`const` 推断字面量；配 `as const` 才保留字面量 |
| 索引访问 | `T[number]` 取数组元素；`T[keyof T]` 取所有值的联合；元组越界报错 |
| `as const` | 产生 `readonly`；不能用在变量上；只对字面量生效 |

## 七、记忆口诀

- **`keyof`**：取键 → `"a" | "b"`
- **`typeof`**：取值反推类型 → 必须作用在值上
- **索引访问 `T[K]`**：取值类型 → 配 `keyof` 取全部，配 `number` 取数组元素
- **`as const`**：锁字面量 + 只读 → 常量的最佳搭档

最常用的一行代码：
```ts
const X = [...] as const;
type T = typeof X[number];
```
这一行就完成了“从值 → 字面量联合类型”的转换，是 TS 里最实用的模式之一。

需要的话，下一步可以讲**映射类型 + 键重映射（`as`）**，那是把这些拼成完整工具类型的最后一块。

# 可辨识联合 + 类型收窄

可辨识联合（Discriminated Union）是 TypeScript 里**建模状态**最核心的手段，类型收窄则是配套的“自动识别当前是哪个状态”的能力。两者几乎总是一起出现。

## 一、什么是可辨识联合

一个联合类型，其中每个成员都有一个**共同的字面量字段**作为“标签”，TS 靠这个标签来区分成员。

```ts
type Shape =
  | { kind: "circle"; radius: number }
  | { kind: "square"; side: number }
  | { kind: "rectangle"; width: number; height: number };
```

三个条件缺一不可：

1. **联合类型**：`A | B | C`
2. **共同字段**：每个成员都有 `kind`
3. **字面量类型**：`kind` 的值是具体字面量 `"circle"` 等，不是 `string`

满足后，`switch (shape.kind)` 就能自动收窄到具体成员。

## 二、为什么叫“可辨识”

```ts
function area(shape: Shape) {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2; // shape 收窄为 circle
    case "square":
      return shape.side ** 2;             // shape 收窄为 square
    case "rectangle":
      return shape.width * shape.height;  // shape 收窄为 rectangle
  }
}
```

`shape.kind` 就是**判别式（discriminant）**，TS 通过它“辨识”出当前是哪个成员。

如果 `kind` 是 `string` 而不是字面量：

```ts
type Bad = { kind: string; radius: number } | { kind: string; side: number };
// ❌ 无法收窄，kind 不能区分两者
```

## 三、判别式的选择

判别式可以是任何字面量类型：`string`、`number`、`boolean`、`symbol`，甚至嵌套字面量。

```ts
// 字符串标签（最常用）
type A = { type: "success"; data: string } | { type: "error"; message: string };

// 数字标签
type B = { status: 200; body: string } | { status: 404; error: string };

// 布尔标签
type C = { ok: true; value: number } | { ok: false; error: string };

// 可辨识 + 可选字段
type D =
  | { kind: "none" }
  | { kind: "some"; value: number };
```

### 命名习惯
- `kind`、`type`、`tag`、`status`、`variant` 都常见
- 团队内保持一致即可，`kind` 和 `type` 最多

## 四、收窄的几种触发方式

### 1. `switch`（最推荐）

```ts
switch (shape.kind) {
  case "circle": return ...;
  case "square": return ...;
}
```

配合 `never` 做**穷尽性检查**（见下文）。

### 2. `if` + 相等比较

```ts
if (shape.kind === "circle") {
  shape.radius;
} else {
  // shape 收窄为 square | rectangle
}
```

### 3. 解构后判断

```ts
const { kind } = shape;
if (kind === "circle") {
  shape.radius; // ✅ 仍能收窄
}
```

解构字面量字段后，TS 仍能追踪收窄（前提是 `shape` 不再被重新赋值）。

### 4. 嵌套可辨识联合

```ts
type Response =
  | { status: "ok"; payload: { kind: "text"; content: string } | { kind: "json"; data: unknown } }
  | { status: "error"; message: string };

if (res.status === "ok") {
  if (res.payload.kind === "text") {
    res.payload.content; // string
  }
}
```

### 5. 自定义类型守卫

```ts
type Circle = { kind: "circle"; radius: number };

function isCircle(s: Shape): s is Circle {
  return s.kind === "circle";
}

if (isCircle(shape)) {
  shape.radius;
}
```

## 五、穷尽性检查（Exhaustiveness Check）

最实用的技巧之一：确保 `switch` 覆盖了所有成员，漏掉就编译报错。

```ts
function area(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2;
    case "square":
      return shape.side ** 2;
    case "rectangle":
      return shape.width * shape.height;
    default:
      const _exhaustive: never = shape; // ✅ 全部覆盖，shape 是 never
      throw new Error(`Unknown shape: ${_exhaustive}`);
  }
}
```

新增成员时：

```ts
type Shape =
  | { kind: "circle"; radius: number }
  | { kind: "square"; side: number }
  | { kind: "rectangle"; width: number; height: number }
  | { kind: "triangle"; base: number; height: number }; // 新增
```

`area` 里 `default` 分支的 `shape` 不再是 `never`，赋值给 `never` 报错，**编译期就提醒你补分支**。

### 不写 `default` 也能穷尽

```ts
function area(shape: Shape): number {
  switch (shape.kind) {
    case "circle": return ...;
    case "square": return ...;
    case "rectangle": return ...;
  }
  // 这里 shape 是 never
}
```

但显式写 `default` + `never` 断言更直观，也防止“未来加成员忘了处理”时静默通过。

### 用 `assertNever` 封装

```ts
function assertNever(x: never): never {
  throw new Error(`Unexpected value: ${JSON.stringify(x)}`);
}

function area(shape: Shape): number {
  switch (shape.kind) {
    case "circle": return ...;
    case "square": return ...;
    case "rectangle": return ...;
    default: return assertNever(shape);
  }
}
```

这是社区最常见的写法。

## 六、实战模式

### 1. 请求状态

```ts
type RequestState<T> =
  | { status: "idle" }
  | { status: "loading" }
  | { status: "success"; data: T }
  | { status: "error"; error: Error };

function render<T>(state: RequestState<T>) {
  switch (state.status) {
    case "idle": return null;
    case "loading": return <Spinner />;
    case "success": return <View data={state.data} />;  // state.data 安全
    case "error": return <Error msg={state.error.message} />;
  }
}
```

**这是替代“多个可选字段”的最佳实践**。对比：

```ts
// ❌ 不推荐：字段之间没有约束
interface BadState<T> {
  loading: boolean;
  data?: T;
  error?: Error;
}
// 可能出现 loading=true 又有 data 的非法状态
```

可辨识联合让**非法状态无法表示**（make illegal states unrepresentable）。

### 2. 动作/事件

```ts
type Action =
  | { type: "increment"; amount: number }
  | { type: "decrement"; amount: number }
  | { type: "reset" }
  | { type: "set"; value: number };

function reducer(state: number, action: Action): number {
  switch (action.type) {
    case "increment": return state + action.amount;
    case "decrement": return state - action.amount;
    case "reset": return 0;
    case "set": return action.value;
  }
}
```

Redux / useReducer 的类型基础。

### 3. 表单校验结果

```ts
type ValidationResult =
  | { valid: true; value: string }
  | { valid: false; errors: string[] };

function validate(input: unknown): ValidationResult {
  if (typeof input === "string" && input.length > 0) {
    return { valid: true, value: input };
  }
  return { valid: false, errors: ["不能为空"] };
}

const r = validate(x);
if (r.valid) {
  r.value;   // ✅
} else {
  r.errors;  // ✅
}
```

### 4. API 响应

```ts
type ApiResult<T> =
  | { ok: true; data: T }
  | { ok: false; code: number; message: string };

async function fetchUser(): Promise<ApiResult<User>> { /* ... */ }

const res = await fetchUser();
if (res.ok) {
  res.data.name; // ✅
} else {
  console.log(res.code, res.message); // ✅
}
```

### 5. 与泛型结合

```ts
type Result<T, E = Error> =
  | { ok: true; value: T }
  | { ok: false; error: E };

function unwrap<T, E>(r: Result<T, E>): T {
  if (r.ok) return r.value;
  throw r.error;
}
```

类似 Rust 的 `Result` 类型。

## 七、进阶技巧

### 1. 提取某个成员

```ts
type Shape =
  | { kind: "circle"; radius: number }
  | { kind: "square"; side: number };

// 提取 circle 成员
type Circle = Extract<Shape, { kind: "circle" }>;
// { kind: "circle"; radius: number }

// 提取多个
type Roundish = Extract<Shape, { kind: "circle" | "oval" }>;
```

### 2. 排除某个成员

```ts
type NonCircle = Exclude<Shape, { kind: "circle" }>;
// { kind: "square"; side: number }
```

### 3. 按 kind 建映射

```ts
type ShapeMap = {
  circle: { radius: number };
  square: { side: number };
};

type Shape = {
  [K in keyof ShapeMap]: { kind: K } & ShapeMap[K];
}[keyof ShapeMap];

// 等价于原来的 Shape，但避免了重复写 kind
```

### 4. 每个分支单独定义 + 联合

```ts
type Circle = { kind: "circle"; radius: number };
type Square = { kind: "square"; side: number };
type Shape = Circle | Square;

// 之后可以单独引用 Circle，比内联联合更易读
```

### 5. 判别式 + 可选字段

```ts
type Notification =
  | { type: "email"; to: string; cc?: string }
  | { type: "sms"; phone: string }
  | { type: "push"; deviceId: string; silent?: boolean };
```

可选字段属于某个成员，不影响判别。

## 八、易错点

### 1. 判别式不是字面量

```ts
type Bad = { kind: string; a: number } | { kind: string; b: number };
// ❌ kind 是 string，无法收窄
```

必须写成 `kind: "a"` / `kind: "b"`。

### 2. 忘了 `as const`

```ts
const KIND = { Circle: "circle", Square: "square" };
type Shape = { kind: typeof KIND.Circle; radius: number }; // ❌ string
```

用 `as const` 锁字面量：

```ts
const KIND = { Circle: "circle", Square: "square" } as const;
type Shape = { kind: typeof KIND.Circle; radius: number }; // ✅ "circle"
```

### 3. 成员间字段冲突

```ts
type Bad =
  | { kind: "a"; value: string }
  | { kind: "b"; value: number };
```

这其实没问题——`value` 在不同成员里类型不同，收窄后各自安全。但访问共有字段时要注意：

```ts
function f(x: Bad) {
  x.value; // string | number，需要进一步收窄
}
```

### 4. 回调中收窄丢失

```ts
function f(shape: Shape) {
  if (shape.kind === "circle") {
    setTimeout(() => {
      shape.radius; // ❌ shape 可能已变
    });
  }
}
```

解决：赋给 `const` 局部变量。

```ts
if (shape.kind === "circle") {
  const r = shape.radius;
  setTimeout(() => r, 100);
}
```

### 5. `switch` 里用变量 case

```ts
const k = "circle";
switch (shape.kind) {
  case k: // ❌ 不触发收窄，case 必须是字面量
}
```

### 6. 判别式被重新赋值

```ts
let shape: Shape = ...;
if (shape.kind === "circle") {
  shape = { kind: "square", side: 1 }; // 改变后收窄失效
  shape.radius; // ❌
}
```

## 九、小结

### 可辨识联合的四个要素

| 要素 | 说明 |
|---|---|
| 联合类型 | `A \| B \| C` |
| 共同字段 | 每个成员都有，如 `kind` |
| 字面量值 | `"circle"` 而非 `string` |
| 收窄判断 | `switch` / `if` 比较字面量 |

### 核心价值

1. **让非法状态无法表示**：避免“多个可选字段”导致的非法组合。
2. **收窄后类型安全**：每个分支访问专属字段不报错。
3. **穷尽性检查**：新增成员时编译期提醒补分支。
4. **可读性强**：状态建模清晰，`switch` 一目了然。

### 记忆口诀

- **有标签**：每个成员都有共同的字面量字段
- **能收窄**：`switch` / `if` 比较标签自动识别
- **会穷尽**：`default` + `never` 断言，漏分支编译报错
- **禁非法**：用联合替代“可选字段堆砌”，让非法状态无法表示
