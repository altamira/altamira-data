package br.com.altamira.data.dao.manufacturing.process;

import br.com.altamira.data.dao.BaseDao;
import static br.com.altamira.data.dao.Dao.ENTITY_VALIDATION;

import javax.ejb.Stateless;
import br.com.altamira.data.model.manufacturing.process.Operation;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 *
 * @author Alessandro
 */
@Stateless
public class OperationDao extends BaseDao<Operation> {
    
    public OperationDao() {
        this.type = Operation.class;
    }

    /**
     *
     * @param id
     * @param startPage
     * @param pageSize
     * @return
     */
    public List<Operation> list(
            @Min(value = 1, message = ID_NOT_NULL_VALIDATION) long id,
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage,
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize)
            throws ConstraintViolationException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Operation> q = cb.createQuery(Operation.class);
        Root<Operation> entity = q.from(Operation.class);

        q.select(cb.construct(Operation.class,
                entity.get("id"),
                entity.get("sequence"),
                entity.get("name")));

        q.where(cb.equal(entity.get("process"), id));

        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     *
     * @return
     */
    @Override
    public Operation find(
            @Min(value = 1, message = ID_NOT_NULL_VALIDATION) long id)
            throws ConstraintViolationException, NoResultException {

        Operation entity = super.find(id);

        entity.getConsume().size();
        entity.getProduce().size();

        return entity;
    }
    
    /**
     *
     * @param entity
     * @return
     * @throws ConstraintViolationException
     */
    @Override
    public Operation create(
            @NotNull(message = ENTITY_VALIDATION) Operation entity)
            throws ConstraintViolationException {
        
        entity.getConsume().stream().forEach((c) -> {
            c.setOperation(entity);
        });
        
        entity.getProduce().stream().forEach((p) -> {
            p.setOperation(entity);
        });
        
        return super.create(entity);
    }

    /**
     *
     * @param entity
     * @return
     * @throws ConstraintViolationException
     * @throws IllegalArgumentException
     */
    @Override
    public Operation update(
            @NotNull(message = ENTITY_VALIDATION) Operation entity)
            throws ConstraintViolationException, IllegalArgumentException {
        
        entity.getConsume().stream().forEach((c) -> {
            c.setOperation(entity);
        });
        
        entity.getProduce().stream().forEach((p) -> {
            p.setOperation(entity);
        });
        
        return super.update(entity);
    }   
}
