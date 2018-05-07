package hub.camel;

import hub.CsoSearch;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static hub.helper.SOAPMessageToString.stringify;

@ApplicationScoped
@Startup
@ContextName("cdi-context")
public class SearchRouteBuilder extends RouteBuilder {

    @Inject
    CsoSearch csoSearch;

    private final static Logger LOGGER = Logger.getLogger(SearchRouteBuilder.class.getName());

    @Override
    public void configure() {
        XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
        xmlJsonFormat.setEncoding("UTF-8");
        xmlJsonFormat.setForceTopLevelObject(true);
        xmlJsonFormat.setTrimSpaces(true);

        from("direct:search")
                .process(exchange -> LOGGER.log(Level.INFO, "first call..."))
                .process(exchange -> {
                    String caseNumber = exchange.getIn().getBody(String.class);
                    LOGGER.log(Level.INFO, "caseNumber="+caseNumber);
                    String message = stringify(csoSearch.searchByCaseNumber(caseNumber));
                    LOGGER.log(Level.INFO, "message="+message);
                    exchange.getOut().setBody(message);
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
                .setHeader("Authorization", constant(csoSearch.basicAuthorization()))
                .setHeader("SOAPAction", constant(csoSearch.searchByCaseNumberSoapAction()))
                .to(csoSearch.searchEndpoint())
                .process(exchange -> {
                    String answer = exchange.getIn().getBody(String.class);
                    LOGGER.log(Level.INFO, "answer of first call="+answer);

                    exchange.getOut().setBody(answer);
                })
                .choice()
                    .when(body().contains("<CaseId>"))
                        .process(exchange -> LOGGER.log(Level.INFO, "second call..."))
                        .process(exchange -> {
                            String caseId = csoSearch.extractCaseId(exchange.getIn().getBody(String.class));
                            LOGGER.log(Level.INFO, "caseId="+caseId);

                            exchange.getOut().setBody(stringify(csoSearch.viewCaseParty(caseId)));
                        })
                        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                        .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
                        .setHeader("Authorization", constant(csoSearch.basicAuthorization()))
                        .setHeader("SOAPAction", constant(csoSearch.viewCasePartySoapAction()))
                        .to(csoSearch.searchEndpoint())
                        .process(exchange -> {
                            String answer = exchange.getIn().getBody(String.class);
                            LOGGER.log(Level.INFO, "answer of second call="+answer);

                            exchange.getOut().setBody(answer);
                        })
                        .marshal(xmlJsonFormat)
                        .endChoice()
                    .otherwise()
                        .process(exchange -> LOGGER.log(Level.INFO, "not found..."))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                        .setBody(constant("NOT FOUND"))
        ;
    }
}
