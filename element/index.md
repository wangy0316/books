全局配置（main.js）

1. dialog中是否可以通过点击 modal 关闭 Dialog，需要设置close-on-click-modal:false，
   只需要配置Element.Dialog.props.closeOnClickModal.default = false就可实现全局不可关闭
2.

el-table中，如果要给el-table-column分块，必须使用template包裹，并且每个el-table-column必须带上key值，不然数据展示会错位

el-menu中，不要给el-submenu添加popper-append-to-body属性，不然当鼠标从二级菜单挪出后不会关闭一级el-submenu菜单。

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
