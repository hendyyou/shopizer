package com.salesmanager.web.entity.catalog;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.ShopEntity;

public class Attribute extends ShopEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = null;
	private String type = null;
	private String code = null;
	private List<AttributeValue> values = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	


	public void setValues(List<AttributeValue> values) {
		this.values = values;
	}
	public List<AttributeValue> getValues() {
		return values;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}



	

}
