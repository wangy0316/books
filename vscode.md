```ht
// Emmet的使用
ul>li*3
div{我是div}
li.item$*4
div{$}*4
// css 带冒号就是后面的值，两个拼接写首字母
mr margin-right
pl padding-left
d:n display: none
d:b display: block

// 快捷键
alt+上下方向键 挪动当前行位置
ctrl+shift+p 打开命令行，可输入transform转换选中的字符为大小写
// 配置项
1.修改vscode的分词机制，搜索wordSeparators将-符号删掉，就可双击选择a-b这种词
2.一个文件夹下面只有一个文件夹，那么这两个文件夹会合并展示 "explorer.compactFolders": false
3.静止选择后拖动 "editor.dragAndDrop": false
// 插件
vsc-nvm。项目运行自动执行nvm，能做到不同项目不同node版本。不需要设置统一的node版本。
```

### 文件嵌套展示

```json
// 控制相关文件嵌套展示settings.json
  "explorer.fileNesting.enabled": true,
  "explorer.fileNesting.expand": false,
  "explorer.fileNesting.patterns": {
    "*.ts": "$(capture).test.ts, $(capture).test.tsx, $(capture).spec.ts, $(capture).spec.tsx, $(capture).d.ts",
    "*.tsx": "$(capture).test.ts, $(capture).test.tsx, $(capture).spec.ts, $(capture).spec.tsx,$(capture).d.ts",
    "*.env": "$(capture).env.*",
    "README.md": "README*,CHANGELOG*,LICENSE,CNAME",
    "package.json": "pnpm-lock.yaml,pnpm-workspace.yaml,.gitattributes,.gitignore,.gitpod.yml,.npmrc,.browserslistrc,.node-version,.git*,.tazerc.json",
    "eslint.config.mjs": ".eslintignore,.prettierignore,.stylelintignore,.commitlintrc.*,.prettierrc.*,stylelint.config.*,.lintstagedrc.mjs,cspell.json",
    "tailwind.config.mjs": "postcss.*"
  },
```
