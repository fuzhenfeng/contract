# contract

## contract是什么？
contract是对领域驱动开发的一次实践。

## 为什么要做contract？
看过一些领域驱动的书籍、博客、视频，内容差异不大，理论居多，没有很强的指导性，希望contract的实践带来一些指导性。
路不怕走错，错误是必经之路，希望contract的错误反推或者补充领域驱动的理论。

## contract怎么做？
contract约定了工程结构和模块指责

### 框架（contract）
```
com.xxx.controller
    xxxController
com.xxx.controller.component
    xxxComponent
com.xxx.service
    xxxService
    
com.xxx.service
    xxxService
com.xxx.Domain
    xxxDomain
    
com.xxx.repository
    xxxRepository
com.xxx.repository.dao
    xxxDao
``` 

### 领域（contract）
```
com.xxx.service.service
    xxxDomain
com.xxx.service.service.entity
    xxxEntity
```

### 设计思想或原则（contract）
* 不重复造轮子：充分利用现有的框架去构建，除非有明显缺陷
* 面向接口开发：模块、领域、组件之间面向接口开发，其它保持简单，不做要求
* 面向约定开发：遵循最常用的工程配置（maven工程结构）、通信协议（HTTP）
* KISS原则：只做最小设计，只做本地迭代的任务，不做未来的设计，包括接口的扩展和组件内部的扩展
* 产品：（不做要求）
* 编码：遵循常用编码规范
* 注释：接口必写注释，其它保持简单，不做要求
* 测试：单元测试、黑盒测试

## 使用的工具（contract）

### 标准
* servlet api(undertow)
* jpa api(mybatis)
* jms api(rocketmq)
* slf4j api(log4j2)
* jmx api

### 非标准
* ioc api
* task api(kotlin)
* cache api(guava)
* security api()
* test api(junit)
* doc api(spring rest docs)
* config api(zookeeper)

### 推荐
* json api(gson)
* xml api(dom4j)
