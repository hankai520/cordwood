
package com.demo;

import com.demo.plugins.config.PluginBootstrap;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 基于 Spring boot 的单元测试基类，需要测试 spring mvc，只需要继承此类即可。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 21, 2016 1:29:53 PM
 */
@RunWith( PluginJunitRunner.class )
@ContextConfiguration(
    classes = { PluginBootstrap.class } )
@WebAppConfiguration
@ActiveProfiles( { "unit-test" } )
public abstract class ApplicationTests {

    @Autowired
    protected WebApplicationContext ctx;
    protected MockMvc               mockMvc;

    @Before
    public void setUpMVC() {
        mockMvc = MockMvcBuilders.webAppContextSetup( ctx ).build();
        initTestFixures();
    }

    private void initTestFixures() {
    }
}
