package br.com.sysmap.application.service;

import br.com.sysmap.application.domain.SearIn;
import br.com.sysmap.application.domain.SearOut;
import br.com.sysmap.webservice.customer.selfempowered.SearchServiceRequestTypeIn;
import br.com.sysmap.webservice.customer.selfempowered.ServiceRequestTypeIn;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by ecellani on 01/06/17.
 */
@Slf4j
@Service("searchService")
public class SearchServiceImpl implements SearchService {

//    @Produce(uri = "direct:x1")
    @Produce(uri = "xquery:search.xquery")
    private ProducerTemplate template;

    @Override
    public Collection<SearOut> search(Integer serviceId, String channel) {
        SearIn searIn = new SearIn(serviceId, channel);
        log.info("SearIn: {}", searIn);
        Future future = template.asyncSendBody(template.getDefaultEndpoint(), searIn);
        try {
            log.info("----> FUTURE: {}", future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
