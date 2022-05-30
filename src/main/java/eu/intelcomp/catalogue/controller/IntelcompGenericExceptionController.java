package eu.intelcomp.catalogue.controller;

import eu.openminted.registry.core.exception.ServerError;
import gr.athenarc.catalogue.controller.GenericExceptionController;
import gr.athenarc.catalogue.exception.ResourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class IntelcompGenericExceptionController extends GenericExceptionController {

    private static final Logger logger = LogManager.getLogger(IntelcompGenericExceptionController.class);

    @ExceptionHandler(Exception.class)
    ResponseEntity<ServerError> handleUnauthorized(HttpServletRequest req, Exception ex) {
        logger.info(ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof ResourceException) {
            status = ((ResourceException) ex).getStatus();
        } else if (ex instanceof HttpClientErrorException) {
            status = ((HttpClientErrorException) ex).getStatusCode();
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof InsufficientAuthenticationException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return ResponseEntity
                .status(status)
                .body(new ServerError(req.getRequestURL().toString(), ex));
    }


}
