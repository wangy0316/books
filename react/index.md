### hook
1.组件函数首字母必须大写,不然就是普通函数,不能使用hook



### 基础组件
1. Redux
2. ant
3. [React Router Dom](https://reactrouter.cn/docs/getting-started/installation)

### 基础概念
```jsx
// router
import {useNavigate} from "react-router-dom";
路径跳转使用 Link 组件，而不是 a 标签
渲染嵌套路由使用 Outlet 组件
方法调用使用useNavigate
let navigate = useNavigate()
navigate('/path')
获取router参数
写法:path: "/route/:id"  
效果:http://localhost:3000/route/1  
使用: useParams()

```

```jsx
const ProductDetails = () => {
  return (
    <>
      <h1>产品详情</h1>
    </>
  )
}
// 这里的<>和</>是React.Fragment的简写，用于包裹多个元素，而不会增加额外的DOM节点。  
// React.Fragment 是 React 提供的一个组件，用于在不额外创建 DOM 元素的情况下包裹一组子元素。它的主要作用是让你可以返回多个元素，而不会在页面中生成多余的节点。
```


```jsx

// react-helmet-async提供了一种高效的方式来管理和更新React组件树中的元数据。核心是Helmet、HelmetProvider组件。在整个React树中传递。

```
