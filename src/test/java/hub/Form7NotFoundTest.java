package hub;

import com.sun.net.httpserver.HttpServer;
import hub.http.PingServlet;
import hub.http.SearchServlet;
import hub.support.HttpTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

import static hub.support.Resource.bodyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Form7NotFoundTest extends HttpTest {

    private HttpServer cso;

    @Before
    public void startServer() throws Exception {
        cso = HttpServer.create( new InetSocketAddress( 8111 ), 0 );
        cso.createContext( "/", exchange -> {
            exchange.sendResponseHeaders( 200, "KO".length() );
            exchange.getResponseBody().write( "KO".getBytes() );
            exchange.close();
        } );
        cso.start();
    }
    @After
    public void stopServer() {
        cso.stop( 0 );
    }
    @Before
    public void setProperties() {
        System.setProperty("COA_SEARCH_ENDPOINT", "http4://localhost:8111");
        System.setProperty("COA_NAMESPACE", "http://hub.org");
    }

    @Test
    public void returnsNotFound() throws Exception {
        context.addServlet(SearchServlet.class, "/search");
        server.start();

        assertThat(bodyOf("http://localhost:8888/search?caseNumber=unknown"),
                equalTo("NOT FOUND"));
    }
}