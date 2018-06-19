package hub;

import com.sun.net.httpserver.HttpServer;
import hub.http.SearchServlet;
import hub.support.HttpResponse;
import hub.support.HavingHubRunning;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static hub.support.GetRequest.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Form7NotFoundTest extends HavingHubRunning {

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
    public void stopCsoServer() {
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

        HttpResponse response = get("http://localhost:8888/search?caseNumber=unknown");

        assertThat(response.getBody(), equalTo("NOT FOUND"));
        assertThat(response.getStatusCode(), equalTo(404));
    }
}