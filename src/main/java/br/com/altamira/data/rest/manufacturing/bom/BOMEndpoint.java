/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest.manufacturing.bom;

import br.com.altamira.data.dao.manufacturing.bom.BOMDao;
import br.com.altamira.data.model.manufacturing.bom.BOM;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullValueSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

/**
 *
 * Bill of Material rest services
 */
@RequestScoped
@Path("/manufacturing/bom")
public class BOMEndpoint {

    private static final String NOT_FOUND = "Entity not found.";
    
    @Inject
    private Logger log;

    @Inject
    private BOMDao bomDao;

    /**
     *
     * @param startPosition
     * @param maxResult
     * @return
     * @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult,
            @Context HttpHeaders headers)
            throws IOException {

        List<BOM> list;

        try {
            list = bomDao.listUnchecked(startPosition, maxResult);
        } catch (NoResultException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations())
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        ObjectWriter writer = mapper.writerWithView(JSonViews.ListView.class);

        return Response.ok(writer.writeValueAsString(list))
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param startPosition
     * @param maxResult
     * @param search
     * @param headers
     * @return
     * @throws IOException
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult,
            @Size(min = 2) @QueryParam("search") String search,
            @Context HttpHeaders headers)
            throws IOException {

        List<BOM> list;

        try {
            list = bomDao.search(search, startPosition, maxResult);
        } catch (NoResultException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations())
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }

        return Response.ok(list)
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param number
     * @param headers
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("/{number:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByNumber(
            @Min(1) @PathParam("number") long number,
            @Context HttpHeaders headers) throws JsonProcessingException {
        BOM entity = null;

        try {
            entity = bomDao.findByNumber(number);
        } catch (NoResultException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations())
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (IllegalArgumentException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }


        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
        ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);

        return Response.ok(writer.writeValueAsString(entity))
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param entity
     * @param headers
     * @return
     * @throws IllegalArgumentException
     * @throws UriBuilderException
     * @throws JsonProcessingException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @NotNull(message = BOMDao.ENTITY_VALIDATION) BOM entity,
            @Context HttpHeaders headers) 
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

        try {
            entity = bomDao.create(entity);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations())
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (IllegalArgumentException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }


        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
        ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);

        return Response.created(
                UriBuilder.fromResource(BOMEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(writer.writeValueAsString(entity))
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param number
     * @param entity
     * @param headers
     * @return
     */
    @PUT
    @Path("/{number:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @Min(1) @PathParam("number") long number,
            @NotNull(message = BOMDao.ENTITY_VALIDATION) BOM entity,
            @Context HttpHeaders headers) {

        // Add the current date to checked field
        Date date = new Date();
        entity.setChecked(date);

        try {
            entity = bomDao.update(entity);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations())
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (IllegalArgumentException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }
        
        return Response
                .ok(UriBuilder.fromResource(BOMEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param number
     * @param headers
     * @return
     */
    @DELETE
    @Path("/{number:[0-9]*}")
    public Response deleteById(
            @Min(1) @PathParam("number") long number,
            @Context HttpHeaders headers) {
        try {
            bomDao.remove(number);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations())
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (IllegalArgumentException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("message", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseObj)
                    .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }

        return Response.noContent()
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param headers
     * @return
     */
    @OPTIONS
    public Response getCORSHeadersFromPath(@Context HttpHeaders headers) {
        return Response.ok()
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Origin, Content-Type, Content-Length, Accept, Authorization, X-Requested-With")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

    /**
     *
     * @param number
     * @param headers
     * @return
     */
    @OPTIONS
    @Path("/{number:[0-9][0-9]*}")
    public Response getCORSHeadersFromNumberPath(
            @PathParam("number") long number,
            @Context HttpHeaders headers) {
        return Response.ok()
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Origin, Content-Type, Content-Length, Accept, Authorization, X-Requested-With")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

    /**
     *
     * @param startPosition
     * @param maxResult
     * @param search
     * @param headers
     * @return
     */
    @OPTIONS
    @Path("/search")
    public Response getCORSHeadersFromSearchPath(
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult,
            @Size(min = 2) @QueryParam("search") String search,
            @Context HttpHeaders headers) {
        return Response.ok()
                .header("Access-Control-Allow-Origin", headers.getRequestHeader("Origin").get(0))
                .header("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Origin, Content-Type, Content-Length, Accept, Authorization, X-Requested-With")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation
     * fields, and their message. This can then be used by clients to show
     * violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.log(Level.FINE, "Validation completed. violations found: {0}", violations.size());

        Map<String, String> responseObj = new HashMap<>();

        violations.stream().forEach((violation) -> {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        });

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

}
