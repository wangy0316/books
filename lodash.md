```javascript

// 数组转对象，id为数组中对象的key， label为数组中对象的值
let params = [{
  id:1,
  label: 'name'
}]
_.mapValues(_.keyBy(params, 'id'), 'label')
// 1: {id:1, label:'name'}
_.keyBy(params, 'id')

// 多数组中相同key值合并数组返回新数组
const all = [...arr, ...arr2]
const result = _(all)
      .groupBy('id')
      .map(_.spread(_.merge))
      .value();
// 如果需要arr2合并到arr中，那么就调整合并的顺序。
// 方法2：将两个数组转为对象，然后合并对象，再将对象转为数组。
const merged = _.merge(_.keyBy(arr, 'id'), _.keyBy(arr2, 'id'));
const values = _.values(merged);

// 用字符的方式获取值，比如获取a[0].c的值
_.get(obj, 'a[0].c')
```
