package br.com.altamira.data.rest.sales;

import java.io.IOException;
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

import br.com.altamira.data.dao.sales.ProductDao;
import br.com.altamira.data.model.sales.Product;
import br.com.altamira.data.rest.BaseEndpoint;
import static br.com.altamira.data.rest.BaseEndpoint.ENTITY_VALIDATION;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriBuilderException;

/**
 *
 * 
 */
@RequestScoped
@Path("sales/product")
public class ProductEndpoint extends BaseEndpoint<Product> {

    @EJB
    private ProductDao productDao;

    public ProductEndpoint() {
        this.type = ProductEndpoint.class;
    }
    
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

        return createListResponse(
                productDao.list(startPosition, maxResult)).build();
    }

    /**
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("/{id:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(
            @Min(1) @PathParam("id") long id)
            throws JsonProcessingException {

        return createEntityResponse(productDao.find(id)).build();
    }
    
    /**
     *
     * @param code
     * @return
     */
    /*@GET
    @Path("/{code:[a-zA-Z0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("code") String code) {
        Product entity = null;

        try {
            entity = productDao.find(code);
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(entity).build();
    }*/


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
            @NotNull(message = ENTITY_VALIDATION) Product entity)
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

        return createCreatedResponse(productDao.create(entity)).build();
    }
    

    /**
     *
     * @param id
     * @param entity
     * @return
     * @throws JsonProcessingException
     */
    @PUT
    @Path(value = "{id:[0-9]*}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response update(
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "id") long id,
            @NotNull(message = ENTITY_VALIDATION) Product entity)
            throws JsonProcessingException {

        productDao.update(entity);
        
        return createNoContentResponse().build();
    }
    
    /**
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @DELETE
    @Path("/{id:[0-9]*}")
    public Response delete(
            @Min(1) @PathParam("id") long id) 
            throws JsonProcessingException {

        productDao.remove(id);

        return createNoContentResponse().build();
    }
}
