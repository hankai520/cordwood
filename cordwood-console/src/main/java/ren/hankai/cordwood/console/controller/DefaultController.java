
package ren.hankai.cordwood.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author hankai
 * @version 0.0.1
 * @since Sep 28, 2016 11:19:57 AM
 */
@Controller
public class DefaultController {

    @Autowired
    private EmbeddedWebApplicationContext context;
    @Autowired
    private RequestMappingHandlerMapping  requestMappingHandlerMapping;

    @RequestMapping( "/" )
    @ResponseBody
    public String home() throws Exception {
        URL url = new URL(
            "file:/Users/hankai/Documents/devops/projects/cordwood/cordwood-plugins/build/libs/cordwood-plugins-0.0.1.RELEASE.jar" );
        URLClassLoader loader =
                              new URLClassLoader( new URL[] { url }, context.getClassLoader() );
        ClassPathResource resource = new ClassPathResource( "beans.xml", loader );
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader( context );
        xmlReader.setBeanClassLoader( loader );
        xmlReader.loadBeanDefinitions( resource );
        requestMappingHandlerMapping.afterPropertiesSet();
        return "hello";
    }
}
