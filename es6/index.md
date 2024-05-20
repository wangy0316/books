```javascript
// 解构赋值, 赋值已声明变量写法
const obj = {a:1, b:2}
let a = null,
    b = null
({a, b} = obj)

// 当data为 undefined 、null 时候，代码就会报错需要增加或写法
const { data } = props;
const { name, age } = data || {}

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




```
