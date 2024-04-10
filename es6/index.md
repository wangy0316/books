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
```
