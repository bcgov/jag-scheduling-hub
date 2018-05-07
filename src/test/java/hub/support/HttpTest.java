package hub.support;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;

public class HttpTest {

    protected Server server;
    protected ServletContextHandler context;

    @Before
    public void startHub() throws Exception {
        server = new Server(8888);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("src/main/resources");
        context.addEventListener(new org.jboss.weld.environment.servlet.Listener());
        server.setHandler(context);
    }
    @After
    public void stopHub() throws Exception {
        server.stop();
    }
    @Before
    public void setDefaultProperties() {
        System.setProperty("COA_NAMESPACE", "http://hub.org");
        System.setProperty("COA_SEARCH_ENDPOINT", "http4://localhost:8111");
    }
}