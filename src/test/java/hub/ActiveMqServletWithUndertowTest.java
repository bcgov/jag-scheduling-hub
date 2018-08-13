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
import static io.undertow.servlet.Servlets.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActiveMqServletWithUndertowTest {

    private static Undertow server;
    private static int port = 8889;
    private String url = "http://localhost:" + port + "/message/this-topic";

    @BeforeClass
    public static void startServer() throws ServletException {
        DeploymentInfo servletBuilder = deployment()
                .setClassLoader(ActiveMqServletWithUndertowTest.class.getClassLoader())
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
        AsyncHttpResponse client = asyncGet(url);
        ThirdParty.post(url, "body=hi");

        Assert.assertThat(client.getBody(), equalTo("hi"));
    }

    @Test
    public void returnsNoContentAfterTimeoutWhenQueueIsEmpty() throws Exception {
        HttpResponse response = get(url);

        assertThat(response.getStatusCode(), equalTo(204));
        assertThat(response.getBody(), equalTo(""));
    }

    @Test
    @Ignore
    public void severalClientsCanListenToOneTopic() throws Exception {
        AsyncHttpResponse client1 = asyncGet(url);
        AsyncHttpResponse client2 = asyncGet(url);
        ThirdParty.post(url, "body=welcome");

        Assert.assertThat(client1.getBody(), equalTo("welcome"));
        Assert.assertThat(client2.getBody(), equalTo("welcome"));
    }
}