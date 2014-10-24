package br.com.altamira.data.dao.manufacturing.process;

import br.com.altamira.data.dao.BaseDao;
import static br.com.altamira.data.dao.Dao.NUMBER_VALIDATION;
import java.util.List;

import javax.ejb.Stateless;
import br.com.altamira.data.model.manufacturing.process.Operation;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;

/**
 *
 * 
 */
@Stateless
public class OperationDao extends BaseDao<Operation> {

    public static final String CODE_VALIDATION = "Invalid code, must be char or numbers";

    /**
     *
     * @param processId
     * @param startPage
     * @param pageSize
     * @return
     */
    public List<Operation> list(
            @Min(value = 1, message = ID_NOT_NULL_VALIDATION) long processId,
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
        
        q.where(cb.equal(entity.get("process"), processId));

        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

        /**
     *
     * @param operationId
     * @return
     */
    @Override
    public Operation find(
            @Min(value = 1, message = NUMBER_VALIDATION) long operationId)
            throws ConstraintViolationException, NoResultException {

        Operation entity = super.find(operationId);
        
        entity.getConsume().size();

        return entity;
    }
}
