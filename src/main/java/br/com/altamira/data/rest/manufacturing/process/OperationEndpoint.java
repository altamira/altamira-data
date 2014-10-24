package br.com.altamira.data.rest.manufacturing.process;

import java.io.IOException;

import javax.inject.Inject;
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

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.data.dao.manufacturing.process.OperationDao;
import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.rest.BaseEndpoint;
import static br.com.altamira.data.rest.BaseEndpoint.ENTITY_VALIDATION;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alessandro.holanda
 */
@RequestScoped
public class OperationEndpoint extends BaseEndpoint<Operation> {

    @Inject
    private OperationDao operationDao;
    
    @Inject
    private ConsumeEndpoint consumeEndpoint;
    
    /**
     *
     * @param processId
     * @param startPosition
     * @param maxResult
     * @return
     * @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(
            @PathParam("process") long processId,
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult)
            throws IOException {

        return createOkResponse(
                operationDao.list(processId, startPosition, maxResult)).build();
    }

        /**
     *
     * @param operationId
     * @return
     * @throws JsonProcessingException
     */
    @Path("/{operation:[0-9]*}/consume")
    @Produces(MediaType.APPLICATION_JSON)
    public java.lang.Object listOperation(
            @PathParam("operation") long operationId)
            throws JsonProcessingException, NoResultException {

        return consumeEndpoint;

    }
    
    /**
     *
     * @param sequence
     * @return
     * @throws JsonProcessingException
     */
//    @GET
//    @Path("{sequence:[0-9]*}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response findById(@PathParam("sequence") long sequence) throws JsonProcessingException {
//        return createOkResponse(
//                operationDao.find(sequence)).build();
//    }

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
    public Response create(
            @NotNull(message = ENTITY_VALIDATION) Operation entity)
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {
        
        return createCreatedResponse( 
                operationDao.create(entity)).build();
    }

    /**
     *
     * @param sequence
     * @param entity
     * @return
     * @throws JsonProcessingException
     */
    @PUT
    @Path("/{sequence:[0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @Min(value = 1, message = ID_VALIDATION) @PathParam("sequence") long sequence,
            @NotNull(message = ENTITY_VALIDATION) Operation entity)
            throws JsonProcessingException {

        return createOkResponse(
                operationDao.update(entity)).build();
    }

    /**
     *
     * @param sequence
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @DELETE
    @Path("/{sequence:[0-9]*}")
    public Response deleteById(
            @Min(1) @PathParam("sequence") long sequence)
            throws JsonProcessingException {
        
        operationDao.remove(sequence);

        return createNoContentResponse().build();
    }

}
