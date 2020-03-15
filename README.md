# tkmybatis-generator
基于tkmybatis的mybatis-generator生成器，支持Lombok、swagger、po的基类

### 背景
SSM 框架在现在互联网企业JAVA选型中已经相当成熟, 随着微服务的架构思想提出,spring-cloud生态也是逐渐火热,但是持久层的框架Mybatis依旧占据重要地位.
在日常开发中持久层我们会借助一些工具来帮忙我们生成, mybatis官方也提供了相当好的工具mybatis-generator, 本tkmybatis-generator是基于 maven版
本的mybatis-generator/tkmybatis-generator进行二次封装

### 对原生mybatis-generator的改动点
1. 对xml && dao注释的优化
2. 将数据库的注释映射到实体类中，实体类继承了基类，如果基类中存在通用字段，可支持子类忽略基类的字段
3. 实体支持swagger注解生成
4. 实体支持lombok，减少getter、setter的代码


### tkmybatis-generator在工程中使用步骤
1.创建Spring Maven 项目, 添加Pom依赖(pom.xml)
```xml
<dependencies>
    <dependency>
        <groupId>tk.mybatis</groupId>
        <artifactId>mapper-spring-boot-starter</artifactId>
        <version>2.0.4</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-core</artifactId>
        <version>1.3.5</version>
    </dependency>
</dependencies>    
```

2.添加maven打包插件(pom.xml)
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.suncht.generator</groupId>
            <artifactId>tkmybatis-generator-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <configurationFile>src/main/resources/generator/mybatis-code-generator.xml</configurationFile>
                <overwrite>true</overwrite>
                <verbose>true</verbose>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.44</version>
                </dependency>
                <dependency>
                    <groupId>tk.mybatis</groupId>
                    <artifactId>mapper</artifactId>
                    <version>4.0.4</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>

```

3.在source资源文件夹下面创建mybatis-code-generator.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat">
        <!--处理sql中的`符号-->
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <!--添加自定义的继承接口-->
        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="com.suncht.wl.vsc.framework.dao.BaseMapper"/>
        </plugin>
        <plugin type="com.suncht.tkmybatis.generator.maven.plugin.DomainLombokPlugin">
            <property name="useSwagger" value="true"/>
            <property name="ignoreFields" value="id,createUser,updateUser,createTime,updateTime,ts,yn"/>
        </plugin>
        <plugin type="com.suncht.tkmybatis.generator.maven.plugin.BaseColumnListPlugin" />
        <plugin type="com.suncht.tkmybatis.generator.maven.plugin.AutoGenXmlPlugin" />
        <plugin type="com.suncht.tkmybatis.generator.maven.plugin.ParentMapperPlugin" />

        <!--数据源配置-->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://127.0.0.1:3306/vsc_sim_scot?allowMultiQueries=true"
                        userId="root"
                        password="root">
        </jdbcConnection>

        <!--model包路径-->
        <javaModelGenerator targetPackage="com.suncht.stocksim.web.repository.mysql.datado" targetProject="src/main/java">
            <property name="rootClass" value="com.suncht.stocksim.web.repository.mysql.datado.BaseSimPo" />
        </javaModelGenerator>
        <!--mapper.xml包路径-->
        <sqlMapGenerator targetPackage="mybatis" targetProject="src/main/resources"/>
        <!--mapper包路径-->
        <javaClientGenerator targetPackage="com.suncht.stocksim.web.repository.mysql.mapper" targetProject="src/main/java"
                             type="XMLMAPPER"/>

       <!-- 表配置，tableName支持%，表示全部生成-->
        <table tableName="sim_project" domainObjectName="SimProject">
            <!--mysql 配置-->
            <generatedKey column="id" sqlStatement="Mysql" identity="true"/>
        </table>
        
    </context>
</generatorConfiguration>
```
5.mybatis-code-generator.xml中的自定义插件的功能
- tk.mybatis.mapper.generator.MapperPlugin：生成mapper类的tkmybatis自定义的mapper基类
- com.suncht.tkmybatis.generator.maven.plugin.DomainLombokPlugin： Po实体类支持Lombok，useSwagger让实体支持Swagger注解， ignoreFields忽略实体某些字段，可处理实体基类中存在的公用字段
- com.suncht.tkmybatis.generator.maven.plugin.BaseColumnListPlugin：处理带as字段
- com.suncht.tkmybatis.generator.maven.plugin.AutoGenXmlPlugin：将xml、mapper的可变、不可变部分的分离
- com.suncht.tkmybatis.generator.maven.plugin.ParentMapperPlugin：处理mapper的通用基类的情况

6.至此我们运行generator插件 || mvn命令运行就会生成我们的持久层类, 在target/ 目录下
```bash
mvn mybatis-generator:generate
```
