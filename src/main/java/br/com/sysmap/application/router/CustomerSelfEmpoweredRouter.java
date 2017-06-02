package br.com.sysmap.application.router;

import br.com.sysmap.application.domain.SearOut;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.path;
import static org.springframework.http.HttpStatus.OK;

/**
 * Created by ecellani on 01/06/17.
 */
@Component
public class CustomerSelfEmpoweredRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration("servlet");

        rest("/customers").description("Customer Self Empowered Rest Service")
            .consumes("application/json")
            .produces("application/json")

        .get("/{serviceId}/{channel}").description("").outTypeList(SearOut.class)
            .param().name("serviceId").type(path).dataType("integer").description("The ID of service").endParam()
            .param().name("channel").type(path).dataType("string").description("The channel").endParam()
            .responseMessage().code(OK.value()).message("The list of the objects successfully returned").endResponseMessage()
            .to("bean:searchService?method=search(${header.serviceId}, ${header.channel})")
        ;

        from("direct:x1")
            .log("-------------> BODY: ${body}")
        .end();
    }
}
