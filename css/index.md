* [文案中间添加省略](https://codepen.io/xboxyan/pen/VwpPNbm?fileGuid=YcHxPHhHvDtpqvDw)
* [文案中间添加省略2](https://segmentfault.com/a/1190000040057525)
* [hover情况下字体加粗效果不影响布局](https://blog.csdn.net/qq_51066068/article/details/127047704)

1. lin-height: 2em; 2em代表当前元素的文字内容占2倍行高，如果文字大小发生改变，高也会自动改变。在父子嵌套中子的字体很大会造成展示不全。
   line-height:2；根据子元素计算行高（取最大字体值*2）,这种更合理，取当最大字体的两倍。
2. flex全局样式命名通过row和col来区分横向和竖向.比如，col-center代表竖向居中，row-center代表横向居中
3. clip-path 生成器: https://tools.jb51.net/static/api/css3path/index.html
4. flex样式下，可以使用gap来调整margin.
5. flex样式下，三个div如果左后一个要在最右边，可以设置margin-left:auto
6. flex样式下，三个p标签，一行展示，中间自适应超出添加省略号，

   ```html
   <div class="flex">
     <p class="flex-shrink">1</p>
     <p class="ellipsis">2</p>
     <p class="flex-shrink">3</p>
   </div>
   // 如果第一个和第二个需要在一起展示
   <div class="flex">
     <div style="min-width: 0px">
       <p class="flex-shrink">1</p>
       <p>2</p>
     </div>
     <p class="flex-shrink">3</p>
   </div>
   ```
7. gsap不遵守开源协议的动画库。
8. [移动端使用 100vh 导致页面出现滚动条](https://juejin.cn/post/7402916277272739890)
9.
10. css动态变量：

    ```html
    <p class="size" style="--width: 100px">动态演示</p>
    .size {
      width: var(--width);
    }
    // 动态赋值
    ref.style.setProperty('--width', '200px')
    // 变量回退，如果--actions-width未定义则会回退使用 120px
    .modal {
      max-width: calc(100% - var(--actions-width, 120px)) 
    }
    ```
11. gspa动画库, [基础概念](http://www.lixianglong.cn/2024/07/30/application/fore-end/threejs/GSAP%E5%8A%A8%E7%94%BB%E5%8F%82%E7%85%A7%E8%AF%A6%E8%A7%A3/)

    ```js
    import gsap from "gsap";
    import ScrollTrigger from "gsap/ScrollTrigger";
    gsap.registerPlugin(ScrollTrigger)
    // 引入ScrollTrigger
    gsap
        .timeline({
          scrollTrigger: {
            trigger: dom,     // 引入滑动效果的块
            start: 'top 70%', // 滚动条顶部的距离
            end: '+=800',     // 每滚动多少距离执行一次效果
            scrub: 1,
            toggleActions: 'play none reverse none'
          }
        })
        .to(".select-line", { // 需要动画移动的类名
          stagger: 0.1,       // 每个动画的起始时间间隔
          y: -20,             // dom向y轴移动距离
          keyframes: {
            '0%': { color: '#ccc' },
            '25%': { color: '#4c4c4c' },
            '50%': { color: '#ffffff' },
            '75%': { color: '#ffffff' },
            '100%': { color: '#ffffff' }
          }
        })
    /*
      to():从初始状态到目标状态，form():和to反过来，fromTo()：自定义起始值和结束值
      pin: true // 有点类似fixed的效果
      scrub: 1  // 缓冲动画的延迟时间
    */
    ```

```
作为常识大家要清楚html,body默认也是height:auto的，通常都是计算完子元素的高度后才计算父元素的高度。
默认情况：普通文档流，父元素height：auto 这种情况下，父元素也就是body，html高度均为自动，子元素高度设置height:100%无任何效果，原因也很简单。auto\*100% 无法计算，当然是0。这点与宽度是不同的，父元素宽度为auto的时候，子元素也可以拿到宽度。
根据以上概念，所以页面中弹框展示内容可能会造成一个问题，由于没用给弹框设置最大高度，并且内容也没设置任何高度，那么就可能内容将整个页面撑满。
```
