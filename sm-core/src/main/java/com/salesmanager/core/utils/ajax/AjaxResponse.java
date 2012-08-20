package com.salesmanager.core.utils.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class AjaxResponse implements JSONAware {
	
	public final static int RESPONSE_STATUS_SUCCESS=0;
	public final static int RESPONSE_STATUS_FAIURE=-1;
	public final static int RESPONSE_OPERATION_COMPLETED=9999;
	public final static int CODE_ALREADY_EXIST=9998;
	
	private int status;
	private List<Map<String,String>> data = new ArrayList<Map<String,String>>();
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Map<String,String>> getData() {
		return data;
	}
	
	public void addDataEntry(Map<String,String> dataEntry) {
		this.data.add(dataEntry);
	}
	
	public void setErrorMessage(Throwable t) {
		this.setStatusMessage(t.getMessage());
	}
	

	
	private String statusMessage = null;
	
	
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	
	protected String getJsonInfo() {
		
		StringBuilder returnString = new StringBuilder();
		returnString.append("{");
		returnString.append("\"response\"").append(":");
		returnString.append("{");
		returnString.append("\"status\"").append(":").append(this.getStatus());
		if(this.getStatusMessage()!=null && this.getStatus()!=0) {
			returnString.append(",").append("\"statusMessag\"").append(":\"").append(JSONObject.escape(this.getStatusMessage())).append("\"");
		}
		return returnString.toString();
		
	}
	
	@Override
	public String toJSONString() {
		// TODO Auto-generated method stub
		StringBuilder returnString = new StringBuilder();
		
		returnString.append(getJsonInfo());

		if(this.getData().size()>0) {
			StringBuilder dataEntries = null;
			int count = 0;
			for(Map keyValue : this.getData()) {
				if(dataEntries == null) {
					dataEntries = new StringBuilder();
				}
				JSONObject data = new JSONObject();
				Set<String> keys = keyValue.keySet();
				for(String key : keys) {
					data.put(key, keyValue.get(key));
				}
				String dataField = data.toJSONString();
				dataEntries.append(dataField);
				if(count<this.data.size()-1) {
					dataEntries.append(",");
				}
				count ++;
			}
			
			returnString.append(",").append("\"data\"").append(":[");
			if(dataEntries!=null) {
				returnString.append(dataEntries.toString());
			}
			returnString.append("]");
		}
		returnString.append("}}");

		
		return returnString.toString();

		
	}

}
