package br.com.altamira.data.dao.sales;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.sales.OrderItem;

@Named
@Stateless
public class OrderItemDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<OrderItem> list(int startPosition, int maxResult) {

		TypedQuery<OrderItem> findAllQuery = entityManager.createNamedQuery("OrderItem.list", OrderItem.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public OrderItem find(OrderItem material) {
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public OrderItem find(String lamination, String treatment,
			BigDecimal thickness, BigDecimal width, BigDecimal length) {

		List<OrderItem> materials = entityManager
				.createNamedQuery("OrderItem.findUnique", OrderItem.class)
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
	public OrderItem find(long id) {
		return entityManager.find(OrderItem.class, id);
	}

	public OrderItem create(OrderItem entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() != null && entity.getId() > 0) {
			throw new IllegalArgumentException("To create this entity, id must be null or zero.");
		}
		
		entity.setId(null);
		
		entityManager.persist(entity);
		entityManager.flush();
		
		return entityManager.find(OrderItem.class, entity.getId());
	}

	public OrderItem update(OrderItem entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		return entityManager.contains(entity) ? null : entityManager.merge(entity);
	}

	public OrderItem remove(OrderItem entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		return remove(entity.getId());
	}
	
	public OrderItem remove(long id) {
		if (id == 0) {
			throw new IllegalArgumentException("Entity id can't be zero.");
		}
		
		OrderItem entity = entityManager.find(OrderItem.class, id);
        
		if (entity == null) {
			throw new IllegalArgumentException("Entity not found.");
		}

        entityManager.remove(entity);
        entityManager.flush();
		
		return entity;
	}

	
}
