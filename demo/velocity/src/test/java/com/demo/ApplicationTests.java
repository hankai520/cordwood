
package com.demo;

import org.junit.Before;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于 Spring boot 的单元测试基类，需要测试 spring mvc，只需要继承此类即可。
 *
 * @author hankai
 * @version 1.0.0
 * @since Jun 21, 2016 1:29:53 PM
 */
public abstract class ApplicationTests {

    protected HttpServletRequest  mockRequest;
    protected HttpServletResponse mockResponse;

    @Before
    public void setup() {
        initTestFixures();
    }

    private void initTestFixures() {
    }
}
