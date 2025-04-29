```javascript
// 同源策略主要限制的是 JavaScript 发起的异步请求（如 AJAX），而不是表单提交
// 使用 contains 方法来实现检测元素之外的点击

// 返回什么值
console.log('b', arr.length && 'a')
// 这里会判断arr是否为true，如果是true则返回'a', 如果为false，那么这里就返回false。
// 如果arr = [] 那么这里就有个特殊情况，会返回0.
// 如果只能返回false和'a'，那么这里就需要用到!!
!!arr.length && 'a'

// 解构赋值, 赋值已声明变量写法
const obj = {a:1, b:2}
let a = null,
    b = null
({a, b} = obj)
// 解构数组中的对象
const arr = [{name:1}]
const [{name:value}] = arr
value就是1

// 当data为 undefined 、null 时候，代码就会报错需要增加或写法
const { data } = props;
const { name, age } = data || {}

// 需要将赋值语句用用括号括起来
isBoolean && (value = '条件达成');

// data必须为真数组
if (Array.isArray(data)) {
  nameList = data.map(item => item.name)
}

// try...catch只能捕捉到同步执行代码块中的错误，所以如果是异步代码，需要在异步操作（setTimeout）中处理错误。在Promise链中使用.catch方法或者在一个async函数中使用try...catch

// 处理URL查询参数是一项相对繁琐的任务。开发人员需要手动解析URL，提取查询字符串部分，并对其进行分割和解码。这个过程涉及到很多繁琐的字符串操作和编码解码步骤，容易出错且代码冗长。为了简化这一过程，ECMAScript 2015（ES6）引入了URLSearchParams API。
const params = new URLSearchParams()
// 添加参数，可通过toString()方法输出拼接的参数，就不需要自己手动拼接?和&符号。
params.append('page', 1)
const baseURL = "https://fakestoreapi.com/products"
const url = new URL(baseURL)
url.search = params.toString()

// 清理数组
// Boolean函数作为回调。它将会从数组中删除所有假值（ 0，false，null，undefined，''，NaN）
arr.filter(Boolean)

// 数组中数据检查
some(()=>)函数用于检查数组中是否至少有一个元素不符合要求，every(()=>)来检查数组的所有元素是否全部符合要求

// 取两个数组的交集
arr1.filter(value => arr2.includes(value))

// 数组求和
const arr = [1, 2, 3, 4]
const sum = arr.reduce((total, value) => total + value, 0);

// 获取后缀名称
const filePath = "file://upload/test.png"
const index = filePath.lastIndexOf(".")
const ext = index > -1 && filePath.substr(index+1)
// 或者
filePath.split(".").pop()

// 字符串转对象
const query = '?name=John&age=30'
// 将字符串解析为对象
const parseQuery = query => Object.fromEntries(new URLSearchParams(query));

// ??和||区别
let a = value ?? 1
let a = value || 1
// || 的左侧如果为假值，它将会返回右侧的值。JavaScript 中的假值包括null、undefined、0、NaN、''（空字符串）和false
// 空值合并运算符 (??) 检查左侧是否为null或undefined

// 如果解构模式是嵌套的对象，而且子对象所在的父属性不存在，那么将会报错
// 可以设置默认值
const { b: { c=null } } = { b: { } } 
// 这种情况下，c还是会报错
const { b: { c=null } } = { b: null } 
console.log('b', c)

let obj = {
    a: 1,
    b: null
}
const {a,b:{c}} = obj
// 由于b不存在这将报错 obj中的b需要写成 b: null || {}
// 根据这个特性使用map输出需要的字段就需要主要这种父属性不存在但是需要取子属性的情况

// 妙用reduce可遍历数组中的对象值
[].renduce((prev,cur)=>{
  const value = []  
  return value
} , [])

// includes和indexOf对unll和undefined是不同的标准，indexOf() 是使用的严格相等算法（===），而includes() 是使用的是抽象相等算法（==）。使用时需要注意

LRU（Least Recently Used）缓存是一种缓存淘汰策略，用于在缓存容量有限的情况下，决定哪些数据需要被移除。其核心思想是：当缓存满了，需要移除最久未被使用的数据，以给新的数据腾出空间。


```
