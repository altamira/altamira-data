package br.com.altamira.data.rest.sales;

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

import br.com.altamira.data.dao.sales.OrderDao;
import br.com.altamira.data.model.sales.Order;
import br.com.altamira.data.rest.BaseEndpoint;
import static br.com.altamira.data.rest.BaseEndpoint.ENTITY_VALIDATION;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * 
 * @author Alessandro
 */
@RequestScoped
@Path("/sales/order")
public class OrderEndpoint extends BaseEndpoint<Order> {

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

        return createOkResponse(
                orderDao.list(startPosition, maxResult)).build();
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
    public Response findByNumber(
            @Min(1) @PathParam("number") long number)
            throws JsonProcessingException {

        return createOkResponse(orderDao.findByNumber(number)).build();
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
    public Response create(
            @NotNull(message = ENTITY_VALIDATION) Order entity)
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

        return createCreatedResponse(orderDao.create(entity)).build();
    }

    /**
     *
     * @param number
     * @param entity
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "number") long number,
            @NotNull(message = ENTITY_VALIDATION) Order entity)
            throws JsonProcessingException {

        return createOkResponse(orderDao.update(entity)).build();
    }

    /**
     *
     * @param id
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @DELETE
    @Path("/{id:[0-9]*}")
    public Response delete(
            @Min(1) @PathParam("id") long id) 
            throws JsonProcessingException {

        orderDao.remove(id);

        return createNoContentResponse().build();
    }
    
}
