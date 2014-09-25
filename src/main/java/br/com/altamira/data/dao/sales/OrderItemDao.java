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
import br.com.altamira.data.model.sales.OrderItemPart;
import br.com.altamira.data.model.sales.Product;

@Named
@Stateless
public class OrderItemDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject
	private ProductDao productDao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<OrderItem> list(Long number, int startPosition, int maxResult) {

		TypedQuery<OrderItem> findAllQuery = entityManager.createNamedQuery("OrderItem.list", OrderItem.class);
		findAllQuery.setParameter("number", number);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public OrderItem find(long id) {
        OrderItem entity;

		TypedQuery<OrderItem> findByIdQuery = entityManager.createNamedQuery("OrderItem.findById", OrderItem.class);
        findByIdQuery.setParameter("id", id);
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

        return entity;
	}
	
	public OrderItem create(Order order, OrderItem entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() != null && entity.getId() > 0) {
			throw new IllegalArgumentException("To create this entity, id must be null or zero.");
		}
		
		if (entity.getOrder() == null) {
			//throw new IllegalArgumentException("Order parent not assigned");
			
			entity.setOrder(order);
		}
		
		if (!entity.getOrder().getId().equals(order.getId())) {
			throw new IllegalArgumentException("Insert item to non current Order is not allowed. Your id " + entity.getOrder().getId() + ", expected id " + order.getId());
		}
		
		entity.setId(null);
		
		for (OrderItemPart part : entity.getParts()) {
			part.setOrderItem(entity);
			Product product = productDao.findByCode(part.getCode());
			if (product == null) {
				product = new Product(
						part.getCode(),
						part.getDescription(),
						part.getColor(),
						part.getWidth(),
						part.getHeight(),
						part.getLength(),
						part.getWeight());
				product = productDao.create(product);
			}
			part.setProduct(product);
		}
		
		entityManager.persist(entity);
		entityManager.flush();
		
		// Reload to update child references

		return entityManager.find(OrderItem.class, entity.getId());
	}
	
	public OrderItem update(Order order, OrderItem entity) {
		
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		if (entity.getOrder() == null) {
			//throw new IllegalArgumentException("Order parent not assigned");
			
			entity.setOrder(order);
		}
		
		if (!entity.getOrder().getId().equals(order.getId())) {
			throw new IllegalArgumentException("Update item of non current Order is not allowed. Your id " + entity.getOrder().getId() + ", expected id " + order.getId());
		}
		
		if (entity.getParts() == null) {
			throw new IllegalArgumentException("Product is required.");
		}
		
	
		entityManager.merge(entity);

		// Reload to update child references

		return entityManager.find(OrderItem.class, entity.getId());
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
		
		//entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));

		OrderItem entity = entityManager.find(OrderItem.class, id);
        
		if (entity == null) {
			throw new IllegalArgumentException("Entity not found.");
		}
		
	    entityManager.remove(entity);
	    entityManager.flush();
		
		return entity;
	}


}
