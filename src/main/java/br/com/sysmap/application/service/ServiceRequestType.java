package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.CustomResponse;

/**
 * Created by ecellani on 01/06/17.
 */
public interface ServiceRequestType {

    CustomResponse search(String serviceId, String channel);
}
