## 1.0.0
**初始化项目**
- spring boot 使用 2.1.9.RELEASE
- 添加华为 maven 镜像

## 1.0.1
**添加Web模块**
- 添加 spring-boot-starter-web
- 删除 spring-boot-starter (spring-boot-starter-web 中已包含)

**依赖说明**

spring-boot-starter-web 包含以下依赖
![](https://raw.githubusercontent.com/wqlm/common/master/boot.png)

- spring-boot-starter
- spring-boot-starter-tomcat
- spring-webmvc
- spring-web
- spring-boot-starter-json (有了它，就可以使用 @ResponseBody 返回 json 数据)
    - jackson
- hibernate-validator(提供参数校验注解)
    - javax.validation (提供参数校验注解)
