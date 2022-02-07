package eu.intelcomp.catalogue.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "job-service")
public class ExternalJobServiceProperties {

    String apiUrl;
    Authorization authorization;

    public static class Authorization {
        private String url;
        private String grantType;
        private String clientId;
        private String clientSecret;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }
}
