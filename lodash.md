// 数组转对象，id为数组中对象的key， label为数组中对象的值  
```
let params = [{
  id:1,
  label: 'name'
}]
_.mapValues(_.keyBy(params, 'id'), 'label')
```
