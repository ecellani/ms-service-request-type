package br.com.sysmap.application.router;

import br.com.sysmap.application.domain.CustomResponse;
import br.com.sysmap.application.domain.ResponseError;
import br.com.sysmap.infrastructure.config.ApplicationConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.component.rabbitmq.RabbitMQConstants.DELIVERY_MODE;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
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
        String customErrorResponseJson = new CustomResponse(false, responseError).toJson();

        from("direct:service-request-type-search")
            .choice().when(simple("${header.channel} == null || ${header.serviceid} == null"))
                .setHeader(HTTP_RESPONSE_CODE, constant(BAD_REQUEST.value()))
                .setBody(simple(customErrorResponseJson))
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
                    String fields = (String) headers.get("fields");
                    if (isNotBlank(fields)) {
                        CustomResponse payloadClean = cleanFields(customResponse.toJson(), asList(fields.split(",")));
                        if (payloadClean != null)
                            customResponse.setPayload(payloadClean.getPayload());
                    }
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

    private CustomResponse cleanFields(String json, List<String> fields) {
        if (isBlank(json) || isEmpty(fields))
            return null;

        try {
            JsonNode rootNode = new ObjectMapper().readValue(json, JsonNode.class);
            for (JsonNode node : rootNode.get("payload")) {
                Iterator<String> it = node.fieldNames();
                List<String> fieldsToRemove = new ArrayList<>();
                while (it.hasNext()) {
                    String field = it.next();
                    if (!fields.contains(field))
                        fieldsToRemove.add(field);
                }
                ObjectNode objNode = (ObjectNode) node;
                fieldsToRemove.stream().forEach(objNode::remove);
            }
            return new ObjectMapper().treeToValue(rootNode, CustomResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
