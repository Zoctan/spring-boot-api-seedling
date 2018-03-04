# Spring Boot API Seedling

## 简介

本项目与[种子项目](https://github.com/lihengming/spring-boot-api-project-seed)定位一致：快速构建中小型 API、RESTful API 项目，摆脱重复劳动，专注于业务代码的编写，减少加班。

种子项目本身很简洁，已经能满足很多基本需求，在此感谢种子作者。

我根据自己的需求继续添加了一些小功能，比如 API 的签名认证、调用文档等，所以就有了该 Seedling 项目。

添加的内容包括：
- Spring Cache：缓存
- Redis：缓存中间件
- Swagger2：API 文档展示
- Spring Security + JWT：对调用方签名认证
- Jasypt：加密配置
- Lombok：注解（IDE 需要安装 lombok 插件[idea](https://github.com/mplushnikov/lombok-intellij-plugin/)）
- 其他略

## 快速开始

```
# 克隆项目
git clone https://github.com/Zoctan/spring-boot-api-seedling.git

# 配置代码生成器
对 test/java 包内的代码生成器 CodeGenerator 进行配置
test/resources 目录下有数据库文件 seedDev.sql

# 根据表名生成代码
输入表名，运行 CodeGenerator.main() 方法，生成基础代码（观看[种子项目的快速演示视频](http://v.youku.com/v_show/id_XMjg1NjYwNDgxNg==.html?spm=a2h3j.8428770.3416059.1)）

# last
对开发环境配置文件 application-dev.properties 进行配置，启动项目，Have Fun Too：)
```

## 技术选型&文档

1. Lombok（[官方英文文档](https://projectlombok.org/features/all)）
2. Spring Boot（[种子项目作者的学习&使用指南](https://www.jianshu.com/p/1a9fd8936bd8) | [基础教程](http://blog.didispace.com/Spring-Boot%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)）
3. MyBatis（[官方中文文档](http://www.mybatis.org/mybatis-3/zh/index.html)）
4. MyBatisb通用Mapper插件（[官方中文文档](https://mapperhelper.github.io/docs/)）
5. MyBatis PageHelper分页插件（[官方中文文档](https://pagehelper.github.io/)）
6. Druid Spring Boot Starter（[官方中文文档](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter/)）
7. Fastjson（[官方中文文档](https://github.com/alibaba/fastjson/wiki/Quick-Start-CN) | [W3CShool使用指南](https://www.w3cschool.cn/fastjson/fastjson-quickstart.html)）