package hub.helper;

import javax.inject.Named;
import javax.xml.soap.SOAPException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Named
public class Stringify {

    public String soapMessage(javax.xml.soap.SOAPMessage message) throws IOException, SOAPException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        message.writeTo(out);

        return new String(out.toByteArray());
    }
}
