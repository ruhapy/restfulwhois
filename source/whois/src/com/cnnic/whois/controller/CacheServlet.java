package com.cnnic.whois.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnnic.whois.service.CacheUpdater;

public class CacheServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		CacheUpdater cacheUpdater = CacheUpdater.getCacheUpdater();
		cacheUpdater.init();
	}
}