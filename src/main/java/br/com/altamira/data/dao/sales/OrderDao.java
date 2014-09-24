package br.com.altamira.data.dao.sales;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.sales.Order;
import br.com.altamira.data.model.sales.OrderItem;

@Named
@Stateless
public class OrderDao {
	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Order> list(int startPosition, int maxResult) {

		TypedQuery<Order> findAllQuery = entityManager.createNamedQuery("Order.list", Order.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<OrderItem> listItems(Long requestId, int startPosition, int maxResult) {

		TypedQuery<OrderItem> findAllQuery = entityManager.createNamedQuery("Order.items", OrderItem.class);
		findAllQuery.setParameter("requestId", requestId);
		
		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	public Order find(long id) {
        Order entity;

		TypedQuery<Order> findByIdQuery = entityManager.createNamedQuery("Order.findById", Order.class);
        findByIdQuery.setParameter("id", id);
        
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
        
        // Lazy load of items
        if (entity.getItem() != null) {
        	entity.getItem().size();
        }

        return entity;
	}
	
	public Order create(Order entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() != null && entity.getId() > 0) {
			throw new IllegalArgumentException("To create this entity, id must be null or zero.");
		}
		
		entity.setId(null);

		entityManager.persist(entity);
		entityManager.flush();
		
		// Reload to update child references
		
		return entityManager.find(Order.class, entity.getId());
	}
	
	public Order update(Order entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		entityManager.merge(entity);

		// Reload to update child references
		
		return entityManager.find(Order.class, entity.getId());
	}
	
	public Order remove(Order entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		return remove(entity.getId());
	}
	
	public Order remove(long id) {
		if (id == 0) {
			throw new IllegalArgumentException("Entity id can't be zero.");
		}
		
		//entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));

		Order entity = entityManager.find(Order.class, id);
        
		if (entity == null) {
			throw new IllegalArgumentException("Entity not found.");
		}
		
	    entityManager.remove(entity);
	    entityManager.flush();
		
		return entity;
	}

}
