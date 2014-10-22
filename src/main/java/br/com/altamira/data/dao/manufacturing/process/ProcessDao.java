package br.com.altamira.data.dao.manufacturing.process;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.data.model.manufacturing.bom.BOM;
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
	
    public static final String START_PAGE_VALIDATION = "Invalid start page number, must be greater than 0.";
    public static final String PAGE_SIZE_VALIDATION = "Invalid page size, must be greater than 0.";
    public static final String NUMBER_VALIDATION = "Invalid number, must be greater than zero.";
    public static final String ENTITY_VALIDATION = "Entity can't be null.";
    public static final String ID_NULL_VALIDATION = "Entity id must be null or zero.";
    public static final String ID_NOT_NULL_VALIDATION = "Entity id can't be null or zero.";
    public static final String SEARCH_VALIDATION = "Search word can't be null and size must be greater that 2 characters.";
    
    @Inject
    private Logger log;
    
    @Inject
    private EntityManager entityManager;
    
    @Inject
    private Validator validator;

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
    
    /**
    *
    * @param search
    * @param startPage
    * @param pageSize
    * @return
    */
   public List<Process> search (
           @NotNull @Size(min = 2, message = SEARCH_VALIDATION) String search,
           @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
           @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
       throws ConstraintViolationException, NoResultException {
       
       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
       CriteriaQuery<Process> q = cb.createQuery(Process.class);
       Root<Process> entity = q.from(Process.class);
       
       String searchCriteria = "%" + search.toLowerCase().trim() + "%";
       
       q.select(cb.construct(Process.class, entity.get("code"), 
                                        entity.get("description")));
       
       q.where(cb.or(
               cb.like(cb.lower(entity.get("code").as(String.class)), searchCriteria),
               cb.like(cb.lower(entity.get("description")), searchCriteria)));
       
       log.log(Level.INFO, "Searching for {0}...", searchCriteria);
       
       return entityManager.createQuery(q)
               .setFirstResult(startPage * pageSize)
               .setMaxResults(pageSize)
               .getResultList();
        
   }
}
