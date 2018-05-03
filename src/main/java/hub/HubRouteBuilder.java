package hub;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static hub.helper.SOAPMessageToString.stringify;

@ApplicationScoped
@Startup
@ContextName("cdi-context")
public class HubRouteBuilder extends RouteBuilder {

    @Inject
    PingBean pingBean;

    @Inject
    CourtOfAppealBean courtOfAppealBean;

    private final static Logger LOGGER = Logger.getLogger(HubRouteBuilder.class.getName());

    @Override
    public void configure() throws Exception {
        from("direct:ping").setBody(simple(pingBean.version()));

        from("direct:search")
                .process(exchange -> {
                    String caseNumber = exchange.getIn().getBody(String.class);
                    LOGGER.log(Level.INFO, caseNumber);
                    String message = stringify(courtOfAppealBean.searchByCaseNumber(caseNumber));

                    exchange.getOut().setBody(message);
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
                .setHeader("Authorization", constant(courtOfAppealBean.basicAuthorization()))
                .setHeader("SOAPAction", constant(courtOfAppealBean.searchSoapAction()))
                .to(courtOfAppealBean.searchEndpoint());
    }
}
