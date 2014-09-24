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

import br.com.altamira.data.model.sales.Product;

@Named
@Stateless
public class ProductDao {
	@PersistenceContext
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public List<Product> list(int startPosition, int maxResult) {

		TypedQuery<Product> findAllQuery = entityManager.createNamedQuery("Product.list", Product.class);

		findAllQuery.setFirstResult(startPosition);
		findAllQuery.setMaxResults(maxResult);

		return findAllQuery.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Product find(Product material) {
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) 
	public Product find(String lamination, String treatment,
			BigDecimal thickness, BigDecimal width, BigDecimal length) {

		List<Product> materials = entityManager
				.createNamedQuery("Product.findUnique", Product.class)
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
	public Product find(long id) {
		return entityManager.find(Product.class, id);
	}

	public Product create(Product entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() != null && entity.getId() > 0) {
			throw new IllegalArgumentException("To create this entity, id must be null or zero.");
		}
		
		entity.setId(null);
		
		entityManager.persist(entity);
		entityManager.flush();
		
		return entityManager.find(Product.class, entity.getId());
	}

	public Product update(Product entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		return entityManager.contains(entity) ? null : entityManager.merge(entity);
	}

	public Product remove(Product entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity can't be null.");
		}
		
		if (entity.getId() == null || entity.getId() == 0l) {
			throw new IllegalArgumentException("Entity id can't be null or zero.");
		}
		
		return remove(entity.getId());
	}
	
	public Product remove(long id) {
		if (id == 0) {
			throw new IllegalArgumentException("Entity id can't be zero.");
		}
		
		Product entity = entityManager.find(Product.class, id);
        
		if (entity == null) {
			throw new IllegalArgumentException("Entity not found.");
		}

        entityManager.remove(entity);
        entityManager.flush();
		
		return entity;
	}

}
