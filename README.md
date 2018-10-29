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

编译源代码

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

 

在您的主工程中增加以下第三方依赖，便于编译和运行

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//cordwood-core 所依赖的第三方框架

compile "org.slf4j:slf4j-api:1.7.12"
compile "org.slf4j:jcl-over-slf4j:1.7.12"
compile "org.slf4j:log4j-over-slf4j:1.7.12"
compile "ch.qos.logback:logback-classic:1.1.7"
compile "javax.interceptor:javax.interceptor-api:1.+"

compile "org.springframework:spring-core:4.3.3.RELEASE"
compile "org.springframework:spring-context:4.3.3.RELEASE"
compile "org.springframework:spring-beans:4.3.3.RELEASE"
compile "org.springframework:spring-webmvc:4.3.3.RELEASE"
compile "org.springframework:spring-context-support:4.3.3.RELEASE"
compile "org.springframework.security:spring-security-web:4.1.3.RELEASE"
compile "org.springframework.security:spring-security-config:4.1.3.RELEASE"
compile "javax.servlet:javax.servlet-api:3.1.0"
compile "commons-io:commons-io:2.2"
compile "com.fasterxml.jackson.core:jackson-databind:2.4.6"
compile "commons-codec:commons-codec:1.9"
compile "org.apache.commons:commons-lang3:3.4"
compile "net.sf.ehcache:ehcache:2.10.3"
compile "com.google.guava:guava:19.0"

testCompile "junit:junit:4.12"
testCompile "com.jayway.jsonpath:json-path-assert:0.8.1"
testCompile "org.easymock:easymock:3.1"
testCompile "com.google.inject:guice:4.0"
testCompile "org.springframework:spring-test:${springVersion}"
testCompile "org.yaml:snakeyaml:1.17"
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//cordwood-data 所依赖的第三方框架

compile "commons-io:commons-io:2.2"
compile "commons-codec:commons-codec:1.9"
compile "org.apache.commons:commons-lang3:3.4"

compile "org.springframework.data:spring-data-jpa:1.11.10.RELEASE"
compile "org.eclipse.persistence:eclipselink:2.5.0"
compile "com.alibaba:druid:1.1.10"
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//cordwood-mobile 所依赖的第三方框架

compile "commons-io:commons-io:2.2"
compile "commons-codec:commons-codec:1.9"
compile "org.apache.commons:commons-lang3:3.4"
compile "com.googlecode.plist:dd-plist:1.16"
compile "net.dongliu:apk-parser:2.1.2"
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

在您的主工程中依赖 cordwood（根据需要依赖 core, data 或者mobile）

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
compile files("../libs/cordwood-core-0.0.1.RELEASE.jar")
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

在您的主工程中，根构建路径下新建 support 目录，添加 system.yml
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

通过 ren.hankai.cordwood.core.util.RuntimeVariables
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
    //..自定义缓存配置，可配置 redis, memcache, ehcache 等，默认采用 ehcache
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

通过为需要缓存的方法签名增加缓存注解，实现缓存。框架采用 ehcache
2实现缓存机制，根据经验，对需要缓存的方法划分了三个量级：轻量级、中量级、重量级。缓存级别的区别主要是缓存时长，缓存数据量。

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//使用 ren.hankai.cordwood.core.cache 包下的缓存注解

@HeavyWeight //重量级缓存
public String hello() {...}

@MiddleWeight //中量级缓存
public String hello() {...}

@LightWeight //轻量级缓存
public String hello() {...}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

通过继承
ren.hankai.cordwood.core.config.CoreCacheConfig，重写下面的方法，可以自定义各个级别缓存的配置：

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public CacheManager cacheManager() {...}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

下面是 CoreCacheConfig 类部分源代码：

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//ren.hankai.cordwood.core.config.CoreCacheConfig 源代码示例：

final CacheConfiguration heavyWeightCache = new CacheConfiguration();
heavyWeightCache.setName("heavyWeight"); // 定义重量级缓存
heavyWeightCache.setMaxEntriesLocalHeap(4000);
heavyWeightCache.setTimeToIdleSeconds(60 * 60 * 24); // 缓存1天
// heavyWeightCache.setMemoryStoreEvictionPolicy("LRU"); // One of "LRU", "LFU" or "FIFO".
heavyWeightCache.persistence(pc);
config.addCache(heavyWeightCache);
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

### 访问限流、熔断

框架内部采用 Google Guava 的 RateLimiter
类实现基于令牌通的限流器。支持对QPS进行控制，基于限流器超时，实现熔断机制，熔断后，将返回HTTP代码
503 。

 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//核心接口及默认实现
ren.hankai.cordwood.web.security.AccessLimiter //访问限制器
ren.hankai.cordwood.web.security.support.DefaultAccessLimiter //默认访问限制器实现
ren.hankai.cordwood.web.security.support.RateLimitInfo //限流信息类，用于缓存限流配置
ren.hankai.cordwood.web.pfms.StabilizationInterceptor //访问限流拦截器
ren.hankai.cordwood.web.security.annotation.Stabilized //限流器配置注解

//使用步骤
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
...
    //1. 在自定义的 MVC 配置类中，注入 StabilizationInterceptor：
    @Autowired
    private StabilizationInterceptor stabilizationInterceptor;
...

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ...
        //2. 配置拦截器，拦截所有请求。也可根据需要拦截部分请求。注意拦截器一定要配置在最前面
        //否则可能因为其他拦截器干预，导致限流器不工作
        registry.addInterceptor(stabilizationInterceptor).addPathPatterns("/**");
        ...
    }
...
}

//3. 为所有控制器创建基类便于统一限流配置，基类可继承框架提供的 WebServiceSupport。

@Stabilized //4. 为基类增加注解，启用限流配置
public abstract class CustomWebServiceSupport extends WebServiceSupport {
    ...
}

//可根据需要配置 Stabilized 注解中的参数，具体如下：
maxQps: 最大QPS（Query per second），默认 100
timeout: 请求受理的超时时长（毫秒），默认1000
warmupPeriod: 热身时长（毫秒），详见源代码，默认2000
fusingThreshold: 熔断阈值，默认3
fusingInterval: 熔断恢复间隔，默认5000
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

应用入口类示例
--------------

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
