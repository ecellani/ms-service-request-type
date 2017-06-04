package br.com.sysmap.application.router;

import br.com.sysmap.infrastructure.config.ApplicationConfig;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.camel.component.rabbitmq.RabbitMQConstants.DELIVERY_MODE;
import static org.springframework.boot.autoconfigure.jms.JmsProperties.DeliveryMode.PERSISTENT;

/**
 * Created by ecellani on 02/06/17.
 */
@Component
public class InOutRouter extends RouteBuilder {

    @Autowired
    private ApplicationConfig config;

    @Override
    public void configure() throws Exception {

        from("direct:service-request-type-search")
            .setHeader(DELIVERY_MODE, constant(PERSISTENT.getValue()))
            .inOut(config.getQueues().getServiceRequestTypeSearch()) // Send to rabbit using InOut exchange pattern
            .to("bean:serviceRequestType?method=search(${header.serviceid}, ${header.channel})")
        .end();

        // Rabbit reply
        from(config.getQueues().getServiceRequestTypeSearch()).removeHeaders("CamelHttp*").end();
    }
}
