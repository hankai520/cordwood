Cordwood
========

cordwood 是一个基于 Spring-boot 的 Web 应用代码级快速开发框架。

 

目录结构
--------

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
cordwood
    - doc //文档
    - LICENSE //许可证
    - README.md //本文档
    - source //cordwood核心代码
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

工程结构
--------

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
cordwood
    - cordwood-core //核心类库
    - cordwood-data //数据持久化
    - cordwood-mobile //移动端工具库
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

依赖关系
--------

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
cordwood-mobile

cordwood-data
    - cordwood-core
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

集成框架
--------

1.  下载框架源代码

2.  编译源代码

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//linux shell
cd source
chmod +x ./gradlew
./gradlew build -x test

//windows cmd
cd source
gradlew.bat build -x test

编译结果位于 cordwood-core/build/libs （其他模块编译结果在类似位置）

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

3. 在您的主工程中增加以下第三方依赖，便于编译和运行

 

cordwood-core 所依赖的第三方框架：

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
compile "org.slf4j:slf4j-api:1.7.12"
compile "org.slf4j:jcl-over-slf4j:1.7.12"
compile "org.slf4j:log4j-over-slf4j:1.7.12"
compile "ch.qos.logback:logback-classic:1.1.7"
compile "javax.interceptor:javax.interceptor-api:1.+"

testCompile "junit:junit:4.12"
testCompile "com.jayway.jsonpath:json-path-assert:0.8.1"
testCompile "org.easymock:easymock:3.1"
testCompile "com.google.inject:guice:4.0"
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

cordwood-data 所依赖的第三方框架

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
compile "commons-io:commons-io:2.2"
compile "commons-codec:commons-codec:1.9"
compile "org.apache.commons:commons-lang3:3.4"

compile "org.springframework.data:spring-data-jpa:1.11.10.RELEASE"
compile "org.eclipse.persistence:eclipselink:2.5.0"
compile "org.apache.tomcat:tomcat-jdbc:8.5.5"
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

cordwood-mobile 所依赖的第三方框架

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
compile "commons-io:commons-io:2.2"
compile "commons-codec:commons-codec:1.9"
compile "org.apache.commons:commons-lang3:3.4"
compile "com.googlecode.plist:dd-plist:1.16"
compile "net.dongliu:apk-parser:2.1.2"
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

3. 在您的主工程中依赖 cordwood（根据需要依赖 core, data 或者mobile）

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
compile project(":jsgs-core")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

框架功能
--------

 

### 应用数据读写

框架提供预定义的应用数据目录，用于读写应用数据。以下为标准的应用数据目录结构：

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//假设 /opt/app/data 为数据根目录，以下目录均相对于此目录

cache //缓存目录
config //配置文件目录
data //用户数据目录
    attachment //用户附件目录
    backups //备份目录
    database //数据库目录（e.g. 嵌入式数据库）
    plugins //插件（预留目录，用于实现插件架构）
    runtime.properties //运行时配置文件
libs //第三方类库目录
logs //日志目录
temp //临时目录
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

### 应用配置参数

在您的主工程中，构建根路径下，添加 system.yml
文件，用于配置系统参数，以下为标准配置。

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#系统秘钥
systemSk: 46ee56cd32f85727

#数据传输秘钥
transferKey: 8dffab6aafc8ebc974bd82364ef9516b

#API 默认鉴权码有效时长（天）
apiAccessTokenExpiry: 7

#要绑定的域名
proxyName: 

#要绑定的端口
proxyPort: 

#要绑定的协议
proxyScheme: 
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

配置的参数通过 ren.hankai.cordwood.core.Preferences 类读取:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//读取 systemSk
public static String getSystemSk();
//读取 transferKey
public static String getTransferKey();
...
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

### 运行时参数

通过

ren.hankai.cordwood.core.util.RuntimeVariables
类，可以配置运行时参数，参数支持热修改，因此适用于配置允许在运行时动态调整的参数。参数基于键值对，使用
properties 文件进行持久化，内存中使用 HashMap 来存储键值。

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//设置参数
public static void setVariable(String key, String value);

//获取参数
public static String getVariable(String key);
public static boolean getBool(String key);
public static int getInt(String key);
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

### Web Service 支持

ren.hankai.cordwood.core.api.support 包下面提供了一组用于开发 Web Service
的基础类：

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
ApiCode //提供了一组标准的接口相应代码，用于返回接口处理结果代码
ApiResponse //提供 Web Service 相应内容包装类
WebServiceSupport // Web Service 基类，处理异常，转义为标准的响应

//标准 Web Service 响应格式（以 JSON 为例）
{
    "code": 1,
    "message": "调试信息",
    "body": {
        "success": true,
        "error": "错误代码",
        "message": "用于调试的信息",
        "data": {
            ... //响应数据区域
        }
    }
}

code: 服务响应代码，侧重于服务执行结果而不是业务执行结果
message: 用于调试的信息，例如返回服务器的一些参数信息，生产环境可通过设置 api.allow.debug 运行时参数来关闭
body.success: 业务是否执行成功
body.error: 业务逻辑错误代码，例如密码错误，用户不存在
body.message: 业务调试信息，例如返回订单号啥的
body.data: 响应的数据
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

### 缓存

通过继承 ren.hankai.cordwood.core.config.CoreCacheConfig
并开启缓存，即可启用默认缓存配置（spring-boot 默认缓存配置）:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@Configuration
@EnableCaching
public class CacheConfig extends CoreCacheConfig {
    //..自定义缓存配置，可配置 redis, memcache, ehcache 等
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

### 示例

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public static void main(String[] args) {

    final String[] configs = {
        "mysql.properties", "keystore.jks",
        "system.yml", "i18n.properties"};

    final ApplicationInitInfo initInfo = ApplicationInitInfo.initWithConfigs(configs);
    final String[] templates = { //模板文件
        "simsun.ttc",
        "1.html", "2.html", "3.html", "4.html", "5.html"
    };
    initInfo.addTemplates(templates);

    //初始化应用
    if (ApplicationInitializer.initialize(false, initInfo)) {
      RuntimeVariables.setCacheSeconds(10); //设置运行时配置缓存10秒

      //... 配置运行时参数的初值

      RuntimeVariables.saveVariables(); //保存运行时参数，生成 runtime.properties
      if (SpringApplication.run(Application.class, args) != null) {
        logger.info("Application started successfully!");

        //设置日志过滤，将警告及更高级别日志单独写入 errors.txt
        LogbackUtil.setupFileLoggerFor(Level.WARN, "errors.txt");
      }
    }
  }
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 
-
