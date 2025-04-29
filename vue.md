图像识别文字：umi-OCR:、onnx

```js
// v-model语法糖，等价于绑定value属性和监听input事件
<child :value="data" @input="data = $event" />
```

[iframe嵌套可能遇见的问题](/https://juejin.cn/post/7454466304951746601)

```js
// 如果不存储array.length，每次循环都会重新计算。
for (let k = 0, len = array.length; k < len; k++) {}
```

错误处理

`try catch`是无法捕获到异步错误的，这时候就要用到`window`的`error`事件。

`window.addEventListener`可以监听`error`，`window.onerror`也可以监听`error`，但是`window.onerror`和`window.addEventListener`相比，无法监听网络异常。

`window.onerror`和`window.addEventListener`，对`Promise`报错都是无法捕获。

js中`同步`、`异步`、`资源加载`、`Promise`、`async/await`都有相对应的捕获方式。

Vue的全局错误处理函数`Vue.config.errorHandler`

token失效的无感刷新，如果有两个接口，根据前面的接口返回数据后，拿参数调用后面的接口。这种方式无感刷新没办法做到调用后面的接口，因为前面接口报错后，记录的也是前面的接口，然后刷新token后，自动调用失去token的接口。不会去调用还没调用过的接口。如果需要调用其实就是刷新token后页面要重新刷新，让调用逻辑重新走。

调试模式下输入 documnet.designMode = 'on'  'off' 可以编辑页面

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

# vue3

```
// Vue 3中不再推荐使用.sync，转而用带参数的v-model
<child v-model:name="userName" v-model:age="userAge" />
```
