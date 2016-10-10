
package com.demo;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 插件测试运行器，用于初始化操作
 *
 * @author hankai
 * @version 1.0
 * @since Oct 10, 2016 10:54:49 AM
 */
public class PluginJunitRunner extends SpringJUnit4ClassRunner {

    public PluginJunitRunner( Class<?> clazz ) throws InitializationError {
        super( clazz );
    }
}
