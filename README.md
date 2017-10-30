# ssm
## 1.spring+mybatis整合：
### 1.1 配置mybatis-config：
主要配置全局设置settring和别名alias，不需要配置mapper所在的链接，因为在spring配置中可以配置进行扫描mapper，必须遵循mapper.xml和mapper.java在同个目录下
### 1.2 spring.xml配置：
spring主要是管理mapper、service、controller的注册，即交给IOC容器
#### 1.2.1 applicationContext-dao.xml：
* 配置加载db.properties；
* 配置数据源；
* 配置sqlSessionFactory（引用的数据源，mybatis-config的位置）
* 配置mapper扫描器
#### 1.2.2 applicationContext-service.xml：
* 配置service扫描包
* 配置service事务
#### 1.2.3 springmvc.xml：
* 配置controller扫描包
* 配置注解驱动
* 配置视图解析器
## 2.web.xml：
* 配置Spring监听器listener，监听spring启动，需要配置spring配置文件的位置
* 配置filter utf-8
* 配置dispatcherServlet，init-param中初始化加载springmvc.xml
