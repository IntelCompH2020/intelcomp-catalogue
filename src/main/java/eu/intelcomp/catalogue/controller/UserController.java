package eu.intelcomp.catalogue.controller;

import eu.intelcomp.catalogue.domain.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    public UserController() {
    }

    @GetMapping("info")
    public ResponseEntity<User> getInfo(@Parameter(hidden = true) Authentication authentication) {
        return new ResponseEntity<>(User.of(authentication), HttpStatus.OK);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("token")
    public OAuth2AccessToken getAccessToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        return accessToken;
    }

    @Hidden
    @GetMapping("auth/oidc-principal")
    public OidcUser getOidcUserPrincipal(@AuthenticationPrincipal OidcUser principal) {
        return principal;
    }

    @Hidden
    @GetMapping("auth/oauth2-principal")
    public OAuth2User getOAuth2UserPrincipal(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }

    @Hidden
    @GetMapping("auth")
    public Authentication getAuth(Authentication auth) {
        return auth;
    }
}
