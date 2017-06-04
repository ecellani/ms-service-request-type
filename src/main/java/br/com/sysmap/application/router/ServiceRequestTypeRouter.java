package br.com.sysmap.application.router;

import br.com.sysmap.application.domain.CustomResponse;
import br.com.sysmap.infrastructure.config.ApplicationConfig;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestBindingMode.json;
import static org.apache.camel.model.rest.RestParamType.query;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Created by ecellani on 01/06/17.
 */
@Component
public class ServiceRequestTypeRouter extends RouteBuilder {

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

        rest(config.getPath().getServiceRequestType())
            .description(config.getPath().getServiceRequestTypeDesc())
            .consumes(APPLICATION_JSON_UTF8_VALUE)
            .produces(APPLICATION_JSON_UTF8_VALUE)

        .get().description("Search the service request types").outType(CustomResponse.class)
            .param().name("serviceid").type(query).dataType("string").description("The ID of service").required(true).endParam()
            .param().name("channel").type(query).dataType("string").description("The channel").required(true).endParam()
            .responseMessage()
                .code(OK.value())
                .message("Custom Response with the list of the service request types successfully returned")
            .endResponseMessage()
            .responseMessage()
                .code(INTERNAL_SERVER_ERROR.value())
                .message("Custom Response with the error description")
            .endResponseMessage()
            .to("direct:service-request-type-search")
        ;
   }
}
