全局配置（main.js）

1. dialog中是否可以通过点击 modal 关闭 Dialog，需要设置close-on-click-modal:false，
   只需要配置Element.Dialog.props.closeOnClickModal.default = false就可实现全局不可关闭
2.

el-table中，如果要给el-table-column分块，必须使用template包裹，并且每个el-table-column必须带上key值，不然数据展示会错位

el-table中，表头和内容发生错位情况，引入一下css。需要全局

```js
.el-table th.gutter{
  display: table-cell !important
}
```

el-menu中，不要给el-submenu添加popper-append-to-body属性，不然当鼠标从二级菜单挪出后不会关闭一级el-submenu菜单。去掉如果右边空间不足，菜单不会自动展示在左侧。

在循环中如何设置单独的el-popover
通过v-model控制el-popover的显隐，控制显隐使用鼠标事件mouseenter和mouseleave

```
<span @mouseenter="onMouseEnter($event)"  @mouseleave="onMouseLeave">popover</span>
<el-popover
 ref="popover"
 v-model="openPopover"
 placement="bottom-start"
 width="100"
 trigger="hover">
 <div class="dept-popover"></div>
</el-popover>
onMouseEnter(e, item, ele){
   this.openPopover = false;
   this.$refs.popover.doDestroy()
   this.$nextTick(() => {
     this.$refs.popover.referenceElm = e.target;
     this.openPopover = true;
   });
},
onMouseLeave(){
   this.$nextTick(() => {
     this.$refs.popover.doClose()
   });
}
```

table项合并

```
// key是table展示的字段，target是合并字段，有时候需求中合并字段不一定是展示字段。比如第二列是根据第一列合并的。
const mergeKeys = [
  {key: 'a', target: 'b'
]
const getMergeRowspan(arr, keys){
  let rowspan = {}
  let row = 0
  const { key, targetKey} = keys
  const list = deepClone(arr)
  const length = list.length
  const [a,b,c] = targetKey.split(',')
  for(let i = 0; i<length; i++){
    const item = list[i]
    const index = i
    if(index == 0){
      rowspan[key] = [1]
      row = 0
      continue
    }
    const target = item[a] || item[b] || item[c]
    const lastKeyValue = list[index-1][a] || list[index-1][b] || list[index-1][c]
    if(target === lastKeyValue){
      rowspan[key][row] +=1
      row[key].push(0)
    }else{
      row[key].push(1)
      row = index
    }
  })
  return rowspan
}

let cell = {}
mergeKeys.forEach(keys => {
  const rowspan = getMergeRowspan(arr, keys)
  Object.assign(cell, rowspan)
})

spanMethod({row, column, rowIndex, columnIndex}{
   const tableRowkey = column['property']
   const rows = cell[tableRowkey]
   if(rows){
      // rowspan: 5 ,意味着前五个合并
      return {
        rowspan: rows[rowIndex],
        colspan: rows[rowIndex] > 0 ? 1 : 0
      }
   }
}
```
