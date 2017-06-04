package br.com.sysmap.infrastructure.helpers;

import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponse;
import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponseFault;
import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponseMessage;
import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayInputStream;

/**
 * Created by ecellani on 03/06/17.
 */
@Slf4j
@Component
public class SoapHandler {

    public GvtBusinessServiceResponseFault handleFaultBody(String body) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(body.getBytes()));
            Node nodeFault = doc.getElementsByTagNameNS("http://www.gvt.com.br/GvtCommonEntities", "gvtBusinessServiceResponseFault").item(0);
            Unmarshaller unmarsh = JAXBContext.newInstance(GvtBusinessServiceResponseFault.class).createUnmarshaller();
            DOMSource domSource = new DOMSource(nodeFault);
            JAXBElement<GvtBusinessServiceResponseFault> faultElement = unmarsh.unmarshal(domSource, GvtBusinessServiceResponseFault.class);
            return faultElement.getValue();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            GvtBusinessServiceResponseFault fault = new GvtBusinessServiceResponseFault();
            GvtBusinessServiceResponse faultResponse = new GvtBusinessServiceResponse();
            fault.setGvtBusinessServiceResponse(faultResponse);
            faultResponse.setResponseStatus(GvtBusinessServiceResponseStatus.fromValue("FATAL_ERROR"));
            GvtBusinessServiceResponseMessage message = new GvtBusinessServiceResponseMessage();
            message.setDescription(e.getMessage());
            faultResponse.getMessages().add(message);
            return fault;
        }
    }
}
