package com.cnnic.whois.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.RedirectView;

import com.cnnic.whois.execption.RedirectExecption;
public class MappingExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {	
		if (ex instanceof RedirectExecption) {
			RedirectExecption rEx = (RedirectExecption)ex;
			response.setStatus(301);
			response.setHeader("Accept", BaseController.getFormatCookie(request));
			response.setHeader("Location", rEx.getRedirectURL());
			response.setHeader("Connection", "close");
			request.setAttribute("execption", ex);
			return new ModelAndView(new RedirectView(rEx.getRedirectURL()), null);
		}
		return null;
	}
}
