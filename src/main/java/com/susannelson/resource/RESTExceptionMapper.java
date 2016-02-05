package com.susannelson.resource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Builds and returns Response for all exceptions.
 */
@Provider
public class RESTExceptionMapper implements ExceptionMapper<Exception> {

    private static Logger LOGGER = LoggerFactory.getLogger(RESTExceptionMapper.class);

    @Override
    /**
     * Handles exceptions by returning an appropriate http status code and json message depending on the exception type.
     * Returns 400 for MissingServletRequestParameterException.
     * Returns 404 for WordNotFoundException.
     * Returns 501 for NotFoundException.
     * Returns 500 for all other exceptions.
     * @returns Response with ErrorResponse body.
     */
    public Response toResponse(Exception e) {

        Object entity;
        int httpStatusCode;

        if (e instanceof MissingServletRequestParameterException || e instanceof IllegalArgumentException) {

            HttpStatus status = HttpStatus.BAD_REQUEST;
            httpStatusCode = 400;
            entity = new ErrorResponse(status.value(), "Please supply the word to check.");

        } else if (e instanceof WordNotFoundException) {

            HttpStatus status = HttpStatus.NOT_FOUND;
            httpStatusCode = 404;
            entity = new ErrorResponse(status.value(), "The word was not found.");

        }else if (e instanceof NotFoundException) {

            HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
            httpStatusCode = 501;
            entity = new ErrorResponse(status.value(), "There is no resource available at this URL.");
        } else {
            LOGGER.error("Something really bad happened: " + e.getMessage());
            LOGGER.error("Caught exception: ", e);

            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            httpStatusCode = 500;
            entity = new ErrorResponse(status.value(), "A server side error occurred.");
        }

        return Response.status(httpStatusCode).entity(entity).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
