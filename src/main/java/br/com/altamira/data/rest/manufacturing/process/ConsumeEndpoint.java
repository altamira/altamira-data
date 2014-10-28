/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest.manufacturing.process;

import br.com.altamira.data.dao.manufacturing.process.ConsumeDao;
import br.com.altamira.data.dao.manufacturing.process.OperationDao;
import br.com.altamira.data.model.manufacturing.process.Consume;
import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.rest.BaseEndpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
import javax.ws.rs.core.UriBuilderException;

/**
 *
 * @author Alessandro
 */
@RequestScoped
public class ConsumeEndpoint extends BaseEndpoint<br.com.altamira.data.model.manufacturing.process.Consume> {

    @EJB
    private OperationDao operationDao;

    @EJB
    private ConsumeDao consumeDao;
    
    /**
     *
     */
    public ConsumeEndpoint() {
    	this.type = ConsumeEndpoint.class;
    }
    
    /**
     *
     * @param operation
     * @param startPosition
     * @param maxResult
     * @return
     * @throws IOException
     */
    @GET
    @Path("/consume")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(
            @Min(value = 1, message = ID_VALIDATION) @PathParam("operation") long operation,
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult)
            throws IOException {

        Operation entity = operationDao.find(operation);

        return createOkResponse(
                entity.getConsume()).build();
    }

    /**
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("/consume/{id:[0-9]*}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response find(
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "id") long id)
            throws JsonProcessingException {

        return createOkResponse(
                consumeDao.find(id)).build();
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
    @Path("/consume")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response create(
    		@Min(value = 1, message = ID_VALIDATION) @PathParam("operation") long id,
            @NotNull(message = ENTITY_VALIDATION) Consume entity)
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {
    	entity.setOperation(operationDao.find(id));
        return createCreatedResponse(
            consumeDao.create(entity)).build();
    }

    /**
     *
     * @param operation
     * @param id
     * @param entity
     * @return
     * @throws JsonProcessingException
     */
    @PUT
    @Path("/consume/{id:[0-9]*}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response update(
            @Min(value = 1, message = ID_VALIDATION) @PathParam("operation") long operation,
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "id") long id,
            @NotNull(message = ENTITY_VALIDATION) Consume entity)
            throws JsonProcessingException {
        
        entity.setOperation(operationDao.find(operation));
        
        return createOkResponse(
                consumeDao.update(entity)).build();
    }

    /**
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @DELETE
    @Path("/consume/{id:[0-9]*}")
    public Response delete(
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "id") long id)
            throws JsonProcessingException {
        
        consumeDao.remove(id);
        
        return createNoContentResponse().build();
    }

}
