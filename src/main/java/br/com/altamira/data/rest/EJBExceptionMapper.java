/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 *
 * @author Alessandro
 */
@Provider
public class EJBExceptionMapper implements ExceptionMapper<javax.ejb.EJBException> {

    @Inject
    private Logger log;

    @Context
    HttpHeaders headers;

    /**
     *
     * @param exception
     * @return
     */
    @Override
    public Response toResponse(EJBException exception) {
        Response.ResponseBuilder responseBuilder;

        log.log(Level.FINE, "EJB exception: {0}", exception.getCausedByException().getMessage());

        Map<String, Object> message = new HashMap<>();

        message.put("exception", exception.getClass().toString());

        List<String> sources = new ArrayList<>();
        
        sources.add(exception.toString());
        sources.add(exception.getCausedByException().toString());
        
        message.put("source", sources);
        
        List<String> causes = new ArrayList<>();
        
        causes.add(exception.getMessage());
        causes.add(exception.getCausedByException().getMessage());
        
        message.put("message", causes);
        
        message.put("trace", Arrays.toString(exception.getStackTrace()));

        if (exception.getCausedByException() instanceof NoResultException) {
            responseBuilder = Response.status(Response.Status.NOT_FOUND).entity(message);
        } else if (
                exception.getCausedByException() instanceof ConstraintViolationException  
                /*exception.getCausedByException() instanceof org.hibernate.exception.ConstraintViolationException*/) {
            responseBuilder = Response.status(Response.Status.CONFLICT).entity(message);
        } else if (exception.getCausedByException() instanceof IllegalArgumentException) {
            responseBuilder = Response.status(Response.Status.BAD_REQUEST).entity(message);
        } else {
            responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message);
        }

        if (headers.getHeaderString("Origin") != null && !headers.getHeaderString("Origin").isEmpty()) {
            responseBuilder
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Origin", headers.getHeaderString("Origin").isEmpty());
        }

        return responseBuilder.build();
    }
}
