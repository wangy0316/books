全局配置（main.js）

1. dialog中是否可以通过点击 modal 关闭 Dialog，需要设置close-on-click-modal:false，
   只需要配置Element.Dialog.props.closeOnClickModal.default = false就可实现全局不可关闭
2.

el-table中，如果要给el-table-column分块，必须使用template包裹，并且每个el-table-column必须带上key值，不然数据展示会错位

在循环中如何设置单独的el-popover
通过v-model控制el-popover的显隐，控制显隐使用鼠标事件mouseenter和mouseleave。
