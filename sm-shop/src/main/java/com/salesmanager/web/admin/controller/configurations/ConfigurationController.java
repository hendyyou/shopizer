package com.salesmanager.web.admin.controller.configurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.model.MerchantConfigurationType;
import com.salesmanager.core.business.system.service.MerchantConfigurationService;
import com.salesmanager.web.admin.controller.ControllerConstants;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;


@Controller
public class ConfigurationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;

	

	@Secured("AUTH")
	@RequestMapping(value="/admin/configuration/accounts.html", method=RequestMethod.GET)
	public String displayAccountsConfguration(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		List<MerchantConfiguration> configs = new ArrayList<MerchantConfiguration>();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		MerchantConfiguration merchantFBConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_FACEBOOK_PAGE_URL,store);
		if(null == merchantFBConfiguration)
		{
			merchantFBConfiguration = new MerchantConfiguration();
			merchantFBConfiguration.setKey(Constants.KEY_FACEBOOK_PAGE_URL);
			merchantFBConfiguration.setMerchantConfigurationType(MerchantConfigurationType.CONFIG);
		}
		configs.add(merchantFBConfiguration);
		
		MerchantConfiguration merchantGoogleAnalyticsConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_GOOGLE_ANALYTICS_URL,store);
		if(null == merchantGoogleAnalyticsConfiguration)
		{
			merchantGoogleAnalyticsConfiguration = new MerchantConfiguration();
			merchantGoogleAnalyticsConfiguration.setKey(Constants.KEY_GOOGLE_ANALYTICS_URL);
			merchantFBConfiguration.setMerchantConfigurationType(MerchantConfigurationType.CONFIG);
		}
		configs.add(merchantGoogleAnalyticsConfiguration);
		
		MerchantConfiguration twitterConfiguration = merchantConfigurationService.getMerchantConfiguration(Constants.KEY_TWITTER_HANDLE,store);
		if(null == twitterConfiguration)
		{
			twitterConfiguration = new MerchantConfiguration();
			twitterConfiguration.setKey(Constants.KEY_TWITTER_HANDLE);
			merchantFBConfiguration.setMerchantConfigurationType(MerchantConfigurationType.CONFIG);
		}
		configs.add(twitterConfiguration);
		
		ConfigListWrapper configWrapper = new ConfigListWrapper();
		configWrapper.setMerchantConfigs(configs);
		model.addAttribute("configuration",configWrapper);
		
		return com.salesmanager.web.admin.controller.ControllerConstants.Tiles.Configuration.accounts;
	}
	
	@Secured("AUTH")
	@RequestMapping(value="/admin/configuration/saveConfiguration.html", method=RequestMethod.POST)
	public String saveConfigurations(@ModelAttribute("configuration") ConfigListWrapper configWrapper, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception
	{
		setMenu(model, request);
		List<MerchantConfiguration> configs = configWrapper.getMerchantConfigs();
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		for(MerchantConfiguration mConfigs : configs)
		{
			mConfigs.setMerchantStore(store);
			if(!StringUtils.isBlank(mConfigs.getValue())) {
				mConfigs.setMerchantConfigurationType(MerchantConfigurationType.CONFIG);
				merchantConfigurationService.saveOrUpdate(mConfigs);
			} else {//remove if submited blank and exists
				MerchantConfiguration config = merchantConfigurationService.getMerchantConfiguration(mConfigs.getKey(), store);
				if(config!=null) {
					merchantConfigurationService.delete(config);
				}
			}
		}	
		model.addAttribute("success","success");
		model.addAttribute("configuration",configWrapper);
		return com.salesmanager.web.admin.controller.ControllerConstants.Tiles.Configuration.accounts;
		
	}
	
	@Secured("AUTH")
	@RequestMapping(value="/admin/configuration/emailConfig.html", method=RequestMethod.GET)
	public String displayEmailSettings(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		
		//must be able to store smtp server settings
		/*
		protocol=smtp
		host=smtpout.secureserver.net
		port=3535
		username=support@shopizer.com
		password=buzz

		auth=true
		starttls.enable=false
		 */
		//see sm-core / system.model.email.EmailConfig
		
		//save in MerchantConfiguration using key EMAIL_CONFIG (see similar shippingService.saveShippingConfiguration and shippingService.getShippingConfiguration)
		
		//may use the default configuration from spring if nothing is configured see shopizer-core-modules.xml)
		
		
		
		//Get from MerchantConfiguration KEY - JSON VALUE
		
		return ControllerConstants.Tiles.Configuration.email;
		
	}
	

	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("accounts-conf", "accounts-conf");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("configuration");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
}
