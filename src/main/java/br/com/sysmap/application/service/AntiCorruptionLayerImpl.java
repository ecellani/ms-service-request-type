package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.ServiceRequestType;
import br.com.sysmap.webservice.customer.selfempowered.GvtBusinessServiceResponseFault;
import br.com.sysmap.webservice.customer.selfempowered.SearchServiceRequestTypeOut;
import org.springframework.stereotype.Service;

import static br.com.sysmap.infrastructure.mapper.AntiCorruptionFunctions.externalErrorToResponseError;
import static br.com.sysmap.infrastructure.mapper.AntiCorruptionFunctions.externalTypeToServiceRequestType;
import static java.util.stream.Collectors.toList;

/**
 * Created by ecellani on 03/06/17.
 */
@Service("antiCorruptionLayer")
public class AntiCorruptionLayerImpl implements AntiCorruptionLayer {

    @Override
    public Object transform(Object in, ServiceRequestType initialBody) throws Exception {
        if (in instanceof SearchServiceRequestTypeOut) {
            return externalTypeToServiceRequestType.apply((SearchServiceRequestTypeOut) in)
                    .stream()
                    .map(m -> new ServiceRequestType(initialBody.getServiceId(), initialBody.getChannel(), m.getId(), m.getDescription()))
                    .collect(toList());
        } else if (in instanceof GvtBusinessServiceResponseFault) {
            return externalErrorToResponseError.apply((GvtBusinessServiceResponseFault) in);
        }
        throw new Exception("Unknown result object");
    }
}
