package br.com.altamira.data.rest.sales.order;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilderException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import br.com.altamira.data.dao.sales.order.OrderDao;
import br.com.altamira.data.model.sales.order.Order;
import br.com.altamira.data.serialize.JSonViews;

@Stateless
@Path("sales/order")
public class OrderEndpoint {
	
    @Inject
    private Logger log;

    @Inject
    private Validator validator;
    
    @Inject 
    private OrderDao orderDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(
			@DefaultValue("0") @QueryParam("start") Integer startPosition,
			@DefaultValue("10") @QueryParam("max") Integer maxResult)
			throws IOException {

		List<Order> list;
		
		try {
			list = orderDao.list(startPosition, maxResult);
		} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
		
		if (list.size() == 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		//mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.ListView.class);

		return Response.ok(writer.writeValueAsString(list)).build();
	}
	
    @GET
    @Path("/{number:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByNumber(@PathParam("number") long number) throws JsonProcessingException {
    	Order entity = null;
    	
    	try {
    		entity = orderDao.findByNumber(number);
    	} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(new Hibernate4Module());
		//mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		ObjectWriter writer = mapper.writerWithView(JSonViews.EntityView.class);
		
		return Response.ok(writer.writeValueAsString(entity)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Order entity) throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

    	if (entity == null) {
    		return Response.status(Status.BAD_REQUEST).build();
    	}
    	
    	if (entity.getId() != null && entity.getId() > 0) {
    		return Response.status(Status.BAD_REQUEST).build();
    	}
    	
    	try {
    		// Validates member using bean validation
            validate(entity);
            
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
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
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
		        .build();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Order entity) {
    	
    	if (entity.getId() != id) {
			return Response.status(Status.CONFLICT)
					.entity("entity id doesn't match with resource path id")
					.build();
		}
    	
    	try {
    		// Validates member using bean validation
            validate(entity);
            
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
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
    	}

		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response
				.ok(UriBuilder.fromResource(OrderEndpoint.class)
						.path(String.valueOf(entity.getId())).build())
				.entity(entity).build();
    }

    @DELETE
    @Path("/{id:[0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
    	Order entity = null;
    	try {
    		entity = orderDao.remove(id);
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
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    	
		if (entity == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		return Response.noContent().build();
    }

    /**
     * <p>
     * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing member with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param member Member to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If member with the same email already exists
     */
    private void validate(Order entity) throws ConstraintViolationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Order>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

}
