package br.com.altamira.data.dao.sales;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.sales.Order;
import br.com.altamira.data.model.sales.OrderItem;
import br.com.altamira.data.model.sales.OrderItemProduct;
import br.com.altamira.data.model.sales.Product;

@Named
@Stateless
public class OrderDao {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject 
	private ProductDao productDao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Order> list(int startPosition, int maxResult) {

		TypedQuery<Order> findAllQuery = entityManager.createNamedQuery("Order.list", Order.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	public Order findByNumber(long number) {
        Order entity;

		TypedQuery<Order> findByNumberQuery = entityManager.createNamedQuery("Order.findByNumber", Order.class);
        findByNumberQuery.setParameter("number", number);
        
        try {
            entity = findByNumberQuery.getSingleResult();
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
    	
    	// Resolve dependencies
    	for (OrderItem item : entity.getItem()) {
    		item.setOrder(entity);
    		for (OrderItemProduct itemProduct : item.getProduct()) {
    			itemProduct.setOrderItem(item);
   				Product product = productDao.findByCode(itemProduct.getCode());
    			if (product == null) {
    				product = new Product(
    						itemProduct.getCode(),
    						itemProduct.getDescription(),
    						itemProduct.getColor(),
    						itemProduct.getWidth(),
    						itemProduct.getHeight(),
    						itemProduct.getLength(),
    						itemProduct.getWeight());
    				product = productDao.create(product);
    			}
    			itemProduct.setProduct(product);
    		}
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
