错误处理

`try catch`是无法捕获到异步错误的，这时候就要用到`window`的`error`事件。

`window.addEventListener`可以监听`error`，`window.onerror`也可以监听`error`，但是`window.onerror`和`window.addEventListener`相比，无法监听网络异常。

`window.onerror`和`window.addEventListener`，对`Promise`报错都是无法捕获。

js中`同步`、`异步`、`资源加载`、`Promise`、`async/await`都有相对应的捕获方式。

Vue的全局错误处理函数`Vue.config.errorHandler`
