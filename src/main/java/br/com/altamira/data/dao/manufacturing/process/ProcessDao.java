package br.com.altamira.data.dao.manufacturing.process;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
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

import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.model.manufacturing.process.Process;
import br.com.altamira.data.model.manufacturing.process.Revision;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

/**
 *
 * @author alessandro.holanda
 */
@Named
@Stateless
public class ProcessDao {

    public static final String START_PAGE_VALIDATION = "Invalid start page number, must be greater than 0.";
    public static final String PAGE_SIZE_VALIDATION = "Invalid page size, must be greater than 0.";
    public static final String CODE_VALIDATION = "Invalid code, must be char or numbers";
    public static final String ENTITY_VALIDATION = "Entity can't be null.";
    public static final String ID_NULL_VALIDATION = "Entity id must be null or zero.";
    public static final String ID_NOT_NULL_VALIDATION = "Entity id can't be null or zero.";
    public static final String SEARCH_VALIDATION = "Search word can't be null and size must be greater that 5 characters.";

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
    public List<Process> list(
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage,
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize)
            throws ConstraintViolationException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Process> q = cb.createQuery(Process.class);
        Root<Process> entity = q.from(Process.class);

        q.select(cb.construct(Process.class, entity.get("code"),
                entity.get("description")));

        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     *
     * @param requestId
     * @param startPosition
     * @param maxResult
     * @return
     */
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
     * @param code
     * @return
     */
    public Process find(
            @Size(min = 1, message = CODE_VALIDATION) String code)
            throws ConstraintViolationException, NoResultException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Process> q = cb.createQuery(Process.class);
        Root<Process> entity = q.from(Process.class);

        q.select(entity).where(cb.equal(entity.get("code"), code));

        Process process = entityManager.createQuery(q).getSingleResult();

        // Lazy load of items
        if (process.getOperation() != null) {
            process.getOperation().size();
            process.getOperation().stream().forEach((operation) -> {
                operation.getConsume().size();
                operation.getProduce().size();
            });
        }

        return process;
    }

    /**
     *
     * @param search
     * @param startPage
     * @param pageSize
     * @return
     */
    public List<Process> search(
            @NotNull @Size(min = 2, message = SEARCH_VALIDATION) String search,
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage,
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize)
            throws ConstraintViolationException, NoResultException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Process> q = cb.createQuery(Process.class);
        Root<Process> entity = q.from(Process.class);

        String searchCriteria = "%" + search.toLowerCase().trim() + "%";

        q.select(cb.construct(Process.class,
                entity.get("code"),
                entity.get("description")));

        q.where(cb.or(
                cb.like(cb.lower(entity.get("code")), searchCriteria),
                cb.like(cb.lower(entity.get("description")), searchCriteria)));

        log.log(Level.INFO, "Searching for {0}...", searchCriteria);

        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

    }

    /**
     *
     * @param entity
     * @return
     */
    public Process create(
            @NotNull(message = ENTITY_VALIDATION) Process entity)
            throws ConstraintViolationException {

        if (entity.getId() != null && entity.getId() > 0) {
            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
        }

        // Resolve dependencies
        for (Revision r : entity.getRevision()) {
            r.setProcess(entity);
        }
        
        entity.getOperation().stream().map((operation) -> {
            operation.setProcess(entity);
            return operation;
        }).forEach((operation) -> {
            operation.getConsume().stream().forEach((consume) -> {
                consume.setOperation(operation);
            });
            operation.getProduce().stream().forEach((produce) -> {
                produce.setOperation(operation);
            });
        });

        entity.setId(null);

        validate(entity);

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
    public Process update(
            @NotNull(message = ENTITY_VALIDATION) Process entity)
            throws ConstraintViolationException, IllegalArgumentException {

        if (entity.getId() == null || entity.getId() == 0l) {
            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
        }

        // Resolve dependencies
        entity.getOperation().stream().map((operation) -> {
            operation.setProcess(entity);
            return operation;
        }).forEach((operation) -> {
            operation.getConsume().stream().forEach((consume) -> {
                consume.setOperation(operation);
            });
            operation.getProduce().stream().forEach((produce) -> {
                produce.setOperation(operation);
            });
        });

        validate(entity);

        entityManager.merge(entity);
        entityManager.flush();

        // Reload to update child references
        return entityManager.find(Process.class, entity.getId());
    }

    /**
     *
     * @param entity
     * @return
     */
    public void remove(
            @NotNull(message = ENTITY_VALIDATION) Process entity)
            throws ConstraintViolationException, IllegalArgumentException {

        if (entity.getId() == null || entity.getId() <= 0) {
            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
        }

        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    /**
     *
     * @param id
     * @return
     */
    public void remove(
            @Min(value = 1, message = CODE_VALIDATION) String code)
            throws ConstraintViolationException, NoResultException {

        remove(find(code));
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
    private void validate(Process entity) throws ConstraintViolationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Process>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
    }
}
