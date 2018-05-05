package hub;

import hub.support.Resource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PingTest {

    protected Server server;

    @Before
    public void aJettyServer() throws Exception {
        server = new Server(8888);
    }
    @After
    public void stopServer() throws Exception {
        server.stop();
    }

    @Before
    public void setProperties() {
        System.setProperty("COA_SEARCH_ENDPOINT", "http4://localhost:8111");
        System.setProperty("OPENSHIFT_BUILD_COMMIT", "42isTheAnswer");
    }

    @Test
    public void returnsCommitHash() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("src/main/resources");
        context.addServlet(PingServlet.class, "/ping");
        context.addEventListener(new org.jboss.weld.environment.servlet.Listener());
        server.setHandler(context);
        server.start();

        assertThat(Resource.withUrl("http://localhost:8888/ping"), equalTo("42isTheAnswer"));
    }
}