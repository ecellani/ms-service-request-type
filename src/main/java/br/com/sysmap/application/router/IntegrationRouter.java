package br.com.sysmap.application.router;

import br.com.sysmap.infrastructure.config.ApplicationConfig;
import br.com.sysmap.infrastructure.config.ApplicationConfig.Integration;
import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponseFault;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayInputStream;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;
import static org.apache.camel.model.rest.RestBindingMode.json;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

/**
 * Created by ecellani on 02/06/17.
 */
@Component
public class IntegrationRouter extends RouteBuilder {

    @Autowired
    private ApplicationConfig config;

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("servlet")
            .bindingMode(json)
            .dataFormatProperty("prettyPrint", "true")
            .apiContextPath(config.getPath().getApiDoc())
                .apiProperty("api.title", config.getPath().getApiTitle())
                .apiProperty("api.version", config.getPath().getApiVersion())
                .apiProperty("cors", "true");

        Integration routeIntegration = config.getRoutes().getIntegration();
        JaxbDataFormat jaxbSelfEmpowered = new JaxbDataFormat("br.com.sysmap.webservice.customer.selfempowered");
        Namespaces cus = new Namespaces("cus", "http://www.gvt.com.br/CustomerManagement/CustomerSelfManagement/CustomerSelfEmpowered/CustomerSelfEmpowered/");

        from(routeIntegration.getServiceRequestType())
            .id(routeIntegration.getServiceRequestType())
            .setProperty("initial-body", body())
            .to("xquery:search-service-request-type.xquery")
            .removeHeaders("CamelHttp*")
            .setHeader(CONTENT_TYPE, constant(TEXT_XML_VALUE))
            .doTry()
                .to(config.getThirdParty().getServiceRequestTypeEndpoint())
                .setBody().xpath("//cus:searchServiceRequestTypeOut", cus)
                .unmarshal(jaxbSelfEmpowered)
            .endDoTry()
            .doCatch(HttpOperationFailedException.class)
                .process(exchange -> {
                    HttpOperationFailedException e = exchange.getProperty(EXCEPTION_CAUGHT, HttpOperationFailedException.class);

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(e.getResponseBody().getBytes()));
                    Node nodeFault = doc.getElementsByTagNameNS("http://www.gvt.com.br/GvtCommonEntities", "gvtBusinessServiceResponseFault").item(0);

                    Unmarshaller unmarsh = JAXBContext.newInstance(GvtBusinessServiceResponseFault.class).createUnmarshaller();
                    DOMSource domSource = new DOMSource(nodeFault);
                    JAXBElement<GvtBusinessServiceResponseFault> faultElement = unmarsh.unmarshal(domSource, GvtBusinessServiceResponseFault.class);
                    exchange.getIn().setBody(faultElement.getValue());
                })
            .end()
            .to("bean:antiCorruptionLayer?method=transform(${body}, ${header.initial-body})");
    }
}
