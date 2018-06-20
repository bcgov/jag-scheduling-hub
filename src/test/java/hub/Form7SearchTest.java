package hub;

import com.sun.net.httpserver.HttpServer;
import hub.http.PingServlet;
import hub.http.SearchServlet;
import hub.support.HavingHubRunning;
import hub.support.HttpResponse;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static hub.support.GetRequest.get;
import static hub.support.Resource.bodyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Form7SearchTest extends HavingHubRunning {

    private HttpServer cso;

    @Before
    public void startServer() throws Exception {
        cso = HttpServer.create( new InetSocketAddress( 8111 ), 0 );
        cso.createContext( "/", exchange -> {
            String action = exchange.getRequestHeaders().getFirst("SOAPAction");
            String body = "<CaseId>12345</CaseId>";
            if ("second-call".equalsIgnoreCase(action)) {
                body = "<anything><that-will-be>jsonified</that-will-be></anything>";
            }
            exchange.sendResponseHeaders( 200, body.length() );
            exchange.getResponseBody().write( body.getBytes() );
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
        System.setProperty("COA_SEARCH_SOAP_ACTION", "first-call");
        System.setProperty("COA_VIEW_CASE_PARTY_SOAP_ACTION", "second-call");
    }

    @Test
    public void returnsInfoAsJson() throws Exception {
        context.addServlet(SearchServlet.class, "/search");
        server.start();

        assertThat(bodyOf("http://localhost:8888/search?caseNumber=unknown"),
                equalTo("{\"anything\":{\"that-will-be\":\"jsonified\"}}"));
    }

    @Test
    public void resistsInternalErrors() throws Exception {
        SearchServlet searchServlet = new SearchServlet();
        context.addServlet(new ServletHolder(searchServlet), "/search");
        server.start();

        HttpResponse response = get("http://localhost:8888/search?caseNumber=unknown");
        assertThat(response.getStatusCode(), equalTo(500));
        assertThat(response.getBody(), equalTo(""));
    }
}