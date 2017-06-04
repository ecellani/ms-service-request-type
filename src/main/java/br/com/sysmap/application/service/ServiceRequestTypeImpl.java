package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.CustomResponse;
import br.com.sysmap.application.domain.ResponseError;
import br.com.sysmap.application.domain.ServiceRequestType;
import br.com.sysmap.infrastructure.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

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

    @Autowired
    private RedisTemplate<String, List<ServiceRequestType>> redisTemplate;

    @Override
    public CustomResponse search(String serviceId, String channel) {

        List<ServiceRequestType> cache = getCache(serviceId, channel);
        if (isNotEmpty(cache))
            return new CustomResponse(true, cache);

        CustomResponse customResponse;
        try {
            Object result = searchServiceRequestTypeWS(serviceId, channel);

            if (result instanceof List) {
                List<ServiceRequestType> serviceRequestTypeList = (List) result;
                customResponse = new CustomResponse(true, serviceRequestTypeList);
                saveCache(serviceId, channel, serviceRequestTypeList);

            } else if (result instanceof ResponseError) {
                customResponse = new CustomResponse(false, (ResponseError) result);
            } else {
                throw new Exception("Unknown result object");
            }
            log.info(customResponse.toString());
        } catch (Exception e) {
            ResponseError responseError = new ResponseError("FATAL_ERROR", e.getMessage());
            customResponse = new CustomResponse(false, responseError);
            log.error(e.getMessage(), e);
        }
        return customResponse;
    }

    private List<ServiceRequestType> getCache(String serviceId, String channel) {
        try {
            String key = serviceId + channel;
            if (redisTemplate.hasKey(key)) {
                log.info("Key {} has been found in cache", key);
                return redisTemplate.opsForValue().get(key);
            }
        } catch (RedisConnectionFailureException e) {
            log.warn("Cannot connect to Redis. The service is not available", e);
        }
        return null;
    }

    private void saveCache(String serviceId, String channel, List<ServiceRequestType> serviceRequestTypeList) {
        try {
            String key = serviceId + channel;
            redisTemplate.opsForValue().set(key, serviceRequestTypeList);
            log.info("Key {} has been added to cache", key);
        } catch (RedisConnectionFailureException e) {
            log.warn("Cannot connect to Redis. The service is not available", e);
        }
    }

    private Object searchServiceRequestTypeWS(String serviceId, String channel) throws ExecutionException, InterruptedException {
        return template.asyncSendBody("direct:integration-service-request-type", new ServiceRequestType(serviceId, channel)).get();
    }
}
