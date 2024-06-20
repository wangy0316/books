# 笔记

## [regex](https://github.com/wangy0316/books/blob/master/regex/index.md) 正则表达式  

## [es6](https://github.com/wangy0316/books/blob/master/es6/index.md) es6写法

// 回退代码不推荐reset，当多人迭代，其它人的提交会把删除的记录提交，而且不会出现变更提示。
revert回退代码

git挪动文件位置并保留commit记录  
原文件位置移动到新位置  
git mv src/old_location/file.txt src/new_location/file.txt  
git commit -m "提交"  
git push  

已经使用git commit -m 'xxx'将代码提交到了本地仓库，但是我后续还想向这个提交中添加文件
首先将你想添加到文件使用git add xxx加入暂存区
git commit --amend 会打开一个编辑器，让你编辑上一次提交的提交信息

vue文件中，路径带好文件类型，比如xxx.vue,xxx.js。这样可直接点击跳转打开文件

sessionStorage在新打开的页面上可以共享数据,需要通过window.open打开新页面
