package br.com.sysmap.infrastructure.mapper;

import br.com.sysmap.application.domain.ResponseError;
import br.com.sysmap.application.domain.ResponseErrorMessage;
import br.com.sysmap.application.domain.ServiceRequestType;
import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponse;
import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponseFault;
import br.com.sysmap.webservice.customer.selfempowered.SearchServiceRequestTypeOut;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Created by ecellani on 03/06/17.
 */
public interface AntiCorruptionFunctions {

    Function<SearchServiceRequestTypeOut, List<ServiceRequestType>> externalTypeToServiceRequestType = requestTypeOut -> {
        if (requestTypeOut == null)
            return null;
        if (isEmpty(requestTypeOut.getServiceRequestType()))
            return emptyList();
        return requestTypeOut.getServiceRequestType()
                .stream()
                .map(external -> {
                    ServiceRequestType serviceRequestType = new ServiceRequestType();
                    serviceRequestType.setId(external.getId());
                    serviceRequestType.setDescription(external.getDescription());
                    return serviceRequestType;
                }).collect(toList());
    };

    Function<GvtBusinessServiceResponseFault, ResponseError> externalErrorToResponseError = externalError -> {
        if (externalError == null
                || externalError.getGvtBusinessServiceResponse() == null)
            return null;

        ResponseError responseError = new ResponseError();
        GvtBusinessServiceResponse businessServiceResponse = externalError.getGvtBusinessServiceResponse();

        if (businessServiceResponse.getResponseStatus() != null)
            responseError.setResponseStatus(businessServiceResponse.getResponseStatus().value());

        if (isNotEmpty(businessServiceResponse.getMessages())) {
            responseError.setMessages(
                businessServiceResponse.getMessages()
                    .stream()
                    .map(m ->
                        new ResponseErrorMessage(m.getSystem(), m.getStatusCode(), m.getDescription(), m.getStatusDetail())
                    ).collect(toList())
            );
        }
        return responseError;
    };
}
