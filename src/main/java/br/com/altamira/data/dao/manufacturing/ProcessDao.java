package br.com.altamira.data.dao.manufacturing;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.manufacturing.*;
import br.com.altamira.data.model.manufacturing.Process;
import br.com.altamira.data.model.sales.OrderItem;
import br.com.altamira.data.model.sales.OrderItemPart;
import br.com.altamira.data.model.sales.Product;

@Named
@Stateless
public class ProcessDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Process> list(int startPosition, int maxResult) {

		TypedQuery<Process> findAllQuery = entityManager.createNamedQuery("Process.list", Process.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Operation> listOperations(Long requestId, int startPosition, int maxResult) {

		TypedQuery<Operation> findAllQuery = entityManager.createNamedQuery("Process.items", Operation.class);
		findAllQuery.setParameter("requestId", requestId);
		
		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
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
    	
		
		entity.setId(null);

		entityManager.persist(entity);
		entityManager.flush();
		
		// Reload to update child references
		
		return entityManager.find(Process.class, entity.getId());
	}
	
	public Process update(Process entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		entityManager.merge(entity);

		// Reload to update child references
		
		return entityManager.find(Process.class, entity.getId());
	}
	
	public Process remove(Process entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		return remove(entity.getId());
	}
	
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
