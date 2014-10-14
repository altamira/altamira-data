package br.com.altamira.data.dao.manufacturing.process;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.manufacturing.process.Consume;
import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.model.manufacturing.process.Process;
import br.com.altamira.data.model.manufacturing.process.Produce;
import br.com.altamira.data.model.manufacturing.process.Revision;

/**
 *
 * @author alessandro.holanda
 */
@Named
@Stateless
public class ProcessDao {
	
	@Inject
	private EntityManager entityManager;

    /**
     *
     * @param startPosition
     * @param maxResult
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Process> list(int startPosition, int maxResult) {

		TypedQuery<Process> findAllQuery = entityManager.createNamedQuery("Process.list", Process.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
    /**
     *
     * @param requestId
     * @param startPosition
     * @param maxResult
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Operation> listOperations(Long requestId, int startPosition, int maxResult) {

		TypedQuery<Operation> findAllQuery = entityManager.createNamedQuery("Process.items", Operation.class);
		findAllQuery.setParameter("requestId", requestId);
		
		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
    /**
     *
     * @param id
     * @return
     */
    public Process find(long id) {
        Process entity;

		TypedQuery<Process> findByIdQuery = entityManager.createNamedQuery("Process.findById", Process.class);
        findByIdQuery.setParameter("id", id);
        
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
        
        // Lazy load of items
        if (entity.getOperation() != null) {
        	entity.getOperation().size();
        }

        return entity;
	}
	
    /**
     *
     * @param entity
     * @return
     */
    public Process create(Process entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() != null && entity.getId() > 0) {
			throw new IllegalArgumentException("To create this entity, id must be null or zero.");
		}
		
		// Resolve dependencies
    	for (Revision revision : entity.getRevision()) {
    		revision.setProcess(entity);	
    	}
    	for (Operation operation : entity.getOperation()) {
    		operation.setProcess(entity);
    		for (Consume consume : operation.getConsume()) {
    			consume.setOperation(operation);
   				
    		}
    		for (Produce produce : operation.getProduce()) {
    			produce.setOperation(operation);
    		}
    	}
    	
		entity.setId(null);

		entityManager.persist(entity);
		entityManager.flush();
		
		// Reload to update child references
		
		return entityManager.find(Process.class, entity.getId());
	}
	
    /**
     *
     * @param entity
     * @return
     */
    public Process update(Process entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		// Resolve dependencies
    	for (Revision revision : entity.getRevision()) {
    		revision.setProcess(entity);	
    	}
    	for (Operation operation : entity.getOperation()) {
    		operation.setProcess(entity);
    		for (Consume consume : operation.getConsume()) {
    			consume.setOperation(operation);
   				
    		}
    		for (Produce produce : operation.getProduce()) {
    			produce.setOperation(operation);
    		}
    	}
    	
		entityManager.merge(entity);

		// Reload to update child references
		
		return entityManager.find(Process.class, entity.getId());
	}
	
    /**
     *
     * @param entity
     * @return
     */
    public Process remove(Process entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		return remove(entity.getId());
	}
	
    /**
     *
     * @param id
     * @return
     */
    public Process remove(long id) {
		if (id == 0) {
			throw new IllegalArgumentException("Entity id can't be zero.");
		}
		
		//entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));

		Process entity = entityManager.find(Process.class, id);
        
		if (entity == null) {
			throw new IllegalArgumentException("Entity not found.");
		}
		
	    entityManager.remove(entity);
	    entityManager.flush();
		
		return entity;
	}
}