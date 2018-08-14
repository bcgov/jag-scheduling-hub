package hub;

import hub.http.ActiveMqServlet;
import hub.support.AsyncHttpResponse;
import hub.support.HttpResponse;
import hub.support.ThirdParty;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.junit.*;

import javax.servlet.ServletException;

import static hub.support.AsyncGetRequest.asyncGet;
import static hub.support.GetRequest.get;
import static hub.support.Resource.bodyOf;
import static io.undertow.servlet.Servlets.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActiveMqQueueWithUndertowTest {

    private static Undertow server;
    private static int port = 8080;
    private String url = "http://localhost:" + port + "/message/tuesday?type=queue";

    @BeforeClass
    public static void startServer() throws ServletException {
        DeploymentInfo servletBuilder = deployment()
                .setClassLoader(ActiveMqQueueWithUndertowTest.class.getClassLoader())
                .setContextPath("")
                .setDeploymentName("test.war")
                .addServlets(servlet("ActiveMqServlet", ActiveMqServlet.class)
                            .addInitParam("maximumReadTimeout", "3000").addMapping("/message/*"));
        DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        HttpHandler servletHandler = manager.start();
        server = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(servletHandler)
                .build();
        server.start();
    }
    @AfterClass
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void publishMessageWithPost() throws Exception {
        ThirdParty.post(url, "body=hi");

        assertThat(bodyOf(url), equalTo("hi"));
    }
}