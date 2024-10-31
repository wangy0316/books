自带顶部title栏，通过navigationStyle：custom设置为不需要
```json
{
  "path" : "pages/home/index",
  "style" : {
    "navigationStyle": "custom",
  }
},
```

打包为app不运行将css样式挂载在main.js上。推荐使用link的方式挂载在index.html上，也可以在app.vue的style中挂载