````js
// 按需引入 Echarts/index.js
import * as echarts from 'echarts/core'
// 按需引入需要的组件模块
import {
  PieChart,
  BarChart,
  LineChart
} from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  DatasetComponent
} from 'echarts/components';
import {
  CanvasRenderer
} from 'echarts/renderers';

echarts.use([
  DatasetComponent,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  PieChart,
  BarChart,
  LineChart,
  CanvasRenderer
]);

export default echarts
````

```

// 图表宽度太小，重设宽度
this.chart.resize()
// 如果页面有多个图表，需要动态id传入。
```
