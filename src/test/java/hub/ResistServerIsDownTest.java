package hub;

import hub.http.SearchServlet;
import hub.support.HttpResponse;
import hub.support.HavingHubRunning;
import org.junit.Before;
import org.junit.Test;

import static hub.support.GetRequest.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResistServerIsDownTest extends HavingHubRunning {

    @Before
    public void setProperties() {
        System.setProperty("COA_SEARCH_ENDPOINT", "http4://localhost:8111");
        System.setProperty("COA_NAMESPACE", "http://hub.org");
    }

    @Test
    public void returnsServiceUnavailable() throws Exception {
        context.addServlet(SearchServlet.class, "/search");
        server.start();

        HttpResponse response = get("http://localhost:8888/search?caseNumber=any");

        assertThat(response.getBody(), equalTo("SERVICE UNAVAILABLE"));
        assertThat(response.getStatusCode(), equalTo(503));
    }
}