package com.salesmanager.core.business.catalog.common;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionDescription;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValueDescription;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;


public class CatalogServiceHelper {
	
	/**
	 * Filters descriptions and set the appropriate language
	 * @param p
	 * @param language
	 */
	public static void setToLanguage(Product p, int language) {
		
		
	Set<ProductAttribute> attributes = p.getAttributes();
		if(attributes!=null) {
			
			for(ProductAttribute attribute : attributes) {

				ProductOption po = attribute.getProductOption();
				Set<ProductOptionDescription> spod = po.getDescriptions();
				if(spod!=null) {
					Set<ProductOptionDescription> podDescriptions = new HashSet();
					for(ProductOptionDescription pod : spod) {
						//System.out.println("    ProductOptionDescription : " + pod.getProductOptionName());
						if(pod.getLanguage().getId()==language) {
							podDescriptions.add(pod);
						}
					}
					po.setDescriptions(podDescriptions);
				}
				
				ProductOptionValue pov = attribute.getProductOptionValue();
				Set<ProductOptionValueDescription> spovd = pov.getDescriptions();
				if(spovd!=null) {
					Set<ProductOptionValueDescription> povdDescriptions = new HashSet();
					for(ProductOptionValueDescription povd : spovd) {
						if(povd.getLanguage().getId()==language) {
							povdDescriptions.add(povd);
						}
					}
					pov.setDescriptions(povdDescriptions);
				}
					
			}
		}
		
	}
	
	/**
	 * Overwrites the availability in order to return 1 price / region
	 * @param product
	 * @param locale
	 */
	public static void setToAvailability(Product product, Locale locale) {
		
		Set<ProductAvailability> availabilities =  product.getAvailabilities();
		
		ProductAvailability defaultAvailability = null;
		ProductAvailability localeAvailability = null;
		
		for(ProductAvailability availability : availabilities) {
			
			if(availability.getRegion().equals(ProductAvailability.DEFAULT_AVAILABILITY)) {
				defaultAvailability = availability;
			} 
			if(availability.getRegion().equals(locale.getCountry())) {
				localeAvailability = availability;
			}
			
		}
		
		if(defaultAvailability!=null || localeAvailability!=null) {
			Set<ProductAvailability> productAvailabilities = new HashSet<ProductAvailability>();
			if(defaultAvailability!=null) {
				productAvailabilities.add(defaultAvailability);
			}
			if(localeAvailability!=null) {
				productAvailabilities.add(localeAvailability);
			}
			product.setAvailabilities(productAvailabilities);
		}
		
	}

}
