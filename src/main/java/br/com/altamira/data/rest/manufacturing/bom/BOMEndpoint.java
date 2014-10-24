/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest.manufacturing.bom;

import br.com.altamira.data.dao.manufacturing.bom.BOMDao;
import br.com.altamira.data.model.manufacturing.bom.BOM;
import br.com.altamira.data.rest.BaseEndpoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Date;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

/**
 *
 * Bill of Material rest services
 */
@Path("/manufacturing/bom")
public class BOMEndpoint
        extends BaseEndpoint<BOM> /*implements Endpoint<Process> See https://issues.jboss.org/browse/WFLY-2724*/ {

    @Inject
    private BOMDao bomDao;

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

        return createOkResponse(
                bomDao.listUnchecked(startPosition, maxResult)).build();
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
                bomDao.search(search, startPosition, maxResult)).build();
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
            @Min(1) @PathParam("number") long number)
            throws JsonProcessingException {

        return createOkResponse(bomDao.findByNumber(number)).build();
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
            @NotNull(message = ENTITY_VALIDATION) BOM entity)
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

        return createCreatedResponse(bomDao.create(entity)).build();
    }

    /**
     *
     * @param number
     * @param headers
     * @return
     * @throws JsonProcessingException
     */
    @PUT
    @Path(value = "{number:[0-9]*}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response update(
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "number") long number,
            @NotNull(message = ENTITY_VALIDATION) BOM entity)
            throws JsonProcessingException {
        
        //BOM entity = bomDao.findByNumber(number);

        // Add the current date to checked field
        //Date date = new Date();
        //entity.setChecked(date);

        return createOkResponse(bomDao.update(entity)).build();
    }

    /**
     *
     * @param number
     * @param headers
     * @return
     * @throws JsonProcessingException
     */
    @PUT
    @Path(value = "{number:[0-9]*}/checked")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response update(
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "number") long number)
            throws JsonProcessingException {
        
        BOM entity = bomDao.findByNumber(number);

        // Add the current date to checked field
        Date date = new Date();
        entity.setChecked(date);

        return createOkResponse(bomDao.update(entity)).build();
    }
    
    /**
     *
     * @param number
     * @param headers
     * @return
     * @throws JsonProcessingException
     */
    @DELETE
    @Path("/{number:[0-9]*}")
    public Response deleteByNumber(
            @Min(1) @PathParam("number") long number) throws JsonProcessingException {

        bomDao.remove(number);

        return createNoContentResponse().build();
    }

}
