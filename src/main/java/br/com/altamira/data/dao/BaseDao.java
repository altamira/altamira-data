///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.com.altamira.data.dao;
//
//import br.com.altamira.data.model.Resource;
//
//import java.lang.reflect.ParameterizedType;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.logging.Logger;
//
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.NoResultException;
//import javax.persistence.Persistence;
//import javax.persistence.PersistenceContext;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import javax.validation.ValidationException;
//import javax.validation.Validator;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//
///**
// *
// * @author Alessandro
// * @param <T>
// */
//public abstract class BaseDao<T extends Resource> implements Dao<T> {
//
//    public static final String START_PAGE_VALIDATION = "Invalid start page number, must be greater than 0.";
//    public static final String PAGE_SIZE_VALIDATION = "Invalid page size, must be greater than 0.";
//    public static final String NUMBER_VALIDATION = "Invalid number, must be greater than zero.";
//    public static final String ENTITY_VALIDATION = "Entity can't be null.";
//    public static final String ID_NULL_VALIDATION = "Entity id must be null or zero.";
//    public static final String ID_NOT_NULL_VALIDATION = "Entity id can't be null or zero.";
//    public static final String SEARCH_VALIDATION = "Search word can't be null and size must be greater that 2 characters.";
//
//    @Inject
//    private Logger log;
//
//    private EntityManager entityManager;
//
//    @Inject
//    private Validator validator;
//
//    private final Class<T> type;
//
//    public BaseDao(Class<T> type, EntityManager em) {
//        super();
//        this.type = type;
//        this.entityManager = em;
//    }
//
//    /**
//     *
//     * @param startPage
//     * @param pageSize
//     * @return
//     */
//    @Override
//    public List<T> list(
//            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage,
//            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize)
//            throws ConstraintViolationException {
//
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<T> q = cb.createQuery(type);
//        Root<T> entity = q.from(type);
//
//        q.select(entity);
//
//        return entityManager.createQuery(q)
//                .setFirstResult(startPage * pageSize)
//                .setMaxResults(pageSize)
//                .getResultList();
//    }
//
//    /**
//     *
//     * @param id
//     * @return
//     */
//    @Override
//    public T findById(
//            @Min(value = 1, message = NUMBER_VALIDATION) long id)
//            throws ConstraintViolationException, NoResultException {
//
//        T entity = entityManager.find(type, id);
//
//        return entity;
//    }
//
//    /**
//     *
//     * @param entity
//     * @return
//     */
//    @Override
//    public T create(
//            @NotNull(message = ENTITY_VALIDATION) T entity)
//            throws ConstraintViolationException {
//
//        if (entity.getId() != null && entity.getId() > 0) {
//            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
//        }
//
//        entity.setId(null);
//
//        validate(entity);
//
//        entityManager.persist(entity);
//        entityManager.flush();
//
//        // Reload to update child references
//        return entityManager.find(type, entity.getId());
//    }
//
//    /**
//     *
//     * @param entity
//     * @return
//     */
//    @Override
//    public T update(
//            @NotNull(message = ENTITY_VALIDATION) T entity)
//            throws ConstraintViolationException, IllegalArgumentException {
//
//        if (entity.getId() == null || entity.getId() == 0l) {
//            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
//        }
//
//        validate(entity);
//
//        entityManager.merge(entity);
//        entityManager.flush();
//
//        // Reload to update child references
//        return entityManager.find(type, entity.getId());
//    }
//
//    /**
//     *
//     * @param entity
//     */
//    @Override
//    public void remove(
//            @NotNull(message = ENTITY_VALIDATION) T entity)
//            throws ConstraintViolationException, IllegalArgumentException {
//
//        if (entity.getId() == null || entity.getId() <= 0) {
//            throw new IllegalArgumentException(ID_NOT_NULL_VALIDATION);
//        }
//
//        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
//    }
//
//    /**
//     *
//     * @param id
//     */
//    @Override
//    public void remove(
//            @Min(value = 1, message = NUMBER_VALIDATION) long id)
//            throws ConstraintViolationException, NoResultException {
//
//        remove(findById(id));
//    }
//
//    /**
//     * <p>
//     * Validates the given Member variable and throws validation exceptions
//     * based on the type of error. If the error is standard bean validation
//     * errors then it will throw a ConstraintValidationException with the set of
//     * the constraints violated.
//     * </p>
//     * <p>
//     * If the error is caused because an existing member with the same email is
//     * registered it throws a regular validation exception so that it can be
//     * interpreted separately.
//     * </p>
//     *
//     * @param member Member to be validated
//     * @throws ConstraintViolationException If Bean Validation errors exist
//     * @throws ValidationException If member with the same email already exists
//     */
//    private void validate(T entity) throws ConstraintViolationException {
//        // Create a bean validator and check for issues.
//        Set<ConstraintViolation<T>> violations = validator.validate(entity);
//
//        if (!violations.isEmpty()) {
//            throw new ConstraintViolationException(new HashSet<>(violations));
//        }
//    }
//
//    private Class<?> getTypeClass() {
//        Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass()
//                .getGenericSuperclass()).getActualTypeArguments()[1];
//        return clazz;
//    }
//}
