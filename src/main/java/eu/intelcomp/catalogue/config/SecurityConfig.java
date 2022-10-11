package eu.intelcomp.catalogue.config;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final AuthenticationSuccessHandler authSuccessHandler;
    private final CompleteLogoutSuccessHandler logoutSuccessHandler;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public SecurityConfig(AuthenticationSuccessHandler authSuccessHandler,
                          CompleteLogoutSuccessHandler logoutSuccessHandler,
                          ApplicationProperties applicationProperties) {
        this.authSuccessHandler = authSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.applicationProperties = applicationProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        .regexMatchers("/dump/.*", "/restore/", "/resources.*", "/resourceType.*", "/search.*", "/logs.*").hasAnyAuthority("ADMIN")
//                        .antMatchers(HttpMethod.GET, "/forms/**").permitAll()
//                        .antMatchers( "/forms/**").hasAnyAuthority("ADMIN")
                        .anyRequest().permitAll())
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
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (OidcUserAuthority.class.isInstance(authority)) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                    logger.debug("User attributes: {}", oidcUserAuthority.getAttributes());
                    JSONArray icRoles = ((JSONArray) oidcUserAuthority.getAttributes().get("ic-roles"));
                    logger.debug("User attributes.ic-roles: {}", icRoles);
                    if (icRoles != null) {
                        for (int i = 0; i < icRoles.size(); i++) {
                            mappedAuthorities.add(new SimpleGrantedAuthority(icRoles.get(i).toString().toUpperCase()));
                        }
                    }

                    if (idToken != null && applicationProperties.getAdmins().contains(idToken.getClaims().get("email"))) {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                    } else if (userInfo != null && applicationProperties.getAdmins().contains(userInfo.getEmail())) {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                    } else {
                        if (((OidcUserAuthority) authority).getAttributes() != null
                                && ((OidcUserAuthority) authority).getAttributes().containsKey("email")
                                && (applicationProperties.getAdmins().contains(((OidcUserAuthority) authority).getAttributes().get("email")))) {
                            mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                        }
                    }

                    // Map the claims found in idToken and/or userInfo
                    // to one or more GrantedAuthority's and add it to mappedAuthorities

                } else if (OAuth2UserAuthority.class.isInstance(authority)) {
                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;

                    Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                    if (userAttributes != null && applicationProperties.getAdmins().contains(userAttributes.get("email"))) {
                        mappedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                    }
                    // Map the attributes found in userAttributes
                    // to one or more GrantedAuthority's and add it to mappedAuthorities

                }
            });
            logger.debug("Granted Authorities: {}", mappedAuthorities);
            return mappedAuthorities;
        };
    }
}
