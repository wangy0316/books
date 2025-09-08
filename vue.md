图像识别文字：umi-OCR:、onnx

[自动表格计算AutoCalculate](https://juejin.cn/post/6873694006736322573?searchId=202507211114242DC18B9F76F2DC410F02#heading-14)

```js
// v-model语法糖，等价于绑定value属性和监听input事件
<child :value="data" @input="data = $event" />
```

[iframe嵌套可能遇见的问题](https://juejin.cn/post/7454466304951746601)

```js
// 如果不存储array.length，每次循环都会重新计算。
for (let k = 0, len = array.length; k < len; k++) {}
```

错误处理

`try catch`是无法捕获到异步错误的，这时候就要用到`window`的`error`事件。

`window.addEventListener`可以监听`error`，`window.onerror`也可以监听`error`，但是`window.onerror`和`window.addEventListener`相比，无法监听网络异常。

`window.onerror`和`window.addEventListener`，~~对`Promise`报错都是无法捕获~~。可以通过`addEventListener`监听unhandledrejection事件来捕获。

js中`同步`、`异步`、`资源加载`、`Promise`、`async/await`都有相对应的捕获方式。

Vue的全局错误处理函数`Vue.config.errorHandler`

token失效的无感刷新，如果有两个接口，根据前面的接口返回数据后，拿参数调用后面的接口。这种方式无感刷新没办法做到调用后面的接口，因为前面接口报错后，记录的也是前面的接口，然后刷新token后，自动调用失去token的接口。不会去调用还没调用过的接口。如果需要调用其实就是刷新token后页面要重新刷新，让调用逻辑重新走。

调试模式下输入 documnet.designMode = 'on'  'off' 可以编辑页面

Blob（二进制大对象）本身设计为不直接加载整个文件到内存，而是通过流式处理方式按需读取数据。这种特性使其适用于处理大型文件或复杂数据流，避免内存占用过高。

将函数拆分成更小的更专注的函数，易于理解和维护，验证可以以validate开头，比如validateName，validateEmail等。

# websocket

websocket是html5的一个特性，为解决不同浏览器对websocket的支持，于是使用socket.io。socket.io底层实现有五种方式，websocket知识其中一种。

默认情况下，无论我们创建了多少次socket连接，指向都是同一个socket实例，除非我们在connect()的第二个参数中指定"force new socket'"强制建立一个新的连接。

socket.io的io()和io.connect()，都是socket.io库中用于建立与服务器的WebSocket连接。io()更简洁，io.connect()更灵活。

客户端成功加载socket.io客户端文件后会获取到一个全局对象io

```js
var socket = io.connect('/');
socket.on('connect',function(){
    //连接成功
});
socket.on('disconnect',function(data){
    //连接断开
});
```

```js
// 发件人
socket.emit("hello", "world", (response) => {
  console.log(response);
});

// 接收者
socket.on("hello", (arg, callback) => {
  console.log(arg);
  callback("got it!");
});
```

# Axios

CancelToken取消接口,通过创建一个取消令牌（cancel token），将其附加到请求中，当需要取消请求时调用 `cancel()` 方法

```js
const CancelToken = axios.CancelToken;
const source = {}

source[key].cancel()
source[key] = CancelToken.source();
request({
  url,
  data,
  cancelToken: source[key].token
})
```

一旦取消请求，`CancelToken` 的 `token` 将失效，需要重新创建新的 `CancelToken` 对象。

AbortController取消接口

```js
const ControllerMap = {}

ControllerMap[key].abort()
ControllerMap[key] = new AbortController()
request({
  url,
  data,
  signal : ControllerMap[key].signal
})

```

1

# vue

[乐观锁机制解决多次请求，但是请求快慢导致数据展示错误](https://juejin.cn/post/7516729363015024677)

// 使用方括号 [] 动态设置对象的键
const key = "dynamicKey";
const obj = {
[key]: "value",
};
console.log(obj.dynamicKey); // "value"

// 使用双位运算符 ~~ 快速向下取整,替代 Math.floor(),仅使用正数
const num = 5.8;
console.log(~~num); // 5

### render、jsx、h函数

vue大部分时间都在使用template的方式来创建HTML，但除此之外，Vue还提供了render()函数来创建HTML。让我们可以通过JS逻辑代码，更灵活的创建HTML。

render函数

```js
render: function (createElement) {
  var myParagraphVNode = createElement('p', 'hi')
  return createElement('div', [
    myParagraphVNode, myParagraphVNode
  ])
}
```

这种写法阅读性太差，于是我们可以使用jsx，在js中写html

```js
  render: function (h) {
    return (
      <AnchoredHeading level={1}>
        <span>Hello</span> world!
      </AnchoredHeading>
    )
  }
```

h函数就是vnode函数，更准确的命名为createdVnode函数。

template 中的HTML 最终也是使用渲染函数生成对应的VNode函数。

# vue3

```
// Vue 3中不再推荐使用.sync，转而用带参数的v-model
<child v-model:name="userName" v-model:age="userAge" />

// shallowReactive它只会监听对象的顶层属性变化,比reactive更节约性能。
const data = shallowReactive({
    name: 'zhangsan',
    age: 18,
    address: {
      name: 'beijing',
    },
    list: [1,2]
 })
// 直接修改address.name页面是不会更新的。但是修改address页面会更新。其次同时修改age和address.name页面也会更新。

// reactive结构会造成响应丢失。可以通过toRefs将响应式对象转换为多个ref
const { name, age, email } = toRefs(user)
name.value = 123 // 这里的name依旧具有响应式

// Teleport 将内容“传送”到指定位置。解决样式覆盖、层级错乱这些老大难问题
<Teleport to="body"></Teleport>

```
