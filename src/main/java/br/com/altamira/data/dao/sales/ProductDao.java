package br.com.altamira.data.dao.sales;

import br.com.altamira.data.dao.BaseDao;
import br.com.altamira.data.model.manufacturing.process.Produce;

import javax.ejb.Stateless;
import javax.inject.Named;

import br.com.altamira.data.model.sales.Product;

/**
 *
 * @author alessandro.holanda
 */
@Named
@Stateless
public class ProductDao extends BaseDao<Product> {

    public ProductDao() {
        this.type = Product.class;
    }

    public Product find(String code) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
