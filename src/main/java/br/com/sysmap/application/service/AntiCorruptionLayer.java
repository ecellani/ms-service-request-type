package br.com.sysmap.application.service;

/**
 * Created by ecellani on 03/06/17.
 */
public interface AntiCorruptionLayer {

    Object transform(Object in, br.com.sysmap.application.domain.ServiceRequestType initialBody) throws Exception;
}
