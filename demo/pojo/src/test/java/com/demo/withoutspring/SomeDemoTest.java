
package com.demo.withoutspring;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 21, 2016 5:23:31 PM
 */
public class SomeDemoTest {

    /**
     * Test method for
     * {@link com.demo.withoutspring.SomeDemo#sayHello(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @Test
    public void testSayHello() {
        HttpServletRequest request = EasyMock.createMock( HttpServletRequest.class );
        EasyMock.expect( request.getParameter( "op1" ) ).andReturn( "10" ).anyTimes();
        EasyMock.expect( request.getParameter( "op2" ) ).andReturn( "15" ).anyTimes();
        EasyMock.replay( request );
        HttpServletResponse response = EasyMock.createNiceMock( HttpServletResponse.class );
        EasyMock.replay( response );
        SomeDemo sd = new SomeDemo();
        String result = sd.sayHello( request, response );
        Assert.assertTrue( result.equals( "Hi, the result is: 25" ) );
        EasyMock.verify( request );
    }
}
