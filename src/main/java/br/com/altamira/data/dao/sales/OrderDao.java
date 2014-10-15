package br.com.altamira.data.dao.sales;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.altamira.data.model.sales.Order;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Sales order persistency strategy
 *
 */
@Stateless
public class OrderDao {

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
     * @param startPage
     * @param pageSize
     * @return
     */
    public List<Order> list(
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
            throws ConstraintViolationException {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> q = cb.createQuery(Order.class);
        Root<Order> entity = q.from(Order.class);

        q.select(cb.construct(Order.class, entity.get("number"), entity.get("customer")));

        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     *
     * @param startPage
     * @param pageSize
     * @return
     */
    public List<Order> listUnchecked(
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
            throws ConstraintViolationException, NoResultException {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> q = cb.createQuery(Order.class);
        Root<Order> entity = q.from(Order.class);

        q.select(cb.construct(Order.class, entity.get("number"), entity.get("customer")));
        q.where(cb.isNotNull(entity.get("checked")));

        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    
    /**
     *
     * @param search
     * @param startPage
     * @param pageSize
     * @return
     */
    public List<Order> search (
            @NotNull @Size(min = 2, message = SEARCH_VALIDATION) String search,
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
        throws ConstraintViolationException, NoResultException {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> q = cb.createQuery(Order.class);
        Root<Order> entity = q.from(Order.class);
        
        String searchCriteria = "%" + search.toLowerCase().trim() + "%";
        
        q.select(cb.construct(Order.class, entity.get("number"), entity.get("customer")));
        
        q.where(cb.or(
                cb.like(cb.lower(entity.get("number").as(String.class)), searchCriteria),
                cb.like(cb.lower(entity.get("customer")), searchCriteria)));
        
        log.log(Level.INFO, "Searching for {0}...", searchCriteria);
        
        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
         
    }
    
    /**
     *
     * @param number
     * @return
     */
    public Order findByNumber(
            @Min(value = 1, message = NUMBER_VALIDATION) long number) 
            throws ConstraintViolationException, NoResultException  {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> q = cb.createQuery(Order.class);
        Root<Order> entity = q.from(Order.class);

        q.select(entity).where(cb.equal(entity.get("number"), number));

        Order order = entityManager.createQuery(q).getSingleResult();

        // Lazy load of items
        if (order.getItems() != null) {
            order.getItems().size();
            order.getItems().stream().forEach((item) -> {
                item.getParts().size();
            });
        }

        return order;
    }
    
    /**
     *
     * @param entity
     * @return
     */
    public Order create(
            @NotNull(message = ENTITY_VALIDATION) Order entity) 
            throws ConstraintViolationException {

        if (entity.getId() != null && entity.getId() > 0) {
            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
        }

        // Resolve dependencies
        entity.getItems().stream().map((item) -> {
            item.setOrder(entity);
            return item;
        }).forEach((item) -> {
            item.getParts().stream().forEach((part) -> {
                part.setOrderItem(item);
                /*Product product = productDao.findByCode(part.getCode());
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
                part.setProduct(product);*/
            });
        });

        entity.setId(null);

        validate(entity);
        
        entityManager.persist(entity);
        entityManager.flush();

        // Reload to update child references
        return entityManager.find(Order.class, entity.getId());
    }

    /**
     *
     * @param entity
     * @return
     */
    public Order update(
            @NotNull(message = ENTITY_VALIDATION) Order entity)
            throws ConstraintViolationException, IllegalArgumentException {

        if (entity.getId() == null || entity.getId() == 0l) {
            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
        }

        // Resolve dependencies
        entity.getItems().stream().map((item) -> {
            item.setOrder(entity);
            return item;
        }).forEach((item) -> {
            item.getParts().stream().forEach((part) -> {
                part.setOrderItem(item);
            });
        });

        validate(entity);
        
        entityManager.merge(entity);
        entityManager.flush();

        // Reload to update child references
        return entityManager.find(Order.class, entity.getId());
    }

    /**
     *
     * @param number
     */
    public void updateToUnchecked(
            @Min(value = 1, message = NUMBER_VALIDATION) long number) {
        
        Order order = findByNumber(number);
        
        order.setChecked(null);
        
        update(order);
        
    }

    public void updateToChecked(
            @Min(value = 1, message = NUMBER_VALIDATION) long number) {
        
        Order order = findByNumber(number);
        
        order.setChecked(new Date());
        
        update(order);
        
    }
    
    /**
     *
     * @param entity
     */
    public void remove(
            @NotNull(message = ENTITY_VALIDATION) Order entity) 
            throws ConstraintViolationException, IllegalArgumentException {

        if (entity.getId() == null || entity.getId() <= 0) {
            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
        }
        
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    /**
     *
     * @param number
     */
    public void remove(
            @Min(value = 1, message = NUMBER_VALIDATION) long number) 
            throws ConstraintViolationException, NoResultException {

        remove(findByNumber(number));
    }

    /**
     * <p>
     * Validates the given Member variable and throws validation exceptions
     * based on the type of error. If the error is standard bean validation
     * errors then it will throw a ConstraintValidationException with the set of
     * the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing member with the same email is
     * registered it throws a regular validation exception so that it can be
     * interpreted separately.
     * </p>
     *
     * @param member Member to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If member with the same email already exists
     */
    private void validate(Order entity) throws ConstraintViolationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Order>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
    }
}
