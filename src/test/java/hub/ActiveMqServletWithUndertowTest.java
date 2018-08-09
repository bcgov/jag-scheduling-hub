package hub;

import hub.http.ActiveMqServlet;
import hub.support.AsyncHttpResponse;
import hub.support.HttpResponse;
import hub.support.ThirdParty;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;

import static hub.support.AsyncGetRequest.asyncGet;
import static hub.support.GetRequest.get;
import static io.undertow.servlet.Servlets.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActiveMqServletWithUndertowTest {

    private static Undertow server;

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
                .addHttpListener(8080, "localhost")
                .setHandler(servletHandler)
                .build();
        server.start();
    }
    @AfterClass
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void exposesQueueForRead() throws Exception {
        ThirdParty.post("http://localhost:8080/message/hello-queue?type=queue", "body=hello");
        HttpResponse response = get("http://localhost:8080/message/hello-queue?type=queue");
        
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getBody(), equalTo("hello"));
    }

    @Test
    public void returnsNoContentAfterTimeoutWhenQueueIsEmpty() throws Exception {
        HttpResponse response = get("http://localhost:8080/message/empty-queue?type=queue");

        assertThat(response.getStatusCode(), equalTo(204));
        assertThat(response.getBody(), equalTo(""));
    }

    @Test
    public void readCanWaitForNewMessage() throws Exception {
        AsyncHttpResponse client = asyncGet("http://localhost:8080/message/this-queue?type=queue");
        ThirdParty.post("http://localhost:8080/message/this-queue?type=queue", "body=welcome");

        Assert.assertThat(client.getBody(), equalTo("welcome"));
    }
}