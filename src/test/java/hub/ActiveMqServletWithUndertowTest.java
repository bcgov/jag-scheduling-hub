package hub;

import hub.http.ActiveMqServlet;
import hub.support.HttpResponse;
import hub.support.ThirdParty;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.junit.*;

import javax.servlet.ServletException;

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
                .addServlets(servlet("ActiveMqServlet", new ActiveMqServlet().getClass()).addMapping("/message/*"));
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
    public void allowsToReadTheQueueTwice() throws Exception {
        ThirdParty.post("http://localhost:8080/message/this-queue?type=queue", "body=hello");

        HttpResponse response = get("http://localhost:8080/message/this-queue?type=queue");
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getBody(), equalTo("hello"));

        response = get("http://localhost:8080/message/this-queue?type=queue");
        assertThat(response.getStatusCode(), equalTo(204));
        assertThat(response.getBody(), equalTo(""));
    }
}