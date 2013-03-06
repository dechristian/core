package org.richfaces.integration.push;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.integration.CoreDeployment;
import org.richfaces.webapp.PushServlet;

import com.google.common.base.Function;

@RunWith(Arquillian.class)
@WarpTest
public class CustomPushServletMappingTest extends AbstractPushTest {

    @Deployment
    public static WebArchive createDeployment() {
        CoreDeployment deployment = createBasicDeployment(CustomPushServletMappingTest.class);

        deployment.webXml(new Function<WebAppDescriptor, WebAppDescriptor>() {
            public WebAppDescriptor apply(WebAppDescriptor webXml) {
                webXml.createServlet()
                            .servletName(PushServlet.class.getSimpleName())
                            .servletClass(PushServlet.class.getName())
                            .asyncSupported(true);
                webXml.createServletMapping()
                            .servletName(PushServlet.class.getSimpleName())
                            .urlPattern("/__custom_mapping");
                webXml.createContextParam()
                            .paramName("org.richfaces.push.handlerMapping")
                            .paramValue("/__custom_mapping");
                return webXml;
            }
        });

        return deployment.getFinalArchive();
    }

    @Test
    @RunAsClient
    public void test() {
        super.testSimplePush();
    }
}
