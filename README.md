# Background-authority-management-system
这是一款专门针对后台管理系统设计的代码，代码简单，结构清晰，如果你是小白，一定不要错过哦！！！

# 所用到的技术栈
- 后端
 1. SpringBoot2
 2. Hutool
 3. Poi
 4. Mybatis/Mybatis-plus
 5. Spring
 6. SpingMVC
 7. Redis
- 前端
 1. Vue2
 2. Vue-Router
 3. VueX
 4. ElementUI
 5. Axios
 6. Echarts
## 由于本人主java，所以前端描述较少，下面注重讲解下后端Service层的业务逻辑，对于小白来说可以根据业务逻辑来自己练习
### 用户控制器

## pure-design接口事务逻辑

### 用户控制器

- **注册业务逻辑**
  1. 用户名长度不小于2个单位长度，不大于10个单位长度，可以包含特殊字符，不能为空 校验
  2. 密码长度不小于6个单位长度，不大于20个单位长度，不能为空 校验
  3. 从数据库中查询是否存在用户
     1. 不存在，对密码进行加密处理，存入数据库
     2. 存在，抛出异常
  4. 给该用户一个默认的普通用户角色
  5. 存入数据库
- **登录业务逻辑**
  1. 从数据库中查询
  2. 设置token
  3. 获取该用户的角色
  4. 获取该用户角色的菜单权限
  5. 设置该权限并返回
- **修改密码业务逻辑**
  1. 判断修改的密码是否符合要求 校验新密码
  2. 根据原始用户名和密码进行数据库查询
  3. 判断是否存在该用户
     1. 不存在 抛出异常
     2. 存在 对新密码进行加密 设置加密后的密码
  4. 对数据库的该用户进行更新
- **导入业务逻辑**
  1. 获取下载文件的输入流
  2. 获取excel工具类的输入流
  3. 读取excel文件中的对象
     1. 通过javaBean读取，缺点是表头必须是英文，并且要跟javaBean的属性对应起来
     2. 忽略表头的中英文，直接读取表的内容
  4. 读取的数据保存到数据库中
- **导出业务逻辑**
  1. 从数据库中查询相关的所有数据
  2. 通过Excel工具类选择写出到磁盘路径或者浏览器
     1. 可以选择自定义excel每一列标题的别名
     2. 将查询的数据写出到excel的数据流中
  3. 设置浏览器响应的格式
  4. 输出流写到浏览器中

### 文件控制器

- **文件上传业务逻辑**
  1. 获取文件的相关属性
  2. 存储到本地磁盘
     1. 定义文件的标识码---防止文件名重复
     2. 判断创建的相关文件目录是否存在，若不存在，进行创建
     3. 获取文件的md5码---给数据库中设置md5索引
     4. 根据md5查询数据库中是否有相同的资源，若有，获取其url；没有，上传文件到磁盘，并设置其url。目的：节约空间资源，重复避免上传相同的文件
  3. 存储数据库
  4. 返回文件的url
- **文件下载业务逻辑**
  1. 根据文件的唯一标识码获取文件路径
  2. 设置输出流的格式
  3. 通过输出流写出
- **分页查询接口业务逻辑**
  1. 查询出未逻辑删除的数据
  2. 按照id导入排列
  3. 根据文件名模糊查询
  4. MP分页插件

### 角色控制器

- **绑定角色和菜单的关系的业务逻辑**
  1. 先删除当前角色id所有的绑定关系
  2. 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
  3. 判断是否有二级菜单，
     1. 如果传过来的menuId数组里面没有它的父级id 根据menu表要进行判断处理，那么我们就得补上这个父级id并存入数据库
  4. 没有的话设置角色与菜单权限并存入数据库

### 项目截图
![image](https://github.com/CodeTeng/Background-authority-management-system/blob/master/images/Snipaste_2022-03-16_21-58-54.png)
![image](https://github.com/CodeTeng/Background-authority-management-system/blob/master/images/Snipaste_2022-03-16_21-59-13.png)
![image](https://github.com/CodeTeng/Background-authority-management-system/blob/master/images/Snipaste_2022-03-16_21-59-26.png)
![image](https://github.com/CodeTeng/Background-authority-management-system/blob/master/images/Snipaste_2022-03-16_21-59-03.png)
![image](https://github.com/CodeTeng/Background-authority-management-system/blob/master/images/Snipaste_2022-03-16_21-59-19.png)
![image](https://github.com/CodeTeng/Background-authority-management-system/blob/master/images/Snipaste_2022-03-16_21-59-34.png)
![image](https://github.com/CodeTeng/Background-authority-management-system/blob/master/images/S0392Z373FM)HD%7B)MN10NFY.png)
