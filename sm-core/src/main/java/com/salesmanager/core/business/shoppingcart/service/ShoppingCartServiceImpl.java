package com.salesmanager.core.business.shoppingcart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.FinalPrice;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.shoppingcart.dao.ShoppingCartDao;
import com.salesmanager.core.business.shoppingcart.dao.ShoppingCartItemDao;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartAttributeItem;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;

@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends SalesManagerEntityServiceImpl<Long, ShoppingCart> implements ShoppingCartService {


	private ShoppingCartDao shoppingCartDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ShoppingCartItemDao shoppingCartItemDao;
	
	@Autowired
	private PricingService pricingService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);
	
	@Autowired
	public ShoppingCartServiceImpl(
			ShoppingCartDao shoppingCartDao) {
		super(shoppingCartDao);
		this.shoppingCartDao = shoppingCartDao;

	}
	
	
	/**
	 * Retrieve a {@link ShoppingCart} cart for a given customer
	 */
	public ShoppingCart getShoppingCart(Customer customer) throws ServiceException {

		try {

			ShoppingCart shoppingCart = shoppingCartDao.getByCustomer(customer);
			return populateShoppingCart(shoppingCart);
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}
	
	/**
	 * Save or update a {@link ShoppingCart} for a given customer
	 */
	public void saveOrUpdate(ShoppingCart shoppingCart) throws ServiceException {
		if(shoppingCart.getId()==null || shoppingCart.getId().longValue()==0) {
			super.create(shoppingCart);
		} else {
			super.update(shoppingCart);
		}
	}
	
	/**
	 * Get a {@link ShoppingCart} for a given id and MerchantStore. Will update the shopping cart
	 * prices and items based on the actual inventory.
	 */
	@Override
	public ShoppingCart getById(Long id, MerchantStore store) throws ServiceException {

		try {
			ShoppingCart shoppingCart = shoppingCartDao.getById(id, store);
			return populateShoppingCart(shoppingCart);
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	@Override
	public ShoppingCart getByCode(String code, MerchantStore store) throws ServiceException {

		try {
			ShoppingCart shoppingCart = shoppingCartDao.getByCode(code, store);
			return populateShoppingCart(shoppingCart);
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}
	
	@Override
	public ShoppingCart getByCustomer(Customer customer) throws ServiceException {
		
		try {
			ShoppingCart shoppingCart = shoppingCartDao.getByCustomer(customer);
			return populateShoppingCart(shoppingCart);
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	

	
	@Transactional
	private ShoppingCart populateShoppingCart(ShoppingCart shoppingCart) throws Exception {
		
		try {

			if(shoppingCart!=null) {
				
				Set<ShoppingCartItem> items = shoppingCart.getLineItems();
				if(items==null || items.size()==0) {
					
					shoppingCartDao.delete(shoppingCart);
					return null;
					
				}
				
				Set<ShoppingCartItem> shoppingCartItems = new HashSet<ShoppingCartItem>();
				for(ShoppingCartItem item : items) {
					this.populateItem(item);
					if(item.getProduct()==null) {//product has been removed
						LOGGER.debug("Removing shopping cart item for product id " + item.getProductId());
						shoppingCartItemDao.delete(item);
					} else {
						shoppingCartItems.add(item);
					}
				}
				
				shoppingCart.setLineItems(shoppingCartItems);
				
				if(shoppingCart.getLineItems().size()==0) {
					LOGGER.debug("Deleting cart with id " + shoppingCart.getId());
					shoppingCartDao.delete(shoppingCart);
					return null;
				}
				
				return shoppingCart;
			}
		
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		return null;
		
	}
	
	public ShoppingCartItem createShoppingCartItem(Product product, List<ProductAttribute> attributes) throws ServiceException {
		Validate.notNull(product, "Product should not be null");
		Validate.notNull(product.getMerchantStore(), "Product.merchantStore should not be null");
		return null;
	}
	
	private ShoppingCartItem createShoppingCartItem(ShoppingCart shoppingCart, Product product, List<ProductAttribute> attributes, Customer customer) throws ServiceException {
		
		//TODO validate if product already exist
		
		ShoppingCartItem item = new ShoppingCartItem(shoppingCart, product);
		
		if(attributes!=null && attributes.size()>0) {
			
			
			
			
			
		}
		
/*		Set<ProductAttribute> productAttributes = product.getAttributes();
		List<ProductAttribute> attributesList = new ArrayList<ProductAttribute>();
		if(productAttributes!=null && productAttributes.size()>0 && attributes!=null && attributes.size()>0) {
			for(ShoppingCartAttributeItem attribute : attributes) {
				long attributeId = attribute.getProductAttributeId().longValue();
				for(ProductAttribute productAttribute : productAttributes) {
					
					if(productAttribute.getId().longValue()==attributeId) {
						attribute.setProductAttribute(productAttribute);
						attributesList.add(productAttribute);
						break;
					}
					
				}
				
			}
		} else {
			
			if(productAttributes!=null && productAttributes.size()>0) {
				LOGGER.debug("Removing attributes for shopping cart item " + item.getId());
				item.setAttributes(null);//TODO check should update shopping cart
			}
			
		}
		*/
		//set item price
		FinalPrice price = null;
		
		if(attributes!=null && attributes.size()>0) {
			price = pricingService.calculateProductPrice(product, attributes);
		} else {
			price = pricingService.calculateProductPrice(product);
		}
			
			
			
		item.setItemPrice(price.getFinalPrice());
		
		return item;
		
		
		
	}
	
	@Transactional
	private void populateItem(ShoppingCartItem item) throws Exception {
		
		Long productId = item.getProductId();

		Product product = productService.getById(productId);
		
		if(product==null) {
			return;
		}
		
		item.setProduct(product);
		
		Set<ShoppingCartAttributeItem> attributes = item.getAttributes();
		Set<ProductAttribute> productAttributes = product.getAttributes();
		List<ProductAttribute> attributesList = new ArrayList<ProductAttribute>();
		if(productAttributes!=null && productAttributes.size()>0 && attributes!=null && attributes.size()>0) {
			for(ShoppingCartAttributeItem attribute : attributes) {
				long attributeId = attribute.getProductAttributeId().longValue();
				for(ProductAttribute productAttribute : productAttributes) {
					
					if(productAttribute.getId().longValue()==attributeId) {
						attribute.setProductAttribute(productAttribute);
						attributesList.add(productAttribute);
						break;
					}
					
				}
				
			}
		} else {
			
			if(productAttributes!=null && productAttributes.size()>0) {
				LOGGER.debug("Removing attributes for shopping cart item " + item.getId());
				item.setAttributes(null);//TODO check should update shopping cart
			}
			
		}
		
		//set item price
		FinalPrice price = pricingService.calculateProductPrice(product, attributesList);
		item.setItemPrice(price.getFinalPrice());
		

		BigDecimal subTotal = item.getItemPrice().multiply(new BigDecimal(item.getQuantity().intValue()));
		item.setSubTotal(subTotal);
		
	}
	
	
	
	
	public void calculateShoppingCart(List<ShoppingCartItem> items) throws ServiceException {
		
		
/*		
 		SHOULD BE IN ORDER PACKAGE
 		
 		the model should be completed so that a shopping cart has this structure
		
		ShoppingCart
			List<ShoppingCartItem> items
				List<ShoppingCartAttributeItem> priceItems
			
		only ShoppingCartItem are saved in the database when a customer is logged on
		
		
		This method should return a ShoppingCart entity
		
		
		- price calculation
		
		the cart price calculation is based on a workflow. The workflow takes the ShoppingCart as input and populates the ShoppingCartPriceItem
		based on different calculation steps. The steps are individual item price calculation, sub-total, apply tax (when customer is known), eventually apply
		custom pricing rules (shopping cart coupons) and other specific rule that can be configured on business rules engine such as drools
		
		STEP 1 Unit price item calculation
		
		:get the Product from ShoppingCartItem and all ProductAttribute fromShoppingCartAttributeItem
		
		:invoke for each product productPriceUtils.getFinalProductPrice
		
		:get the final price for each Product
		
		:set the price in shoppingCartItem.itemPrice
		
		STEP 2 Calculate cart sub total
		
		:calculate sub total based on each shoppingCartItem.itemPrice, [create a ShoppingCartPriceItem that will contain this sub total] -- Not sure about that
		
		STEP 3 Calculate taxes (if the customer is logged on) taxService + OrderSummary
		
		STEP 4 Calculate Cart total
		
		
		WORKFLOW example (package name may not be accurate)
		
		<beans:bean id="shoppingCartWorkflow" class="com.salesmanager.core.service.shoppingcart.workflow.WorkflowProcessor">
			<beans:property name="processes">
				<beans:list>
					<beans:ref bean="a" />
					<beans:ref bean="b" />
					<beans:ref bean="c" />
				</beans:list>
			</beans:property>
		</beans:bean>
		


		<beans:bean id="a" class="com.salesmanager.core.service.shoppingcart.workflow.CalculateThis"/>
		<beans:bean id="b" class="com.salesmanager.core.service.shoppingcart.workflow.CalculateThat"/>
		<beans:bean id="c" class="com.salesmanager.core.service.shoppingcart.workflow..."/>

		In the shopping cart service autowire the workflow processor which implements execute
		
		
		*/
		
		
		
		
	}
	
	


}
