package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.CustomResponse;
import br.com.sysmap.application.domain.ResponseError;
import br.com.sysmap.application.domain.ServiceRequestType;
import br.com.sysmap.infrastructure.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by ecellani on 01/06/17.
 */
@Slf4j
@Service("serviceRequestType")
public class ServiceRequestTypeImpl implements br.com.sysmap.application.service.ServiceRequestType {

    @Autowired
    private ApplicationConfig config;

    @Produce
    private ProducerTemplate template;

    @Override
    public CustomResponse search(String serviceId, String channel) {
        ServiceRequestType serviceRequestType = new ServiceRequestType();
        serviceRequestType.setChannel(channel);
        serviceRequestType.setServiceId(serviceId);

        // TODO: busca no redis

        CustomResponse customResponse = new CustomResponse();
        CompletableFuture future = template.asyncSendBody(config.getRoutes().getIntegration().getServiceRequestType(), serviceRequestType);
        try {
            Object result = future.get();
            if (result instanceof List) {
                customResponse.setSuccess(true);
                customResponse.setPayload(result);

                // TODO: coloca no redis

            } else if (result instanceof ResponseError) {
                customResponse.setSuccess(false);
                customResponse.setError((ResponseError) result);
            } else {
                throw new Exception("Unknown result object");
            }
            log.info(customResponse.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            ResponseError responseError = new ResponseError();
            responseError.setMessage(e.getMessage());
            customResponse.setSuccess(false);
            customResponse.setError(responseError);
        }
        return customResponse;
    }
}
