package hub.helper;

import javax.inject.Named;
import java.io.ByteArrayOutputStream;

@Named
public class Stringify {

    public String soapMessage(javax.xml.soap.SOAPMessage message) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        message.writeTo(out);

        return new String(out.toByteArray());
    }
}
