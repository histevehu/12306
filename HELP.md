# 常见问题汇总

### generator代码生成器运行报错，找不到member中的枚举类

generator模块依赖member模块, member模块依赖common模块。所以先编译 common，再编译 member， 就可以执行generator了。

### MyBatis中的一级缓存

当一个事务中发起多次同样的SQL查询时，后续将从MyBatis的缓存中读取。若非事务，则每次查询都会发起新的查询会话。

注意，一级缓存默认开启。若事务中两次相同之间涉及数据的修改，则后面几次读取的数据仍为缓存中修改前的数据，这将导致错误。解决方法：

- 可以通过配置`mybatis.configuration.local-cache-scope=statement`由默认的session改为statement，即关闭一级缓存。

### MyBatis中的二级缓存

MyBatis二级缓存中不同的 mapper, 即使操作的是同一张表，对应的缓存也是两块区域。当对同个namespace的mapper做增删改操作时，二级缓存就会清空（即使增删改的 SQL 没有改变数据， Mybatis 都会将同个命名空间下的二级缓存清空）。

MyBatis的二级缓存默认关闭。若要开启，需要：

- 相关mapper中新增`<cache></cache>`
- mapper中返回结果的实体类实现Serializable接口以序列化。当一个类需要保存起来，下次再还原成类时就需要序列化，或者需要远程传输，比如放到redis里，也需要序列化

MyBatis二级缓存缺点：

- 若涉及多读多写的场景则二级缓存近乎失效
- 二级缓存存在本地，若有多台服务器会发生数据不一致的问题

因此实际开发中很少使用MyBatis二级缓存。

### Spring内置缓存

Springboot本地缓存可以解决MyBatis二级缓存需要修改mapper的问题，但同样存在多个节点缓存不一致的问题，且缓存无过期策略，只能手动刷新。若要开启，需要：

- Spring Application上@EnableCaching

- 需要缓存的服务方法上@Cacheable

  > #### @Cacheable和@CachePut区别
  >
  > 一、相同点
  > 都是Spring的缓存注解
  >
  > 二、不同点
  > @Cacheable：只会执行一次，当标记在一个方法上时表示该方法是支持缓存的，Spring会在其被调用后将其返回值缓存起来，以保证下次利用同样的参数来执行该方法时可以直接从缓存中获取结果。
  > @CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。