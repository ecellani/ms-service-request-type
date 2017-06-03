package br.com.sysmap.infrastructure.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ecellani on 02/06/17.
 */
@Data
@ToString
@Component
@ConfigurationProperties("application")
public class ApplicationConfig {

    private Path path = new Path();
    private Routes routes = new Routes();
    private ThirdParty thirdParty = new ThirdParty();

    @Data
    @ToString
    public static class Path {
        private String apiDoc;
        private String apiTitle;
        private String apiVersion;
        private String serviceRequestType;
        private String serviceRequestTypeDesc;
    }

    @Data
    @ToString
    public static class ThirdParty {
        private String serviceRequestTypeEndpoint;
    }

    @Data
    @ToString
    public static class Routes {
        private Rest rest = new Rest();
        private Integration integration = new Integration();
    }

    @Data
    @ToString
    public static class Rest {
        private String getServiceRequestType;
        private String getServiceRequestTypeFilter;
    }

    @Data
    @ToString
    public static class Integration {
        private String serviceRequestType;
    }
}
