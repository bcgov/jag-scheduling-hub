package hub;

import javax.inject.Named;
import javax.xml.soap.*;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
public class CourtOfAppealBean {

    private final static Logger LOGGER = Logger.getLogger(CourtOfAppealBean.class.getName());

    protected String getValue(String key) {
        LOGGER.log(Level.INFO, "env."+key+"=" + System.getenv(key));
        LOGGER.log(Level.INFO, "properties."+key+"=" + System.getProperty(key));

        return System.getenv(key) != null ? System.getenv(key) : System.getProperty(key);
    }

    public String user() {
        return getValue("COA_USER");
    }

    public String password() {
        return getValue("COA_PASSWORD");
    }

    public String searchEndpoint() {
        return getValue("COA_SEARCH_ENDPOINT");
    }

    public String namespace() {
        return getValue("COA_NAMESPACE");
    }

    public String searchSoapAction() {
        return getValue("COA_SEARCH_SOAP_ACTION");
    }

    public String basicAuthorization() {
        return "Basic " + Base64.getEncoder().encodeToString(
                (this.user() + ":" + this.password()).getBytes());
    }

    public SOAPMessage searchByCaseNumber(String caseNumber) throws Exception {
        MessageFactory myMsgFct = MessageFactory.newInstance();
        SOAPMessage message = myMsgFct.createMessage();
        SOAPPart mySPart = message.getSOAPPart();
        SOAPEnvelope myEnvp = mySPart.getEnvelope();
        SOAPBody body = myEnvp.getBody();
        Name bodyName = myEnvp.createName("SearchByCaseNumber", "any", this.namespace());
        SOAPBodyElement gltp = body.addBodyElement(bodyName);
        Name myContent = myEnvp.createName("strCaseNumber", "any", this.namespace());
        SOAPElement mySymbol = gltp.addChildElement(myContent);
        mySymbol.addTextNode(caseNumber);
        message.saveChanges();

        return message;
    }
}
