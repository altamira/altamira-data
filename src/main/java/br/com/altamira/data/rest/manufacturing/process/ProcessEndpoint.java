package br.com.altamira.data.rest.manufacturing.process;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Size;
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

import br.com.altamira.data.model.manufacturing.process.Process;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.data.dao.manufacturing.process.ProcessDao;
import br.com.altamira.data.rest.BaseEndpoint;
import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 *
 */
@Path("manufacturing/process")
@RequestScoped
public class ProcessEndpoint
        extends BaseEndpoint<Process> /*implements Endpoint<Process> See https://issues.jboss.org/browse/WFLY-2724*/ {

    @Inject
    private ProcessDao processDao;

    /**
     *
     * @param startPosition
     * @param maxResult
     * @param headers
     * @return
     * @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult)
            throws IOException {

        List<Process> list;

        list = processDao.list(startPosition, maxResult);

        return createOkResponse(list).build();
    }

    /**
     *
     * @param code
     * @param headers
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("/{code:[a-zA-Z0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(
            @Size(min = 5) @PathParam("code") String code)
            throws JsonProcessingException {

        return createOkResponse(
                processDao.find(code)).build();
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
            @Size(min = 2) @QueryParam("search") String search)
            throws IOException {

        return createOkResponse(
                processDao.search(search, startPosition, maxResult)).build();
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
            @NotNull(message = ENTITY_VALIDATION) Process entity)
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

        return createCreatedResponse(
                processDao.create(entity)).build();
    }

    /**
     *
     * @param code
     * @param entity
     * @param headers
     * @return
     * @throws JsonProcessingException
     */
    @PUT
    @Path("/{code:[a-zA-Z0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("code") String code,
            @NotNull(message = ProcessDao.ENTITY_VALIDATION) Process entity)
            throws JsonProcessingException {

        return createOkResponse(
                processDao.update(entity)).build();
    }

    /**
     *
     * @param code
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @DELETE
    @Path("/{code:[a-zA-Z0-9]*}")
    public Response delete(
            @Min(1) @PathParam("code") String code)
            throws JsonProcessingException {

        processDao.remove(code);

        return createNoContentResponse().build();
    }
}
