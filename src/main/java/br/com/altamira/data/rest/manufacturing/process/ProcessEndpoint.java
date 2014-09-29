package br.com.altamira.data.rest.manufacturing.process;

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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.Status;

import br.com.altamira.data.dao.manufacturing.process.ProcessDao;
import br.com.altamira.data.model.manufacturing.process.Process;

@Stateless
@Path("manufacturing/process")
public class ProcessEndpoint {
	@Inject
    private Logger log;

    @Inject
    private Validator validator;
    
    @Inject 
    private ProcessDao processDao;
    
    @GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(
			@DefaultValue("0") @QueryParam("start") Integer startPosition,
			@DefaultValue("10") @QueryParam("max") Integer maxResult)
			throws IOException {

		List<Process> list;
		
		try {
			list = processDao.list(startPosition, maxResult);
		} catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
		
		if (list.size() == 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		return Response.ok(list).build();
	}
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Process entity) {
    	//log.info("This is a log");
    	try {
    		// Validates member using bean validation
            validate(entity);
            
    		entity = processDao.create(entity);
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

		return Response.created(
		        UriBuilder.fromResource(ProcessEndpoint.class)
		        .path(String.valueOf(entity.getId())).build())
		        .entity(entity)
		        .build();
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
    private void validate(Process entity) throws ConstraintViolationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Process>> violations = validator.validate(entity);

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
