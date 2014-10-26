package com.kopetto.sample.controller;

import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for home page, about page, etc.
 * 
 */
@Controller
public class HomeController {
	
    @ModelAttribute(value="pageId")
    public String  getPageId (HttpServletRequestWrapper request) {
    	String requestUri = request.getRequestURI();
    	String rc = null;
    	
    	if (requestUri.indexOf("account") > -1)
    		rc = "account";
    	else if (requestUri.indexOf("search") > -1)
    		rc = "search";
    	else if (requestUri.indexOf("signin") > -1)
    		rc = "signin";
    	else 
    		rc = "home";
    	
    	return rc;
    }
    
    @ModelAttribute(value="requestUri")
    public String  getRequetUrl (HttpServletRequestWrapper request) {
    	return request.getRequestURI();
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model uiModel) {
    	return "home";
    }
    
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String signin (Model uiModel) {
    	return "signin";
    }
    
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account (Model uiModel) {
    	return "account";
    }
}
