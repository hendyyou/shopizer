package com.salesmanager.web.entity.catalog;

import java.io.Serializable;

/**
 * A Product is used in the web application shopping section
 * This object is a simplification of com.salesmanager.core.business.catalog.product.model.Product
 * @author casams1
 *
 */
public class Product implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * this is the final price
	 */
	private String productPrice = "0";
	private String dateAvailable;
	private String sku;
	private boolean productVirtual;
	private String type;
	private String manufacturer;
	private String name;
	private String description;
	private String friendlyUrl;
	private String keywords;
	private String title;
	private String id;
	
	private boolean discounted = false;
	/**
	 * when discounted this is the original price
	 */
	private String originalProductPrice = null;
	
	private String image;
	
	private String imageUrl;


	
	public String getDateAvailable() {
		return dateAvailable;
	}
	public void setDateAvailable(String dateAvailable) {
		this.dateAvailable = dateAvailable;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public boolean isProductVirtual() {
		return productVirtual;
	}
	public void setProductVirtual(boolean productVirtual) {
		this.productVirtual = productVirtual;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFriendlyUrl() {
		return friendlyUrl;
	}
	public void setFriendlyUrl(String friendlyUrl) {
		this.friendlyUrl = friendlyUrl;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImage() {
		return image;
	}

	public void setOriginalProductPrice(String originalProductPrice) {
		this.originalProductPrice = originalProductPrice;
	}
	public String getOriginalProductPrice() {
		return originalProductPrice;
	}
	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}
	public boolean isDiscounted() {
		return discounted;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}





}
