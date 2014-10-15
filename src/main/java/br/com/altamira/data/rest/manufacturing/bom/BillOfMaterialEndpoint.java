/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest.manufacturing.bom;

import br.com.altamira.data.dao.sales.OrderDao;
import br.com.altamira.data.model.sales.Order;
import br.com.altamira.data.rest.WebApplication;
import br.com.altamira.data.rest.sales.OrderEndpoint;
import br.com.altamira.data.serialize.JSonViews;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import java.io.IOException;
import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

/**
 *
 * 
 */
@RequestScoped
@Path("/manufacturing/bom")
public class BillOfMaterialEndpoint {

    @Inject
    private Logger log;

    @Inject
    private OrderDao orderDao;
    
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
            @DefaultValue("10") @QueryParam("max") Integer maxResult)
            throws IOException {

        List<Order> list;

        try {
            list = orderDao.listUnchecked(startPosition, maxResult);
        } catch (NoResultException e) {
            list = new ArrayList<>(); // show empty results
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        ObjectWriter writer = mapper.writerWithView(JSonViews.ListView.class);

        return Response.ok(writer.writeValueAsString(list))
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }
    
    /**
     *
     * @param startPosition
     * @param maxResult
     * @param search
     * @return
     * @throws IOException
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult,
            @Size(min = 2) @QueryParam("search") String search)
            throws IOException {

        List<Order> list;

        try {
            list = orderDao.search(search, startPosition, maxResult);
        } catch (NoResultException e) {
            list = new ArrayList<>(); // show empty results
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        return Response.ok(list)
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param number
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("/{number:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByNumber(@PathParam("number") long number) throws JsonProcessingException {
        Order entity = null;

        try {
            entity = orderDao.findByNumber(number);
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
                    .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }

        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                    .header("Access-Control-Allow-Credentials", "true")
                    .build();
        }

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);

        return Response.ok(writer.writeValueAsString(entity))
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                .header("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Origin, Content-Type, Content-Length, Accept, Authorization, X-Requested-With")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

    /**
     *
     * @param entity
     * @return
     * @throws IllegalArgumentException
     * @throws UriBuilderException
     * @throws JsonProcessingException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Order entity) throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

        if (entity == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (entity.getId() != null && entity.getId() > 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            entity = orderDao.create(entity);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            createViolationResponse(ce.getConstraintViolations()).build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(responseObj).build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
        }

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        //mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
        ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);

        //return Response.ok(writer.writeValueAsString(entity)).build();
        return Response.created(
                UriBuilder.fromResource(OrderEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(writer.writeValueAsString(entity))
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param id
     * @param entity
     * @return
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Order entity) {

        if (entity.getId() != id) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("entity id doesn't match with resource path id")
                    .build();
        }

        try {
            entity = orderDao.update(entity);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations()).build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(entity).build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
        }

        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response
                .ok(UriBuilder.fromResource(OrderEndpoint.class)
                        .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }

    /**
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id:[0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        Order entity = null;
        try {
            orderDao.remove(id);
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            return createViolationResponse(ce.getConstraintViolations()).build();
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(responseObj).build();
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        if (entity == null) {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent()
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                .header("Access-Control-Allow-Credentials", "true")
                .build();
    }
    
    /**
     *
     * @return
     */
    @OPTIONS
    public Response getCORSHeadersFromPath() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
                .header("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Origin, Content-Type, Content-Length, Accept, Authorization, X-Requested-With")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

    /**
     *
     * @param number
     * @return
     */
    @OPTIONS
    @Path("/{number:[0-9][0-9]*}")
    public Response getCORSHeadersFromNumberPath(@PathParam("number") long number) {
        return Response.ok()
                .header("Access-Control-Allow-Origin", WebApplication.ACCESS_CONTROL_ALLOW_ORIGIN)
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
