# 笔记

## [regex](https://github.com/wangy0316/books/blob/master/regex/index.md) 正则表达式  

## [es6](https://github.com/wangy0316/books/blob/master/es6/index.md) es6写法


git删除已提交的commit  
git reset --hard SHA文件id  
git push origin HEAD -f  
或者强制将本地推到线上git push --force  
这样就删除了，选择的SHA就是需要保留的提交，只要在SHA上面的记录和文件都删除了。  

git挪动文件位置并保留commit记录  
原文件位置移动到新位置  
git mv src/old_location/file.txt src/new_location/file.txt  
git commit -m "提交"  
git push  

vue文件中，路径带好文件类型，比如xxx.vue,xxx.js。这样可直接点击跳转打开文件

sessionStorage在新打开的页面上可以共享数据,需要通过window.open打开新页面
