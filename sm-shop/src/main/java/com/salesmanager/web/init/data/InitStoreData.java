package com.salesmanager.web.init.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.catalog.product.service.availability.ProductAvailabilityService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.common.model.Billing;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.common.model.Delivery;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.OrderTotal;
import com.salesmanager.core.business.order.model.orderproduct.OrderProduct;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductDownload;
import com.salesmanager.core.business.order.model.orderproduct.OrderProductPrice;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatus;
import com.salesmanager.core.business.order.model.orderstatus.OrderStatusHistory;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.web.constants.Constants;

@Component
public class InitStoreData implements InitData {
	
	
	
	@Autowired
	protected ProductService productService;

	
	@Autowired
	protected ProductPriceService productPriceService;
	
	@Autowired
	protected ProductAttributeService productAttributeService;
	
	@Autowired
	protected ProductOptionService productOptionService;
	
	@Autowired
	protected ProductOptionValueService productOptionValueService;
	
	@Autowired
	protected ProductAvailabilityService productAvailabilityService;
	
	@Autowired
	protected ProductImageService productImageService;
	
	@Autowired
	protected CategoryService categoryService;
	
	@Autowired
	protected MerchantStoreService merchantService;
	
	@Autowired
	protected ProductTypeService productTypeService;
	
	@Autowired
	protected LanguageService languageService;
	
	@Autowired
	protected CountryService countryService;
	
	@Autowired
	protected ZoneService zoneService;
	
	@Autowired
	protected CustomerService customerService;
	
	@Autowired
	protected ManufacturerService manufacturerService;

	@Autowired
	protected CurrencyService currencyService;
	
	@Autowired
	protected OrderService orderService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	protected GroupService   groupService;

	public void initInitialData() throws ServiceException {
		

		
		Date date = new Date(System.currentTimeMillis());
		
		Language en = languageService.getByCode("en");
		Language fr = languageService.getByCode("fr");
		
		Country canada = countryService.getByCode("CA");
		Zone zone = zoneService.getByCode("QC");
		
		//create a merchant
		MerchantStore store = merchantService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		ProductType generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);
		
		
		 Category book = new Category();
		    book.setMerchantStore(store);
		    book.setCode("book");
		    book.setVisible(true);

		    CategoryDescription bookEnglishDescription = new CategoryDescription();
		    bookEnglishDescription.setName("Book");
		    bookEnglishDescription.setCategory(book);
		    bookEnglishDescription.setLanguage(en);
		    bookEnglishDescription.setSeUrl("livre");

		    CategoryDescription bookFrenchDescription = new CategoryDescription();
		    bookFrenchDescription.setName("Livre");
		    bookFrenchDescription.setCategory(book);
		    bookFrenchDescription.setLanguage(fr);
		    bookFrenchDescription.setSeUrl("book");

		    List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
		    descriptions.add(bookEnglishDescription);
		    descriptions.add(bookFrenchDescription);

		    book.setDescriptions(descriptions);

		    categoryService.create(book);

		    Category music = new Category();
		    music.setMerchantStore(store);
		    music.setCode("music");
		    music.setVisible(true);

		    CategoryDescription musicEnglishDescription = new CategoryDescription();
		    musicEnglishDescription.setName("Music");
		    musicEnglishDescription.setCategory(music);
		    musicEnglishDescription.setLanguage(en);
		    musicEnglishDescription.setSeUrl("music");

		    CategoryDescription musicFrenchDescription = new CategoryDescription();
		    musicFrenchDescription.setName("Musique");
		    musicFrenchDescription.setCategory(music);
		    musicFrenchDescription.setLanguage(fr);
		    musicFrenchDescription.setSeUrl("musique");

		    List<CategoryDescription> descriptions2 = new ArrayList<CategoryDescription>();
		    descriptions2.add(musicEnglishDescription);
		    descriptions2.add(musicFrenchDescription);

		    music.setDescriptions(descriptions2);

		    categoryService.create(music);

		    Category novell = new Category();
		    novell.setMerchantStore(store);
		    novell.setCode("novell");
		    novell.setVisible(true);

		    CategoryDescription novellEnglishDescription = new CategoryDescription();
		    novellEnglishDescription.setName("Novell");
		    novellEnglishDescription.setCategory(novell);
		    novellEnglishDescription.setLanguage(en);
		    novellEnglishDescription.setSeUrl("novell");

		    CategoryDescription novellFrenchDescription = new CategoryDescription();
		    novellFrenchDescription.setName("Roman");
		    novellFrenchDescription.setCategory(novell);
		    novellFrenchDescription.setLanguage(fr);
		    novellFrenchDescription.setSeUrl("roman");

		    List<CategoryDescription> descriptions3 = new ArrayList<CategoryDescription>();
		    descriptions3.add(novellEnglishDescription);
		    descriptions3.add(novellFrenchDescription);

		    novell.setDescriptions(descriptions3);
		    
		    novell.setParent(book);

		    categoryService.create(novell);
		    categoryService.addChild(book, novell);

		    Category tech = new Category();
		    tech.setMerchantStore(store);
		    tech.setCode("tech");

		    CategoryDescription techEnglishDescription = new CategoryDescription();
		    techEnglishDescription.setName("Technology");
		    techEnglishDescription.setCategory(tech);
		    techEnglishDescription.setLanguage(en);
		    techEnglishDescription.setSeUrl("techno");

		    CategoryDescription techFrenchDescription = new CategoryDescription();
		    techFrenchDescription.setName("Technologie");
		    techFrenchDescription.setCategory(tech);
		    techFrenchDescription.setLanguage(fr);
		    techFrenchDescription.setSeUrl("techno");

		    List<CategoryDescription> descriptions4 = new ArrayList<CategoryDescription>();
		    descriptions4.add(techEnglishDescription);
		    descriptions4.add(techFrenchDescription);

		    tech.setDescriptions(descriptions4);
		    
		    tech.setParent(book);

		    categoryService.create(tech);
		    categoryService.addChild(book, tech);

		    Category fiction = new Category();
		    fiction.setMerchantStore(store);
		    fiction.setCode("fiction");
		    fiction.setVisible(true);

		    CategoryDescription fictionEnglishDescription = new CategoryDescription();
		    fictionEnglishDescription.setName("Fiction");
		    fictionEnglishDescription.setCategory(fiction);
		    fictionEnglishDescription.setLanguage(en);
		    fictionEnglishDescription.setSeUrl("fiction");

		    CategoryDescription fictionFrenchDescription = new CategoryDescription();
		    fictionFrenchDescription.setName("Sc Fiction");
		    fictionFrenchDescription.setCategory(fiction);
		    fictionFrenchDescription.setLanguage(fr);
		    fictionFrenchDescription.setSeUrl("fiction");

		    List<CategoryDescription> fictiondescriptions = new ArrayList<CategoryDescription>();
		    fictiondescriptions.add(fictionEnglishDescription);
		    fictiondescriptions.add(fictionFrenchDescription);

		    fiction.setDescriptions(fictiondescriptions);
		    
		    fiction.setParent(novell);

		    categoryService.create(fiction);
		    categoryService.addChild(book, fiction);

		    // Add products
		    // ProductType generalType = productTypeService.

		    Manufacturer oreilley = new Manufacturer();
		    oreilley.setMerchantStore(store);

		    ManufacturerDescription oreilleyd = new ManufacturerDescription();
		    oreilleyd.setLanguage(en);
		    oreilleyd.setName("O\'reilley");
		    oreilleyd.setManufacturer(oreilley);
		    oreilley.getDescriptions().add(oreilleyd);

		    manufacturerService.create(oreilley);

		    Manufacturer packed = new Manufacturer();
		    packed.setMerchantStore(store);

		    ManufacturerDescription packedd = new ManufacturerDescription();
		    packedd.setLanguage(en);
		    packedd.setManufacturer(packed);
		    packedd.setName("Packed publishing");
		    packed.getDescriptions().add(packedd);

		    manufacturerService.create(packed);

		    Manufacturer novells = new Manufacturer();
		    novells.setMerchantStore(store);

		    ManufacturerDescription novellsd = new ManufacturerDescription();
		    novellsd.setLanguage(en);
		    novellsd.setManufacturer(novells);
		    novellsd.setName("Novells publishing");
		    novells.getDescriptions().add(novellsd);

		    manufacturerService.create(novells);

		    // PRODUCT 1

		    Product product = new Product();
		    product.setProductHeight(new BigDecimal(4));
		    product.setProductLength(new BigDecimal(3));
		    product.setProductWidth(new BigDecimal(1));
		    product.setSku("TB12345");
		    product.setManufacturer(oreilley);
		    product.setType(generalType);
		    product.setMerchantStore(store);

		    // Product description
		    ProductDescription description = new ProductDescription();
		    description.setName("Spring in Action");
		    description.setLanguage(en);
		    description.setProduct(product);

		    product.getDescriptions().add(description);

		    product.getCategories().add(tech);
		    product.getCategories().add(novell);
		    product.getCategories().add(fiction);

		    productService.create(product);

		    // Availability
		    ProductAvailability availability = new ProductAvailability();
		    availability.setProductDateAvailable(date);
		    availability.setProductQuantity(100);
		    availability.setRegion("*");
		    availability.setProduct(product);// associate with product

		    productAvailabilityService.create(availability);

		    ProductPrice dprice = new ProductPrice();
		    dprice.setDefaultPrice(true);
		    dprice.setProductPriceAmount(new BigDecimal(29.99));
		    dprice.setProductAvailability(availability);

		    ProductPriceDescription dpd = new ProductPriceDescription();
		    dpd.setName("Base price");
		    dpd.setProductPrice(dprice);
		    dpd.setLanguage(en);

		    dprice.getDescriptions().add(dpd);

		    productPriceService.create(dprice);

		    // PRODUCT 2

		    Product product2 = new Product();
		    product2.setProductHeight(new BigDecimal(4));
		    product2.setProductLength(new BigDecimal(3));
		    product2.setProductWidth(new BigDecimal(1));
		    product2.setSku("TB2468");
		    product2.setManufacturer(packed);
		    product2.setType(generalType);
		    product2.setMerchantStore(store);

		    // Product description
		    description = new ProductDescription();
		    description.setName("This is Node.js");
		    description.setLanguage(en);
		    description.setProduct(product2);

		    product2.getDescriptions().add(description);

		    product2.getCategories().add(tech);
		    productService.create(product2);

		    // Availability
		    ProductAvailability availability2 = new ProductAvailability();
		    availability2.setProductDateAvailable(date);
		    availability2.setProductQuantity(100);
		    availability2.setRegion("*");
		    availability2.setProduct(product2);// associate with product

		    productAvailabilityService.create(availability2);

		    ProductPrice dprice2 = new ProductPrice();
		    dprice2.setDefaultPrice(true);
		    dprice2.setProductPriceAmount(new BigDecimal(39.99));
		    dprice2.setProductAvailability(availability2);

		    dpd = new ProductPriceDescription();
		    dpd.setName("Base price");
		    dpd.setProductPrice(dprice2);
		    dpd.setLanguage(en);

		    dprice2.getDescriptions().add(dpd);

		    productPriceService.create(dprice2);

		    // PRODUCT 3

		    Product product3 = new Product();
		    product3.setProductHeight(new BigDecimal(4));
		    product3.setProductLength(new BigDecimal(3));
		    product3.setProductWidth(new BigDecimal(1));
		    product3.setSku("NB1111");
		    product3.setManufacturer(packed);
		    product3.setType(generalType);
		    product3.setMerchantStore(store);

		    // Product description
		    description = new ProductDescription();
		    description.setName("A nice book for you");
		    description.setLanguage(en);
		    description.setProduct(product3);

		    product3.getDescriptions().add(description);

		    product3.getCategories().add(novell);
		    productService.create(product3);

		    // Availability
		    ProductAvailability availability3 = new ProductAvailability();
		    availability3.setProductDateAvailable(date);
		    availability3.setProductQuantity(100);
		    availability3.setRegion("*");
		    availability3.setProduct(product3);// associate with product

		    productAvailabilityService.create(availability3);

		    ProductPrice dprice3 = new ProductPrice();
		    dprice3.setDefaultPrice(true);
		    dprice3.setProductPriceAmount(new BigDecimal(19.99));
		    dprice3.setProductAvailability(availability3);

		    dpd = new ProductPriceDescription();
		    dpd.setName("Base price");
		    dpd.setProductPrice(dprice3);
		    dpd.setLanguage(en);

		    dprice3.getDescriptions().add(dpd);

		    productPriceService.create(dprice3);

		    // PRODUCT 4

		    Product product4 = new Product();
		    product4.setProductHeight(new BigDecimal(4));
		    product4.setProductLength(new BigDecimal(3));
		    product4.setProductWidth(new BigDecimal(1));
		    product4.setSku("SF333345");
		    product4.setManufacturer(packed);
		    product4.setType(generalType);
		    product4.setMerchantStore(store);

		    // Product description
		    description = new ProductDescription();
		    description.setName("Battle of the worlds");
		    description.setLanguage(en);
		    description.setProduct(product4);

		    product4.getDescriptions().add(description);

		    product4.getCategories().add(fiction);
		    productService.create(product4);

		    // Availability
		    ProductAvailability availability4 = new ProductAvailability();
		    availability4.setProductDateAvailable(date);
		    availability4.setProductQuantity(100);
		    availability4.setRegion("*");
		    availability4.setProduct(product4);// associate with product

		    productAvailabilityService.create(availability4);

		    ProductPrice dprice4 = new ProductPrice();
		    dprice4.setDefaultPrice(true);
		    dprice4.setProductPriceAmount(new BigDecimal(18.99));
		    dprice4.setProductAvailability(availability4);

		    dpd = new ProductPriceDescription();
		    dpd.setName("Base price");
		    dpd.setProductPrice(dprice4);
		    dpd.setLanguage(en);

		    dprice4.getDescriptions().add(dpd);

		    productPriceService.create(dprice4);

		    // PRODUCT 5

		    Product product5 = new Product();
		    product5.setProductHeight(new BigDecimal(4));
		    product5.setProductLength(new BigDecimal(3));
		    product5.setProductWidth(new BigDecimal(1));
		    product5.setSku("SF333346");
		    product5.setManufacturer(packed);
		    product5.setType(generalType);
		    product5.setMerchantStore(store);

		    // Product description
		    description = new ProductDescription();
		    description.setName("Battle of the worlds 2");
		    description.setLanguage(en);
		    description.setProduct(product5);

		    product5.getDescriptions().add(description);

		    product5.getCategories().add(fiction);
		    productService.create(product5);

		    // Availability
		    ProductAvailability availability5 = new ProductAvailability();
		    availability5.setProductDateAvailable(date);
		    availability5.setProductQuantity(100);
		    availability5.setRegion("*");
		    availability5.setProduct(product5);// associate with product

		    productAvailabilityService.create(availability5);

		    ProductPrice dprice5 = new ProductPrice();
		    dprice5.setDefaultPrice(true);
		    dprice5.setProductPriceAmount(new BigDecimal(18.99));
		    dprice5.setProductAvailability(availability5);

		    dpd = new ProductPriceDescription();
		    dpd.setName("Base price");
		    dpd.setProductPrice(dprice5);
		    dpd.setLanguage(en);

		    dprice5.getDescriptions().add(dpd);

		    productPriceService.create(dprice5);

		    // PRODUCT 6

		    Product product6 = new Product();
		    product6.setProductHeight(new BigDecimal(4));
		    product6.setProductLength(new BigDecimal(3));
		    product6.setProductWidth(new BigDecimal(1));
		    product6.setSku("LL333444");
		    product6.setManufacturer(packed);
		    product6.setType(generalType);
		    product6.setMerchantStore(store);

		    // Product description
		    description = new ProductDescription();
		    description.setName("Life book");
		    description.setLanguage(en);
		    description.setProduct(product6);

		    product6.getDescriptions().add(description);

		    product6.getCategories().add(novell);
		    productService.create(product6);

		    // Availability
		    ProductAvailability availability6 = new ProductAvailability();
		    availability6.setProductDateAvailable(date);
		    availability6.setProductQuantity(100);
		    availability6.setRegion("*");
		    availability6.setProduct(product6);// associate with product

		    productAvailabilityService.create(availability6);

		    ProductPrice dprice6 = new ProductPrice();
		    dprice6.setDefaultPrice(true);
		    dprice6.setProductPriceAmount(new BigDecimal(18.99));
		    dprice6.setProductAvailability(availability6);

		    dpd = new ProductPriceDescription();
		    dpd.setName("Base price");
		    dpd.setProductPrice(dprice6);
		    dpd.setLanguage(en);

		    dprice6.getDescriptions().add(dpd);

		    productPriceService.create(dprice6);

		    
		    //Create a customer (user name[nick] : shopizer password : password)

		    Customer customer = new Customer();
			customer.setFirstname("Leonardo");
			customer.setMerchantStore(store);
			customer.setLastname("DiCaprio");
			customer.setCity("Boucherville");
			customer.setEmailAddress("test@shopizer.com");
			customer.setGender("M");						
			customer.setTelephone("444-555-6666");
			customer.setAnonymous(false);
			customer.setCompany("CSTI Consulting");
			customer.setDateOfBirth(new Date());
			customer.setPostalCode("J4B-8J9");			
			customer.setStreetAddress("358 Du Languadoc");
			customer.setTelephone("444-555-6666");
			customer.setCountry(canada);
			customer.setZone(zone);
			customer.setDefaultLanguage(en);
			customer.setNick("shopizer");
			
			String password = passwordEncoder.encodePassword("password", null);
			customer.setPassword(password);
			
			List<Group> groups = groupService.listGroup(GroupType.CUSTOMER);
			  

			for(Group group : groups) {
				  if(group.getGroupName().equals(Constants.GROUP_CUSTOMER)) {
					  customer.getGroups().add(group);
				  }
			}
			
		    Delivery delivery = new Delivery();
		    delivery.setAddress("358 Du Languadoc");
		    delivery.setCity( "Boucherville DiCaprio" );
		    delivery.setCountry(canada);
//		    delivery.setCountryCode(canada.getIsoCode());
		    delivery.setName("Leonardo" );
		    delivery.setPostalCode("J4B-8J9" );
		    delivery.setZone(zone);	    
		    
		    Billing billing = new Billing();
		    billing.setAddress("358 Du Languadoc");
		    billing.setCity("Boucherville");
		    billing.setCompany("CSTI Consulting");
		    billing.setCountry(canada);
//		    billing.setCountryCode(canada.getIsoCode());
		    billing.setName("CSTI Consulting");
		    billing.setPostalCode("J4B-8J9");
		    billing.setZone(zone);
		    
		    customer.setBilling(billing);
		    customer.setDelivery(delivery);		
			customerService.create(customer);
			
			Currency currency = currencyService.getByCode("CAD");

			OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
			
			//create an order
			
			Order order = new Order();
			order.setDatePurchased(new Date());
			order.setCurrency(currency);
			order.setLastModified(new Date());
			order.setBilling(billing);


			order.setChannel(1);//1 is online
			order.setCurrencyValue(new BigDecimal(0.98));//compared to based currency (not necessary)
			order.setCustomerId(customer.getId());
			order.setCustomerFirstName("Leo");
			order.setCustomerLastName("DiCaprio");
			order.setCustomerEmailAddress("leo@shopizer.com");
			order.setDelivery(delivery);
			order.setDisplayInvoicePayments(true);
			order.setIpAddress("ipAddress" );
			order.setMerchant(store);
			order.setOrderDateFinished(new Date());//committed date
			
			orderStatusHistory.setComments("We received your order");
			orderStatusHistory.setCustomerNotified(1);
			orderStatusHistory.setStatus(OrderStatus.ORDERED);
			orderStatusHistory.setDateAdded(new Date() );
			orderStatusHistory.setOrder(order);
			order.getOrderHistory().add( orderStatusHistory );		
			
			order.setOrderTax(new BigDecimal(4.00));
			order.setPaymentMethod("Paypal");
			order.setPaymentModuleCode("paypal");
			order.setStatus( OrderStatus.DELIVERED);
			order.setTotal(new BigDecimal(23.99));
			
			
			//OrderProductDownload - Digital download
			OrderProductDownload orderProductDownload = new OrderProductDownload();
			orderProductDownload.setDownloadCount(1);
			orderProductDownload.setMaxdays(31);		
			orderProductDownload.setOrderProductFilename("Your digital file name");
			
			//OrderProductPrice
			OrderProductPrice oproductprice = new OrderProductPrice();
			oproductprice.setDefaultPrice(true);	
			oproductprice.setProductPriceAmount(new BigDecimal(19.99) );
			oproductprice.setProductPriceCode("baseprice" );
			oproductprice.setProductPriceName("Base Price" );
			//oproductprice.setProductPriceSpecialAmount(new BigDecimal(13.99) );	

			
			//OrderProduct
			OrderProduct oproduct = new OrderProduct();
			oproduct.getDownloads().add( orderProductDownload);
			oproduct.setFinalPrice(new BigDecimal(19.99) );
			oproduct.setOnetimeCharge( new BigDecimal(19.99) );
			oproduct.setOrder(order);		
			oproduct.setProductName( "Product name" );
			oproduct.setProductQuantity(1);
			oproduct.setSku("TB12345" );		
			oproduct.getPrices().add(oproductprice ) ;
			
			oproductprice.setOrderProduct(oproduct);		
			orderProductDownload.setOrderProduct(oproduct);
			order.getOrderProducts().add(oproduct);

			//OrderTotal
			OrderTotal subtotal = new OrderTotal();	
			subtotal.setModule("summary" );		
			subtotal.setSortOrder(0);
			subtotal.setText("Summary" );
			subtotal.setTitle("Summary" );
			subtotal.setValue(new BigDecimal(19.99 ) );
			subtotal.setOrder(order);
			
			order.getOrderTotal().add(subtotal);
			
			OrderTotal tax = new OrderTotal();	
			tax.setModule("tax" );		
			tax.setSortOrder(1);
			tax.setText("Tax" );
			tax.setTitle("Tax" );
			tax.setValue(new BigDecimal(4) );
			tax.setOrder(order);
			
			order.getOrderTotal().add(tax);
			
			OrderTotal total = new OrderTotal();	
			total.setModule("total" );		
			total.setSortOrder(2);
			total.setText("Total" );
			total.setTitle("Total" );
			total.setValue(new BigDecimal(23.99) );
			total.setOrder(order);
			
			order.getOrderTotal().add(total);
			
			orderService.create(order);		
		
	}


	

}
