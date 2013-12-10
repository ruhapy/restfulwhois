package com.cnnic.whois.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoader;

import com.cnnic.whois.util.WhoisProperties;
import com.cnnic.whois.util.WhoisUtil;
import com.cnnic.whois.view.FormatType;
import com.cnnic.whois.view.ViewResolver;

public class ErrorFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		// TODO Need Test
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		
		String userAgent = "";
		try{
			userAgent = request.getHeader("user-agent").toLowerCase();
		}
		catch(Exception e){
			userAgent = "";
		}		

		String format = WhoisUtil.getFormatCookie(request);		
		String role = WhoisUtil.getUserRole(request);

		CharSequence ie = "msie";
		CharSequence firefox = "firefox";
		CharSequence chrome = "chrome";
		CharSequence safiri = "safiri";
		CharSequence opera = "opera";   
		if (format == null && (userAgent.contains(ie) || userAgent.contains(firefox) ||
				userAgent.contains(chrome) || userAgent.contains(safiri) || userAgent.contains(opera)))
			format = FormatType.HTML.getName();
		if (format == null){
			format = request.getHeader("Accept");	
			if (format == null){
				format = FormatType.JSON.getName();
			}

			CharSequence sqhtml = "html";			
			if(format.contains(sqhtml))
				format = FormatType.HTML.getName();
		}
		if(format == null || !((FormatType.getFormatType(format)).isNotNoneFormat())){
			format = FormatType.HTML.getName();
		}
		
		String queryInfo = "";
		String queryType = "";
		
		String path = request.getRequestURI();
		
		if(!path.equals("")){
			queryInfo = path.substring(request.getContextPath().length() + 1);
			
			if(queryInfo.equals("") && (userAgent.contains(ie) || userAgent.contains(firefox) ||
					userAgent.contains(chrome) || userAgent.contains(safiri) || userAgent.contains(opera))){
				format = FormatType.HTML.getName();
				WhoisUtil.clearFormatCookie(request, response);
			}
			
			// disregard .well-known/rdap
			queryInfo = queryInfo.toLowerCase();
			if (queryInfo.startsWith(WhoisProperties.getRdapUrl()+"/")) {
				System.err.println(queryInfo);
				queryInfo = queryInfo.substring(WhoisProperties.getRdapUrl().length()+1);
			}
			
			if(queryInfo.indexOf("/") != -1){				
				queryType = queryInfo.substring(0, queryInfo.indexOf("/"));
			}else{
				queryType = queryInfo;//domains?name=a*.cn ->domains
			}
		}
		
		displayErrorMessage(request, response, chain, format, queryType, role);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
	
	private void displayErrorMessage(HttpServletRequest request, HttpServletResponse response, FilterChain chain, 
			String format, String queryType, String role) throws IOException, ServletException{
		ViewResolver viewResolver = (ViewResolver) ContextLoader.getCurrentWebApplicationContext().getBean("viewResolver");
		FormatType formatType = FormatType.getFormatType(format);
		viewResolver.displayErrorMessage(request, response, chain, formatType, queryType, role); 
	}
}
