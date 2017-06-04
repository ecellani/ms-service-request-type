package br.com.sysmap.application.router;

import br.com.sysmap.application.domain.ResponseError;
import br.com.sysmap.infrastructure.config.ApplicationConfig;
import br.com.sysmap.infrastructure.helpers.SoapHandler;
import org.apache.camel.TypeConversionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

/**
 * Created by ecellani on 02/06/17.
 */
@Component
public class IntegrationRouter extends RouteBuilder {

    @Autowired
    private ApplicationConfig config;

    @Autowired
    private SoapHandler soapHandler;

    @Override
    public void configure() throws Exception {

        JaxbDataFormat jaxbSelfEmpowered = new JaxbDataFormat("br.com.sysmap.webservice.customer.selfempowered");
        Namespaces cus = new Namespaces("cus", "http://www.gvt.com.br/CustomerManagement/CustomerSelfManagement/CustomerSelfEmpowered/CustomerSelfEmpowered/");

        from("direct:integration-service-request-type")
            .setProperty("initial-body", body())
            .to("xquery:search-service-request-type.xquery")
            .setHeader(CONTENT_TYPE, constant(TEXT_XML_VALUE))
            .doTry()
                .to(config.getThirdParty().getServiceRequestTypeEndpoint()) // Call to ws endpoint
                .setBody().xpath("//cus:searchServiceRequestTypeOut", cus)
                .unmarshal(jaxbSelfEmpowered)
            .endDoTry()
            .doCatch(Exception.class)
                .process(exchange -> {
                    Exception ex = exchange.getProperty(EXCEPTION_CAUGHT, Exception.class);
                    if (ex instanceof HttpOperationFailedException) {
                        exchange.getIn().setBody(soapHandler.handleFaultBody(((HttpOperationFailedException) ex).getResponseBody()));
                    } else if (ex instanceof TypeConversionException) {
                        exchange.getIn().setBody(new ResponseError("FATAL_ERROR", "Ocorreu um erro ao chamar o CustomerSelfEmpowered"));
                    } else {
                        exchange.getIn().setBody(new ResponseError("FATAL_ERROR", ex.getMessage()));
                    }
                })
            .end()
            .to("bean:antiCorruptionLayer?method=transform(${body}, ${header.initial-body})");
    }
}
