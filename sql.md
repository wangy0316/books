基础写法
SELECTE sql关键字 FROM
SELECTE 列名 FROM

判断语句用 = < >
运算符使用AND OR
排序使用ORDER BY, ASC升序，DESC降序，DISTINCT字母顺序，ID_p数字顺序排序

操作数据语法
UPDATE,DELETE,

高级语法
LIKE 操作符作用于WHERE子语句中，搜索指定类似值，'N%'以N开头, 不区分大小写。'%N'以n结尾。'%n%'包含n
NOT LIKE 不包含
IN 锁定多个值，IN(a,b,c)
BETWEEN...AND 区间值
AS 别名
JOIN多表关联

函数
AVG 平均值, select avg(num) from sys_blog
COUNT 匹配指定条件的数据行数
MAX 返回最大值，null不包含计算
MIN 返回最小值
SUM 求和
UCASE/UPPER 大写
LCASE/LOWER 小写
UCASE和LCASE是简写
LEN/LENGTH 获取长度
ROUND 保留小数位数，四舍五入

