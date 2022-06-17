package eu.intelcomp.catalogue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class CompleteLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final String OPENID_CONFIGURATION = "/.well-known/openid-configuration";
    private final String END_SESSION_ENDPOINT = "end_session_endpoint";
    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public CompleteLogoutSuccessHandler(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        URL url = ((DefaultOidcUser) (((OAuth2AuthenticationToken) authentication).getPrincipal())).getIssuer();
        String logoutEndpoint = getLogoutEndpoint(url);
        String logoutUrl;
        if (logoutEndpoint != null) {
            logoutUrl = String.format("%s?redirect_uri=%s", logoutEndpoint, applicationProperties.getLogoutRedirect());
        } else {
            logoutUrl = applicationProperties.getLogoutRedirect();
        }
        response.sendRedirect(logoutUrl);
        super.onLogoutSuccess(request, response, authentication);
    }

    private String getLogoutEndpoint(URL issuerUrl) {
        Map<String, String> map = new HashMap<>();
        try {
            issuerUrl = new URL(issuerUrl.toString() + OPENID_CONFIGURATION);
            map = (Map<String, String>) restTemplate.getForObject(issuerUrl.toURI(), Map.class);
        } catch (MalformedURLException | URISyntaxException e) {
            logger.error("Error attempting to find Logout Endpoint", e);
            throw new RuntimeException(e);
        }
        return map.get(END_SESSION_ENDPOINT);
    }
}
