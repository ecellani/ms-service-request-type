package br.com.sysmap.application.router;

import br.com.sysmap.application.domain.CustomResponse;
import br.com.sysmap.application.domain.ResponseError;
import br.com.sysmap.infrastructure.config.ApplicationConfig;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.component.rabbitmq.RabbitMQConstants.DELIVERY_MODE;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.springframework.boot.autoconfigure.jms.JmsProperties.DeliveryMode.PERSISTENT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Created by ecellani on 02/06/17.
 */
@Component
public class InOutRouter extends RouteBuilder {

    @Autowired
    private ApplicationConfig config;

    @Override
    public void configure() throws Exception {

        String message = "Query parameters 'channel' and 'serviceid' must be informed";
        ResponseError responseError = new ResponseError(BAD_REQUEST.getReasonPhrase(), message);
        String customResponseJson = new CustomResponse(false, responseError).toJson();

        from("direct:service-request-type-search")
            .choice().when(simple("${header.channel} == null || ${header.serviceid} == null"))
                .setHeader(HTTP_RESPONSE_CODE, constant(BAD_REQUEST.value()))
                .setBody(simple(customResponseJson))
                .setHeader(CONTENT_TYPE, constant(APPLICATION_JSON_UTF8_VALUE))
            .otherwise()
                .setHeader(DELIVERY_MODE, constant(PERSISTENT.getValue()))
                .inOut(config.getQueues().getServiceRequestTypeSearch()) // Send to rabbit using InOut exchange pattern
                .to("bean:serviceRequestType?method=search(${header.serviceid}, ${header.channel})")
                .setHeader(CONTENT_TYPE, constant(APPLICATION_JSON_UTF8_VALUE))
                .process(exchange -> {
                    int responseStatus = OK.value();
                    CustomResponse customResponse = exchange.getIn().getBody(CustomResponse.class);

                    if (customResponse.getError() != null)
                        responseStatus = INTERNAL_SERVER_ERROR.value();

                    if (customResponse.getSuccess()
                            && isEmpty(customResponse.getPayload()))
                        responseStatus = NOT_FOUND.value();

                    if (responseStatus != OK.value())
                        exchange.getIn().setBody(customResponse.toJson());

                    Map<String, Object> headers = exchange.getIn().getHeaders();
                    headers.put(HTTP_RESPONSE_CODE, responseStatus);
                    headers.put(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE);
                })
            .endChoice()
        .end();

        // Rabbit reply
        from(config.getQueues().getServiceRequestTypeSearch())
            .removeHeaders("CamelHttp*")
        .end();
    }
}
