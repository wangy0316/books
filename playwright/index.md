[如何通过vscode新建一个playwright项目](https://juejin.cn/post/7289740992508215296?searchId=202509151549233DD6BD1F71CCC28F0711)

测试代码都存放在tests文件夹中,文件名必须为xxx.spec.ts

启动测试代码生成器,可将用户操作生成代码:  
npx playwright codegen  
此模式下也可通过pick locator选择元素.

[xpath使用](https://www.w3school.com.cn/xpath/xpath_syntax.asp)

### 选择器
```js
// 选取 class 为 item 的元素
page.locator('.item')

// 选取 id 为 app 的 <div></div> 元素
page.locator('div#app')

// 选择文本(关键字)为 "按钮" 的元素
page.locator('text=按钮')

// 语义化定位 选择button元素,并且文案为提交的元素
page.get_by_role('button', name='提交')

// xpath定位,一种在XML文档中定位节点或节点集的语言,也适用于HTML文档
page.locator('xpath=//button[@type="submit"]')

语义定位
// 元素内部的文字定位元素
page.getByText('首页')

// alt / title 属性定位元素
page.getByAltText('图片')
page.getByTitle('标题');

// 输入框的占位文本定位元素
page.getByPlaceholder('请输入')

// 通过约定好的测试id定位元素
<button data-testid="submit-btn">Button</button>
page.getByTestId('submit-btn')

```

### 语法
```js
// 进入页面, got()方法会等待页面加载完成,单有时候会资源加载错误,这个时候就不会触发domcontentloaded事件,此时需要设置waitUntil: 'networkidle', 只要html解析了,就认为页面加载完成
page.goto('https://www.baidu.com', {waitUntil: 'domcontentloaded'})

// locator表达式匹配多个元素，获取每个元素对应的locator对象，使用 all()
page.locator('div').all()

// 获取元素个数
page.locator('div').count()

// 获取第一个元素,或者使用nth(0)
page.locator('div').first()

// input填充数据
page.locator('input').fill('hello')

// 点击元素
page.locator('button').click()

// 获取元素文本
page.locator('div').textContent()

```
### 定位器断言


### 等待
官网是不推荐使用固定时间的等待,对于api的加载有专门的等待方法.

```js

const promise = page.waitForResponse((res) => res.url().includes('/user_api/v1/author/recommend'));
const res = await promise;
const data = await res.json()

// 等待元素出现
page.locator('div').wait_for()

// 等待元素消失
page.locator('div').wait_for(state='hidden')

// 等待元素被点击
page.locator('button').wait_for(state='visible')

```

### ui组件的测试
```html
<el-select v-model="value1" multiple placeholder="请选择">
  <el-option
    v-for="item in options"
    :key="item.value"
    :label="item.label"
    :value="item.value">
  </el-option>
</el-select>

这是一个基础的select组件,但实际生成的页面却不是这样,而是:

<div class="el-select">
  <div class="el-select__tags" style="width: 100%; max-width: 208px;">
    <span>
      <span class="el-tag el-tag--info el-tag--small el-tag--light">
        <span class="el-select__tags-text">双皮奶</span>
        <i class="el-tag__close el-icon-close"></i>
      </span>
      <span class="el-tag el-tag--info el-tag--small el-tag--light">
        <span class="el-select__tags-text">黄金糕</span>
        <i class="el-tag__close el-icon-close"></i>
      </span>
    </span>
  </div>
  ...
</div>

对于这种复杂但是稳定(基础组件的逻辑是不变的)的特性，需要专门设计代码封装测试行为

1. 在项目中创建一个`components`目录，用于存放组件的测试代码, 如`components/select.ts`.另外,在 tsconfig.json 中的 include 字段中补充此目录

```

