package br.com.sysmap.application.router;

import br.com.sysmap.application.domain.CustomResponse;
import br.com.sysmap.infrastructure.config.ApplicationConfig;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestBindingMode.json;
import static org.apache.camel.model.rest.RestParamType.query;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
            .consumes(APPLICATION_JSON_VALUE)
            .produces(APPLICATION_JSON_VALUE)

        .get().description("Search the service request types").outTypeList(CustomResponse.class)
            .param().name("serviceid").type(query).dataType("string").description("The ID of service").endParam()
            .param().name("channel").type(query).dataType("string").description("The channel").endParam()
            .responseMessage().code(OK.value()).message("The list of the service request types successfully returned").endResponseMessage()
            .to("direct:service-request-type-search")
        ;
   }
}
