# mybatis-pagehelper-spring-boot-starter
提供MyBatis 基于 PageHelper 分页的Mapper 接口 Dorado 分页支持。
MyBatis 自带的分页对象是RowBounds，PageHelper的是PageRowBounds，而 dorado 提供的分页对象是 Page。
该项目通过拦截器，动态修改 dorado Page 为 PageRowBounds，并将查询结果回写到 Page 。

###示例

```
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bstek.dorado.data.provider.Page;
import org.xobo.demo.entity.User;

@Mapper
public interface DemoMapper {
  List<User> queryUsers(Page<User> page, Map<String, Object> parameterMap);
}
```

### maven 项目引入

```XML
<dependency>
  <groupId>org.xobo.dorado</groupId>
  <artifactId>mybatis-pagehelper-spring-boot-starter</artifactId>
  <version>0.0.1</version>
</dependency>
```
