package eu.intelcomp.catalogue.controller;

import gr.athenarc.catalogue.controller.GenericExceptionController;
import gr.athenarc.catalogue.exception.ResourceException;
import gr.athenarc.catalogue.exception.ServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(IntelcompGenericExceptionController.class);

    @ExceptionHandler(Exception.class)
    ResponseEntity<ServerError> handleException(HttpServletRequest req, Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof ResourceException) {
            logger.info(ex.getMessage(), ex);
            status = ((ResourceException) ex).getStatus();
        } else if (ex instanceof HttpClientErrorException) {
            logger.info(ex.getMessage(), ex);
            status = ((HttpClientErrorException) ex).getStatusCode();
        } else if (ex instanceof AccessDeniedException) {
            logger.info(ex.getMessage());
            logger.debug(ex.getMessage(), ex);
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof InsufficientAuthenticationException) {
            logger.info(ex.getMessage());
            logger.debug(ex.getMessage(), ex);
            status = HttpStatus.UNAUTHORIZED;
        }
        return ResponseEntity
                .status(status)
                .body(new ServerError(status, req, ex));
    }


}
