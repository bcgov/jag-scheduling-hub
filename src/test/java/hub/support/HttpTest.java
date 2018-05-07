package hub.support;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;

public class HttpTest {

    protected Server server;
    protected ServletContextHandler context;

    @Before
    public void aJettyServer() throws Exception {
        server = new Server(8888);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase("src/main/resources");
        context.addEventListener(new org.jboss.weld.environment.servlet.Listener());
        server.setHandler(context);
    }
    @After
    public void stopServer() throws Exception {
        server.stop();
    }
}