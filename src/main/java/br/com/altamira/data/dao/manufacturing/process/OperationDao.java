package br.com.altamira.data.dao.manufacturing.process;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.manufacturing.process.Operation;

@Named
@Stateless
public class OperationDao {
	
	@Inject
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Operation> list(int startPosition, int maxResult) {

		TypedQuery<Operation> findAllQuery = entityManager.createNamedQuery("Operation.list", Operation.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Operation find(Operation material) {
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Operation find(String lamination, String treatment,
			BigDecimal thickness, BigDecimal width, BigDecimal length) {

		List<Operation> materials = entityManager
				.createNamedQuery("Operation.findUnique", Operation.class)
				.setParameter("lamination", lamination)
				.setParameter("treatment", treatment)
				.setParameter("thickness", thickness)
				.setParameter("width", width)
				.setParameter("length", length)
				.getResultList();

		if (materials.isEmpty()) {
			return null;
		}

		return materials.get(0);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Operation find(long id) {
		return entityManager.find(Operation.class, id);
	}

	public Operation create(Operation entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() != null && entity.getId() > 0) {
			throw new IllegalArgumentException("To create this entity, id must be null or zero.");
		}
		
		entity.setId(null);
		
		entityManager.persist(entity);
		entityManager.flush();
		
		return entityManager.find(Operation.class, entity.getId());
	}

	public Operation update(Operation entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		return entityManager.contains(entity) ? null : entityManager.merge(entity);
	}

	public Operation remove(Operation entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		return remove(entity.getId());
	}
	
	public Operation remove(long id) {
		if (id == 0) {
			throw new IllegalArgumentException("Entity id can't be zero.");
		}
		
		Operation entity = entityManager.find(Operation.class, id);
        
		if (entity == null) {
			throw new IllegalArgumentException("Entity not found.");
		}

        entityManager.remove(entity);
        entityManager.flush();
		
		return entity;
	}

}
