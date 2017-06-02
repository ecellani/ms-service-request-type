package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.SearOut;

import java.util.Collection;

/**
 * Created by ecellani on 01/06/17.
 */
public interface SearchService {

    Collection<SearOut> search(Integer serviceId, String channel);
}
