通过ctrl+鼠标左键点击跳转到对应的文件
./* 表示当前目录下的文件，@/* 表示src目录下的文件
```json
{
  "compilerOptions": {
    "baseUrl": "./",
    "paths": {
      "@/*": ["./*"]
    }
  }
}
```