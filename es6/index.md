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
```
