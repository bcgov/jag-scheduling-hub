package hub;

import hub.http.PingServlet;
import hub.support.AsyncHttpResponse;
import hub.support.HavingHubRunning;
import hub.support.HttpResponse;
import hub.support.ThirdParty;
import org.apache.activemq.web.MessageServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static hub.support.AsyncGetRequest.asyncGet;
import static hub.support.GetRequest.get;
import static hub.support.Resource.bodyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ActiveMqTest extends HavingHubRunning {

    @Test
    public void allowsToPublishOneMessage() throws Exception {
        context.setInitParameter("org.apache.activemq.brokerURL", "vm://localhost");
        context.addServlet(MessageServlet.class, "/message/*");
        server.start();

        AsyncHttpResponse client = asyncGet("http://localhost:8888/message/this-queue?type=queue");
        ThirdParty.post("http://localhost:8888/message/this-queue?type=queue", "body=hello");

        Assert.assertThat(client.getBody(), equalTo("hello"));
    }
}