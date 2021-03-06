package com.salesmanager.core.business.catalog.product.dao.file;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.QProduct;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.catalog.product.model.file.QDigitalProduct;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Repository("digitalProductDao")
public class DigitalProductDaoImpl extends SalesManagerEntityDaoImpl<Long, DigitalProduct> 
	implements DigitalProductDao {
	
	@Override
	public DigitalProduct getByProduct(MerchantStore store, Product product) {
		
		QDigitalProduct qDigitalProduct = QDigitalProduct.digitalProduct;
		QProduct qProduct = QProduct.product;
		
		JPQLQuery query = new JPAQuery (getEntityManager());
		
		query.from(qDigitalProduct)
			.innerJoin(qDigitalProduct.product, qProduct).fetch()
			.innerJoin(qProduct.merchantStore).fetch()
			.where(qProduct.merchantStore.id.eq(store.getId())
					.and(qProduct.id.eq(product.getId())));
		
		return query.uniqueResult(qDigitalProduct);
	}
}
