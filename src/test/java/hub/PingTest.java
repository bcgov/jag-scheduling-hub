package hub;

import hub.support.Resource;
import hub.support.RouteTest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PingTest extends RouteTest {

    @Before
    public void setCommitHash() {
        System.setProperty("OPENSHIFT_BUILD_COMMIT", "42isTheAnswer");
    }

    @Test
    public void returnsCommitHash() throws Exception {
        context.addServlet(PingServlet.class, "/ping");
        server.start();

        assertThat(Resource.withUrl("http://localhost:8888/ping"), equalTo("42isTheAnswer"));
    }
}