package com.cnnic.whois.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.view.FormatType;

public class BaseController {
	protected QueryParam praseQueryParams(HttpServletRequest request) {
		String format = getFormatCookie(request);
		FormatType formatType = FormatType.getFormatType(format);
		PageBean page = getPageParam(request);
		return new QueryParam(formatType, page);
	}

	private PageBean getPageParam(HttpServletRequest request) {
		Object currentPageObj = request.getParameter("currentPage");
		PageBean page = new PageBean();
		if (null != currentPageObj) {
			page.setCurrentPage(Integer.valueOf(currentPageObj.toString()));
		}
		return page;
	}

	private String getFormatCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String format = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("Format")) {
					return cookie.getValue();
				}
			}
		}
		if (format == null)
			format = "application/json";
		return format;
	}
}
