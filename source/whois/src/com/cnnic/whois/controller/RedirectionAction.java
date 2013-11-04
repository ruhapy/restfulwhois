package com.cnnic.whois.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.cnnic.whois.service.RedirectionService;
import com.cnnic.whois.util.WhoisUtil;
import com.opensymphony.xwork2.ActionSupport;

public class RedirectionAction extends ActionSupport {

	private String tableType;

	public RedirectionAction() {
	}

	/**
	 * To assignment of the tableType property
	 * 
	 * @param tableType
	 */
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	/**
	 * A default implementation that does nothing an returns "success".
	 * Subclasses should override this method to provide their business logic.
	 * 
	 * @return Action.SUCCESS
	 */
	@Override
	public String execute() {
		try {
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * Add TableName value list redirection table information in the request
	 * scope
	 * 
	 * @return Action.SUCCESS
	 */
	public String list() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("tableName", request.getParameter("tableName"));
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * Add redirection information
	 * 
	 * @return Action.SUCCESS
	 */
	public String add() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			RedirectionService redirectService = RedirectionService
					.getRedirectService();
			String start = request.getParameter("start");
			String redirectUrl = request.getParameter("redirectURl");
			String tableName = request.getParameter("tableName");
			String end = null;
			if (tableName.equals(WhoisUtil.IP)
					|| tableName.equals(WhoisUtil.AUTNUM)) {
				end = request.getParameter("end");
			}

			redirectService.addRedirect(start, end, redirectUrl, tableName);
			request.setAttribute("tableName", request.getParameter("tableName"));
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			processError(e.getMessage());
			return null;
		}
	}

	/**
	 * Update redirection information
	 * 
	 * @return Action.SUCCESS
	 */
	public String update() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String redirectId = request.getParameter("redirectId");
			String start = request.getParameter("start");
			String redirectUrl = request.getParameter("redirectURl");
			String tableName = request.getParameter("tableName");
			String end = null;

			if (tableName.equals(WhoisUtil.IP)
					|| tableName.equals(WhoisUtil.AUTNUM))
				end = request.getParameter("end");

			RedirectionService redirectService = RedirectionService
					.getRedirectService();
			redirectService.updateRedirect(start, end, redirectUrl,
					Integer.parseInt(redirectId), tableName);
			request.setAttribute("tableName", request.getParameter("tableName"));
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			processError(e.getMessage());
			return null;
		}
	}

	/**
	 * Delete redirection information
	 * 
	 * @return Action.SUCCESS
	 */
	public String delete() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String tableName = request.getParameter("tableName");

			RedirectionService redirectService = RedirectionService
					.getRedirectService();

			redirectService.deleteRedirect(
					Integer.parseInt(request.getParameter("id")), tableName);
			request.setAttribute("tableName", request.getParameter("tableName"));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			this.addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * Add or update the field, an exception occurs if the field already exists
	 * or other abnormal call the method.
	 * 
	 * @param errorMessage
	 */
	private void processError(String errorMessage) {
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.sendError(417);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
