<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>				
				

<link href="<c:url value="/resources/css/bootstrap/css/datepicker.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/functions.js" />"></script>



<div class="tabbable">

		<jsp:include page="/common/adminTabs.jsp" />
					
		<div class="tab-content">

  		<div class="tab-pane active" id="order-section">

		<div class="sm-ui-component">	


		<h3>
			<div class="control-group">
                      <div class="controls">
                     		 <s:message code="label.order.id2" text="Order ID"/> 
                     		 <c:out value="${order.order.id}" /><br>
                       </div>       
                  </div>
           </h3>
		<br/>
			<br/>
 	       	 	
	     <c:url var="orderSave" value="/admin/orders/save.html"/>
         <form:form method="POST" enctype="multipart/form-data" commandName="order" action="${orderSave}">
	   
                <form:errors path="*" cssClass="alert alert-error" element="div" />
                <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
                <div id="store.error" class="alert alert-error" style="display:none;"><s:message code="message.error" text="An error occured"/></div>
	
	 	<form:hidden path="order.id" />
 			
 		<div class="span4">  
			
			<h6> <s:message code="label.customer.address2" text="Customer Address"/> </h6>
			<address>			        

	            <div class="controls">
		 				<form:input id="customer_Firstname" cssClass="input-large highlight" path="order.customer_Firstname"/>
		 				<span class="help-inline">
		 				<form:errors path="order.customer_Firstname" cssClass="error" /></span>
	            </div>
	                <div class="controls">
		 				<form:input id="customer_Lastname" cssClass="input-large highlight" path="order.customer_Lastname"/>
		 				<span class="help-inline"><form:errors path="order.customer_Lastname" cssClass="error" /></span>
	            </div>
	           <div class="controls">
		 				<form:input id="customer_Street_Address" cssClass="input-large highlight" path="order.customer_Street_Address"/>
		 				<span class="help-inline"><form:errors path="order.customer_Street_Address" cssClass="error" /></span>
	            </div>
	            <div class="controls">
		 				<form:input id="customer_City" cssClass="input-large highlight" path="order.customer_City"/>
		 				<span class="help-inline"><form:errors path="order.customer_City" cssClass="error" /></span>
	            </div> 
	 
			 	<form:input  cssClass="input-large" path="order.customer_State"/><br> 
				
				<div class="controls">
		 				<form:input id="customer_PostCode" cssClass="input-large highlight" path="order.customer_PostCode"/>
		 				<span class="help-inline"><form:errors path="order.customer_PostCode" cssClass="error" /></span>
	            </div> 
	           
	            <div class="controls">
		 				P:<form:input id="customer_Telephone" cssClass="input-large highlight" path="order.customer_Telephone"/>
		 				<span class="help-inline"><form:errors path="order.customer_Telephone" cssClass="error" /></span>
	            </div>	
	            
   
				<dl  class="dl-horizontal">			 		
			 		<div class="controls">
		 	 	 		<dt>Email</dt>
		 				<dd><form:input id="customer_Email_Address" cssClass="input-large highlight" path="order.customer_Email_Address"/></dd>
		 				<span class="help-inline"><form:errors path="order.customer_Email_Address" cssClass="error" /></span>
	            	</div>  
	
		  	 		<dt>Date purchased</dt>
		 			<dd><form:input  cssClass="input-large" path="datePurchased"  class="small" type="text"
							 data-date-format="<%=com.salesmanager.core.constants.Constants.DEFAULT_DATE_FORMAT%>" />
							  <script type="text/javascript">
                                 $('#datePurchased').datepicker();
                              </script>
					</dd>
							
					<dt>Payment method</dt>
		 			<dd><form:input  cssClass="input-large" path="order.paymentMethod"/></dd>
						
					<dt>Shipping method</dt>
					<dd><form:input  cssClass="input-large" path="order.shippingMethod"/></dd>			
				</dl> 
			</address>
			</div>
			
		 	<div class="offset5">			
				<h6>Shipping address</h6>
				<address>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.name"/>				 							
			            </div>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.address"/>		 				
			            </div>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.city"/>
			            </div>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.state"/>
			            </div>
			            <div class="controls">
				 				<form:input  cssClass="input-large" path="order.delivery.postalCode"/>
			            </div>	            	            	            	            				
				</address>	
			
				<br/>
	
			    <h6>Billing address</h6>
			    <address>
			    		<div class="control-group">
			    
					     <div class="controls">
				 				<form:input id="billing_name" cssClass="input-large highlight" path="order.billing.name"/>
				 				<span class="help-inline"><form:errors path="order.billing.name" cssClass="error" /></span>
			            </div>
			            </div>
			            <div class="controls">
				 				<form:input id="billing_adress" cssClass="input-large highlight" path="order.billing.address"/>
				 				<span class="help-inline"><form:errors path="order.billing.address" cssClass="error" /></span>
			            </div>
			            <div class="controls">
				 				<form:input id="billing_city" cssClass="input-large highlight" path="order.billing.city"/>
				 				<span class="help-inline"><form:errors path="order.billing.city" cssClass="error" /></span>
			            </div>
			            <div class="controls">
				 				<form:input id="billing_state" cssClass="input-large highlight" path="order.billing.state"/>
				 				<span class="help-inline"><form:errors path="order.billing.state" cssClass="error" /></span>
			            </div>
			            <div class="controls">
				 				<form:input id="billing_postalCode" cssClass="input-large highlight" path="order.billing.postalCode"/>
				 				<span class="help-inline"><form:errors path="order.billing.postalCode" cssClass="error" /></span>
			            </div>	
			    </address>			 
		  </div>
		
 		  <div class="span8">
			<div class="form-actions">
              <button class="btn btn-medium btn-primary" type="button">Edit</button>
              <button class="btn btn-medium btn-danger" type="button">Apply refund</button>
      		</div>
      	  </div> 
      
      	  <br/>
      
      	  <div class="span8">
		      <table class="table table-bordered table-striped"> 
					<thead> 
						<tr> 
							<th colspan="2" width="55%">Item</th> 
							<th colspan="1" width="15%">Quantity</th> 
							<th width="15%">Price</th>
							<th width="15%">Total</th>  
						</tr> 
					</thead> 
					
 				    <tbody> 
						<c:forEach items="${order.order.orderProducts}" var="orderProducts" varStatus="counter">	 
			            	<c:set var="total" value="${orderProducts.finalPrice * orderProducts.productQuantity }" />
			            	
							<tr> 
								<td width="10%">image</td>
								<td> <c:out value="${orderProducts.productName}" /></td> 
								<td ><c:out value="${orderProducts.productQuantity}" /></td> 
								<%-- <td><strong>$<fmt:formatNumber type="number" 
			            						value="${orderProducts.finalPrice}" />
			            			</strong>       		</td>  --%>
			            		
			            		<td><strong>$<fmt:formatNumber type="number" maxFractionDigits="2" value="${orderProducts.finalPrice}" />  </strong> </td>
								<td><strong>$<fmt:formatNumber type="number" maxFractionDigits="2" value="${total}" />  </strong> </td> 
							</tr> 
			
						</c:forEach> 
					
					 	<c:forEach items="${order.order.orderTotal}" var="orderTotal" varStatus="counter">	
							<tr class="subt"> 
								<td colspan="2">&nbsp;</td> 
								<td colspan="2" ><c:out value="${orderTotal.title}"  /></td> 
								<td ><strong>$<fmt:formatNumber type="number"  minFractionDigits="2"
			            						  value="${orderTotal.value}" /></strong></td> 
							</tr> 
						</c:forEach> 	 
					</tbody>    
				</table>
    	  </div>  

            <br/>   
            <div class="span8">
		           <div class="control-group">
		                  <label>Status</label>	 
		                  <div class="controls">      
	                   			<form:select path="order.status">
				  						<form:options items="${orderStatusList}" />
			       				</form:select>      
		                   </div>
		           </div>  
		     					
           	       <div class="control-group">
                       <label>History</label>
                       <div class="controls">
							 <dl class="dl-horizontal">
								<c:forEach items="${order.order.orderHistory}" var="orderHistory" varStatus="counter">
									<dd>- <c:out value="${orderHistory.comments}"/>                              
	              				</c:forEach> 
							</dl> 
					   </div>
              	   </div> 
              
	     		   <div class="control-group">  
	                    <label>Status</label>
	                     <div class="controls">
	                         <form:textarea  cols="10" rows="3" path="orderHistoryComment"/>
	                    </div> 
	               </div>
              
	              <div class="form-actions">
	              		<button  type="submit" class="btn btn-medium btn-primary" ><s:message code="button.label.save" text="Save"/></button>
	      		  </div>
      		</div> 
            <br/>              
              
             <ul class="nav nav-pills">
							<li><a href="#">Send email invoice</a></li>
							<li class="disabled"><a href="#">Print packing slip</a></li>
		    </ul> 
    
    
    	  </div>
   
   		</form:form>       

      </div>
	 </div>
  </div>      			     