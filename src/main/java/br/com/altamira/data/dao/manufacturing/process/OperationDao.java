package br.com.altamira.data.dao.manufacturing.process;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.altamira.data.model.manufacturing.process.Consume;
import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.model.manufacturing.process.Produce;
import br.com.altamira.data.model.manufacturing.process.Process;

/**
 *
 * 
 */
@Named
@Stateless
public class OperationDao {

    @Inject
    private EntityManager entityManager;

    /**
     *
     * @param startPosition
     * @param maxResult
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Operation> list(int startPosition, int maxResult) {

        TypedQuery<Operation> findAllQuery = entityManager.createNamedQuery("Operation.list", Operation.class);

        findAllQuery.setFirstResult(startPosition);
        findAllQuery.setMaxResults(maxResult);

        return findAllQuery.getResultList();
    }

    /**
     *
     * @param material
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Operation find(Operation material) {
        return null;
    }

    /**
     *
     * @param lamination
     * @param treatment
     * @param thickness
     * @param width
     * @param length
     * @return
     */
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

    /**
     *
     * @param id
     * @return
     */
    public Operation find(long id) {
        //return entityManager.find(Operation.class, id);
        Operation entity;

        TypedQuery<Operation> findByIdQuery = entityManager.createNamedQuery("Operation.findById", Operation.class);
        findByIdQuery.setParameter("id", id);

        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

        // Lazy load of items
        if (entity.getConsume() != null) {
            entity.getConsume().size();
        }
        if (entity.getProduce() != null) {
            entity.getProduce().size();
        }

        return entity;
    }

    /**
     *
     * @param entity
     * @return
     */
    public Operation create(Operation entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity can't be null.");
        }

        if (entity.getId() != null && entity.getId() > 0) {
            throw new IllegalArgumentException("To create this entity, id must be null or zero.");
        }

        Process process = entity.getProcess();
        entity.setProcess(process);

        for (Consume consume : entity.getConsume()) {
            consume.setOperation(entity);

        }
        for (Produce produce : entity.getProduce()) {
            produce.setOperation(entity);
        }
        entity.setId(null);

        entityManager.persist(entity);
        entityManager.flush();

        return entityManager.find(Operation.class, entity.getId());
    }

    /**
     *
     * @param entity
     * @return
     */
    public Operation update(Operation entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity can't be null.");
        }
        if (entity.getId() == null || entity.getId() == 0l) {
            throw new IllegalArgumentException("Entity id can't be null or zero.");
        }

        Process process = entity.getProcess();
        entity.setProcess(process);

        for (Consume consume : entity.getConsume()) {
            consume.setOperation(entity);

        }
        for (Produce produce : entity.getProduce()) {
            produce.setOperation(entity);
        }

        return entityManager.contains(entity) ? null : entityManager.merge(entity);
    }

    /**
     *
     * @param entity
     * @return
     */
    public Operation remove(Operation entity) {
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
