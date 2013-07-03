package com.cnnic.whois.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.cnnic.whois.service.AccessControlService;
import com.cnnic.whois.service.ExColumnService;
import com.opensymphony.xwork2.ActionSupport;

public class AccessControlAction extends ActionSupport {
	private String tableType;
	private String anonymous;
	private String authenticated;
	private String root;

	public AccessControlAction() {
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
	 * To assignment of the anonymous property
	 * 
	 * @param anonymous
	 */
	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}

	/**
	 * To assignment of the authenticated property
	 * 
	 * @param authenticated
	 */
	public void setAuthenticated(String authenticated) {
		this.authenticated = authenticated;
	}

	/**
	 * To assignment of the root property
	 * 
	 * @param root
	 */
	public void setRoot(String root) {
		this.root = root;
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
	
	public String list() {
		try {
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			return INPUT;
		}
	}
	
	/**
	 * This method is called when the update permissions content
	 * 
	 * @return Action.SUCCESS
	 */
	public String update() {
		try {

			AccessControlService accessControlService = AccessControlService
					.getAccessControlService();
			String[] anonymousArray = anonymous.split(",");
			String[] authenticatedArray = authenticated.split(","); //get the parameters from the request scope
			String[] rootArray = root.split(",");

			accessControlService.updatePermission(tableType, anonymousArray,
					authenticatedArray, rootArray);
			HttpServletRequest request = ServletActionContext.getRequest();

			request.setAttribute("successFlag", "UpdateSucess");

			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			return null;
		}
	}
}