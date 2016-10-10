
package com.demo.plugins.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.ApplicationTests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 9, 2016 4:49:18 PM
 */
public class HelloTest extends ApplicationTests {

    @Test
    public void testAdd() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put( "op1", "12" );
        params.put( "op2", "11" );
        MvcResult result = mockMvc.perform(
            post( "/hello/add" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                .param( "op1", params.get( "op1" ) )
                .param( "op2", params.get( "op2" ) ) )
            .andExpect( status().isOk() )
            .andDo( print() )
            .andReturn();
        String response = result.getResponse().getContentAsString();
        Assert.assertEquals( "23", response );
    }
}
