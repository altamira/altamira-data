/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest.manufacturing.process;

import br.com.altamira.data.dao.manufacturing.process.OperationDao;
import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.rest.BaseEndpoint;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alessandro
 */
@RequestScoped
public class ConsumeEndpoint extends BaseEndpoint<br.com.altamira.data.model.manufacturing.process.Process> {

    @Inject
    private OperationDao operationDao;

    /**
     *
     * @param id
     * @param startPosition
     * @param maxResult
     * @return
     * @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(
            @PathParam("operation") long operationId,
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult)
            throws IOException {

        Operation entity = operationDao.find(operationId);

        return createOkResponse(
                entity.getConsume()).build();
    }

}
