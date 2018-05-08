package hub.support;

import hub.helper.Stringify;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.xml.soap.*;

import static org.hamcrest.CoreMatchers.containsString;

public class StringifyTest extends CamelTestSupport {

    @Test
    public void transformsIntoXml() throws Exception {
        Stringify stringify = new Stringify();

        assertThat(stringify.soapMessage(dummy()),
                containsString("<any:greetings>hello world</any:greetings>"));
    }

    private SOAPMessage dummy() throws Exception {
        MessageFactory myMsgFct = MessageFactory.newInstance();
        SOAPMessage message = myMsgFct.createMessage();
        SOAPPart mySPart = message.getSOAPPart();
        SOAPEnvelope myEnvp = mySPart.getEnvelope();
        SOAPBody body = myEnvp.getBody();
        Name bodyName = myEnvp.createName("Message", "any", "http://message.net");
        SOAPBodyElement gltp = body.addBodyElement(bodyName);
        Name myContent = myEnvp.createName("greetings", "any", "http://message.net");
        SOAPElement mySymbol = gltp.addChildElement(myContent);
        mySymbol.addTextNode("hello world");
        message.saveChanges();

        return message;
    }
}
