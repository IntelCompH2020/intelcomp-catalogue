package eu.intelcomp.catalogue.config;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final AuthenticationSuccessHandler authSuccessHandler;
    private final CompleteLogoutSuccessHandler logoutSuccessHandler;
    private final IntelcompProperties intelcompProperties;

    public SecurityConfig(AuthenticationSuccessHandler authSuccessHandler,
                          CompleteLogoutSuccessHandler logoutSuccessHandler,
                          IntelcompProperties intelcompProperties) {
        this.authSuccessHandler = authSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.intelcompProperties = intelcompProperties;
    }

    /*
     * Needed to save client repository in redis session.
     */
    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }

    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        .regexMatchers("/dump/.*", "/restore/", "/resources.*", "/resourceType.*", "/search.*", "/logs.*").hasAnyAuthority("ADMIN")
//                        .antMatchers(HttpMethod.GET, "/forms/**").permitAll()
//                        .antMatchers( "/forms/**").hasAnyAuthority("ADMIN")
                        .anyRequest().permitAll())
                .oauth2Client()
                .and()
                .oauth2Login()
                .successHandler(authSuccessHandler)
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("AccessToken")
                .and()
                .cors()
                .and()
                .csrf().disable();
        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                    logger.debug("User attributes: {}", oidcUserAuthority.getAttributes());
                    logger.debug("User Token Claims: {}", idToken.getClaims());
                    JSONArray icRoles = ((JSONArray) oidcUserAuthority.getIdToken().getClaims().get("ic-roles"));
                    logger.debug("User attributes.ic-roles: {}", icRoles);
                    if (icRoles != null) {
                        for (int i = 0; i < icRoles.size(); i++) {
                            mappedAuthorities.add(new SimpleGrantedAuthority(icRoles.get(i).toString().toUpperCase()));
                        }
                    }

                    if (idToken != null && intelcompProperties.getAdmins().contains(idToken.getClaims().get("email"))) {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                    } else if (userInfo != null && intelcompProperties.getAdmins().contains(userInfo.getEmail())) {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                    } else {
                        if (((OidcUserAuthority) authority).getAttributes() != null
                                && ((OidcUserAuthority) authority).getAttributes().containsKey("email")
                                && (intelcompProperties.getAdmins().contains(((OidcUserAuthority) authority).getAttributes().get("email")))) {
                            mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                        }
                    }

                    // Map the claims found in idToken and/or userInfo
                    // to one or more GrantedAuthority's and add it to mappedAuthorities

                } else if (authority instanceof OAuth2UserAuthority) {

                    OAuth2User oauth2User = (OAuth2User) authority;
                    Map<String, Object> userAttributes = oauth2User.getAttributes();

                    if (userAttributes != null && intelcompProperties.getAdmins().contains(userAttributes.get("email"))) {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                    }

                    mappedAuthorities.addAll(oauth2User.getAuthorities());
                    // Map the attributes found in userAttributes
                    // to one or more GrantedAuthority's and add it to mappedAuthorities

                }
            });
            logger.debug("Granted Authorities: {}", mappedAuthorities);
            return mappedAuthorities;
        };
    }
}
