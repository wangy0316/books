* 文案中间添加省略：https://codepen.io/xboxyan/pen/VwpPNbm?fileGuid=YcHxPHhHvDtpqvDw，https://segmentfault.com/a/1190000040057525
* hover情况下字体加粗效果不影响布局：https://blog.csdn.net/qq_51066068/article/details/127047704

1. lin-height: 2em; 2em代表当前元素的文字内容占2倍行高，如果文字大小发生改变，高也会自动改变。在父子嵌套中子的字体很大会造成展示不全。
   line-height:2；根据子元素计算行高（取最大字体值*2）,这种更合理，取当最大字体的两倍。
2. flex全局样式命名通过row和col来区分横向和竖向.比如，col-center代表竖向居中，row-center代表横向居中
3. clip-path 生成器: https://tools.jb51.net/static/api/css3path/index.html
4. flex样式下，可以使用gap来调整margin.
5. flex样式下，三个div如果左后一个要在最右边，可以设置margin-left:auto
6.
7. css动态变量：

```
<p class="size" style="--width: 100px">动态演示</p>
.size {
  width: var(--width);
}
// 动态赋值
ref.style.setProperty('--width', '200px')
```
