package eu.intelcomp.catalogue.config.logging;

import gr.athenarc.catalogue.config.logging.AbstractLogContextFilter;
import org.slf4j.spi.MDCAdapter;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LogUser extends AbstractLogContextFilter {

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String USER_INFO = "user_info";

    @Override
    public void editMDC(MDCAdapter mdc) {
        String transactionId = UUID.randomUUID().toString();
        mdc.put(TRANSACTION_ID, transactionId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            try {
                mdc.put(USER_INFO, authentication.toString());
            } catch (InsufficientAuthenticationException e) {
                mdc.put(USER_INFO, authentication.toString());
            }
        }
    }
}
