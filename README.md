# spring-boot-api-fast

## 简介

[项目骨架](https://github.com/lihengming/spring-boot-api-project-seed)

本项目定位与使用的骨架定位一致：快速构建中小型API、RESTful API项目，摆脱重复劳动，专注于业务代码的编写，减少加班。

fast主要在骨架基础上继续添加了：
- Spring Cache：缓存
- Redis：缓存中间件
- Swagger2：API文档展示
- Spring Security + JWT：对调用方签名认证
- Jasypt：加密配置
- Lombok：注解（IDE需要安装lombok插件[idea](https://github.com/mplushnikov/lombok-intellij-plugin/)）

项目内部包含了最基本的角色认证，主要参考了[CSDN博客](http://blog.csdn.net/u012373815/article/details/54632176)。

## 快速开始

1. clone项目
2. 对test/java包内的代码生成器CodeGenerator进行配置，主要是JDBC，因为要根据表名来生成代码
3. test/resources目录下有开发用的数据库fastdev.sql
4. 输入表名，运行CodeGenerator.main()方法，生成基础代码（观看[骨架的快速演示视频](http://v.youku.com/v_show/id_XMjg1NjYwNDgxNg==.html?spm=a2h3j.8428770.3416059.1)）
5. 对开发环境配置文件application-dev.properties进行配置，启动项目，Have Fun：)

## 技术选型&文档

1. Spring Boot（[骨架作者的学习&使用指南](https://www.jianshu.com/p/1a9fd8936bd8) | [基础教程](http://blog.didispace.com/Spring-Boot%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)）
2. MyBatis（[官方中文文档](http://www.mybatis.org/mybatis-3/zh/index.html)）
3. MyBatisb通用Mapper插件（[官方中文文档](https://mapperhelper.github.io/docs/)）
4. MyBatis PageHelper分页插件（[官方中文文档](https://pagehelper.github.io/)）
5. Druid Spring Boot Starter（[官方中文文档](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter/)）
6. Fastjson（[官方中文文档](https://github.com/alibaba/fastjson/wiki/Quick-Start-CN) | [W3CShool使用指南](https://www.w3cschool.cn/fastjson/fastjson-quickstart.html)）
7. Lombok（[官方英文文档](https://projectlombok.org/features/all)）