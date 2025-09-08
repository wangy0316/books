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
9. clip-path
   ```js
    <div class="img-box">
      <svg width="0" height="0">
        <defs>
          <clipPath id="clipPath">
            <path transform="scale(2)" d="" />
          </clipPath>
        </defs>
      </svg>
      <img src="" style="clip-path: url(#clipPath)" />
    </div>
    // svg放大缩小可通过transform去处理，使用svg的width和height以及viewBox是无效的
    // 如何解决hover状态下，如果svg是异形的会导致hover效果抖动？
    // 解决方案：hover状态添加到div中，而不是img中。
    .img-box{
      &:hover{
        img{
          clip-path: url(#clipPath)
        }
      }
    }
    // clip-path: circle(radio at x y) radio: 半径，x y: 距离圆心坐标
    // 200px at 0 0代表：圆心在左上角，半径为200px
    // 200px at 0 100%代表：圆心在左下角，半径为200px
   ```
11. css动态变量：

    ```js
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
12. grid布局

13. gspa动画库, [基础概念](http://www.lixianglong.cn/2024/07/30/application/fore-end/threejs/GSAP%E5%8A%A8%E7%94%BB%E5%8F%82%E7%85%A7%E8%AF%A6%E8%A7%A3/)

    ```js
    import gsap from "gsap";
    import ScrollTrigger from "gsap/ScrollTrigger";
    gsap.registerPlugin(ScrollTrigger)
    // 引入ScrollTrigger
    gsap
      .timeline({
        scrollTrigger: {
          trigger: dom,     // 引入滑动效果的块
          start: 'top 70%', // 滚动条距离trigger的位置,触发动画两个值，一个是动画开始的位置，一个是动画的锚点
          end: 'top 70%',     // 和start一样使用量值，一个结束位置，一个锚点
          pin: dom,             // 固定元素, 可以使用".class", "#id"等的选择器文本
          scrub: 1,         // 将动画的进度直接链接到滚动条
          markers: true, // 在开发/故障添加标记, 可对象形式 { startColor: "red", endColor: "blue" }
          toggleActions: 'play none reverse none' // 滚动条触发动画的四个状态，play, pause, resume, restart
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
      .to(".class", { x: 100 }, "<") 这里的<表示插入到动画的开头。如果是>表示插入到动画的结尾
      .to(".class", { x: 100 }, "<+0.5") 表示在动画开始0.5s后开始动画.
      例如：
        tl.to(".box", {x: 100, duration: 1});
        tl.to(".box", { y: 100 }, "<");
      这个动画效果就是一边向右移动，一边向下移动，右移动会持续一秒钟，但是向下是一瞬间。
    */
    /**
     * fromTo()：自定义起始值和结束值，这对于完全控制动画非常有用，尤其是当它与其他动画链接时
     * formTo('dom', {},{}) 两个对象中是起始和结束位置的属性
     */
    // Staggers（交错动画）
    // 如何设置动画的结束后启动另外一个动画

    ```

```
作为常识大家要清楚html,body默认也是height:auto的，通常都是计算完子元素的高度后才计算父元素的高度。
默认情况：普通文档流，父元素height：auto 这种情况下，父元素也就是body，html高度均为自动，子元素高度设置height:100%无任何效果，原因也很简单。auto\*100% 无法计算，当然是0。这点与宽度是不同的，父元素宽度为auto的时候，子元素也可以拿到宽度。
根据以上概念，所以页面中弹框展示内容可能会造成一个问题，由于没用给弹框设置最大高度，并且内容也没设置任何高度，那么就可能内容将整个页面撑满。
```
