/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.altamira.data.util;

import br.com.altamira.data.dao.BaseDao;
import br.com.altamira.data.dao.manufacturing.bom.BOMDao;
import br.com.altamira.data.dao.manufacturing.bom.BOMItemDao;
import br.com.altamira.data.dao.manufacturing.bom.BOMItemPartDao;
import br.com.altamira.data.dao.manufacturing.process.ConsumeDao;
import br.com.altamira.data.dao.manufacturing.process.OperationDao;
import br.com.altamira.data.dao.manufacturing.process.ProcessDao;
import br.com.altamira.data.dao.manufacturing.process.ProduceDao;
import br.com.altamira.data.dao.manufacturing.process.RevisionDao;
import br.com.altamira.data.dao.manufacturing.process.SketchDao;
import br.com.altamira.data.dao.manufacturing.process.UseDao;
import br.com.altamira.data.model.manufacturing.bom.BOM;
import br.com.altamira.data.model.manufacturing.bom.BOMItem;
import br.com.altamira.data.model.manufacturing.bom.BOMItemPart;
import br.com.altamira.data.model.manufacturing.process.Consume;
import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.model.manufacturing.process.Produce;
import br.com.altamira.data.model.manufacturing.process.Revision;
import br.com.altamira.data.model.manufacturing.process.Sketch;
import br.com.altamira.data.model.manufacturing.process.Use;
import java.util.logging.Logger;
import javax.ejb.EJB;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans
 *
 * <p>
 * Example injection on a managed bean field:
 * </p>
 *
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
@Dependent
public class ResourcesFactory {

    // use @SuppressWarnings to tell IDE to ignore warnings about field not being referenced directly
    @Produces
    @Default
    @PersistenceContext
    private EntityManager entityManager;

    /**
     *
     * @param injectionPoint
     * @return
     */
    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        String resourcePath = injectionPoint.getMember().getDeclaringClass().getName();
        return Logger.getLogger(resourcePath);
    }

    /*@EJB
    private UseDao useDao;

    @Produces
    public BaseDao<Use> getUseDao() {
        return useDao;
    }

    @EJB
    private BOMItemDao bomItemDao;

    @Produces
    public BaseDao<BOMItem> getBOMItemDao() {
        return bomItemDao;
    }

    @EJB
    private BOMItemPartDao bomItemPartDao;

    @Produces
    public BaseDao<BOMItemPart> getBOMItemPartDao() {
        return bomItemPartDao;
    }

    @EJB
    private BOMDao bomDao;

    @javax.ws.rs.Produces
    public BaseDao<BOM> getBOMDao() {
        return bomDao;
    }

    @EJB
    private ConsumeDao consumeDao;

    @javax.ws.rs.Produces
    public BaseDao<Consume> getDao() {
        return consumeDao;
    }

    @EJB
    private OperationDao operationDao;

    @javax.ws.rs.Produces
    public BaseDao<Operation> getOperationDao() {
        return operationDao;
    }

    @EJB
    private ProcessDao processDao;

    @Produces
    public BaseDao<br.com.altamira.data.model.manufacturing.process.Process> getProcessDao() {
        return processDao;
    }

    @EJB
    private ProduceDao produceDao;

    @javax.ws.rs.Produces
    public BaseDao<Produce> getProduceDao() {
        return produceDao;
    }

    @EJB
    private RevisionDao revisionDao;

    @javax.ws.rs.Produces
    public BaseDao<Revision> getRevisionDao() {
        return revisionDao;
    }

    @EJB
    private SketchDao sketchDao;

    @javax.ws.rs.Produces
    public BaseDao<Sketch> getSketchDao() {
        return sketchDao;
    }*/

}
