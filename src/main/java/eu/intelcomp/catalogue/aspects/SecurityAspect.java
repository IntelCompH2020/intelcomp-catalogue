package eu.intelcomp.catalogue.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class SecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    public SecurityAspect() {
    }

    @Before(value = "execution(* gr.athenarc.catalogue.controller.GenericItemController.create(..)) ||" +
            "execution(* gr.athenarc.catalogue.controller.GenericItemController.update(..)) ||" +
            "execution(* gr.athenarc.catalogue.controller.GenericItemController.delete(..))")
    void authorize(JoinPoint joinPoint) {
        String resourceType = (String) joinPoint.getArgs()[0];
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean authorized = false;
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return;
        }
        switch (resourceType) {
            case "tool":
                authorized = authorizeAiTool(authentication);
                break;
            case "ai_model":
                authorized = authorizeAiModel(authentication);
                break;
            case "dataset_type":
                authorized = authorizeDatasetType(authentication);
                break;
            case "dataset_instance":
                authorized = authorizeDatasetInstance(authentication);
                break;
            default:
                authorized = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
        }
        if (!authorized) {
            throw new AccessDeniedException("Forbidden");
        }
    }

    boolean authorizeDatasetType(Authentication authentication) {
        SimpleGrantedAuthority[] authorities = {new SimpleGrantedAuthority("OPERATOR_DATASET-INGESTOR")};
        return Arrays.stream(authorities).anyMatch(authority -> authentication.getAuthorities().contains(authority));
    }

    boolean authorizeDatasetInstance(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("OPERATOR_DATASET-INGESTOR"));
    }

    boolean authorizeAiTool(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("OPERATOR_DEVELOPER"));
    }

    boolean authorizeAiModel(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("OPERATOR_DEVELOPER"));
    }
}
