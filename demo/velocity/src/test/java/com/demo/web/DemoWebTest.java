
package com.demo.web;

import com.demo.ApplicationTests;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 18, 2016 9:46:24 AM
 */
public class DemoWebTest extends ApplicationTests {

    /**
     * Test method for
     * {@link com.demo.web.DemoWeb#sayHello(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    public void testSayHello() throws Exception {
        HttpServletRequest request = EasyMock.createMock( HttpServletRequest.class );
        EasyMock.expect( request.getParameter( "name" ) ).andReturn( "Eola" ).anyTimes();
        EasyMock.replay( request );
        HttpServletResponse response = EasyMock.createNiceMock( HttpServletResponse.class );
        EasyMock.replay( response );
        DemoWeb dw = new DemoWeb();
        String result = dw.sayHello( request, response );
        Assert.assertTrue( !StringUtils.isEmpty( result ) );
        EasyMock.verify( request );
    }
}
