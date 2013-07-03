package com.cnnic.whois.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.cnnic.whois.service.ExColumnService;
import com.cnnic.whois.util.WhoisUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ExColumnAction extends ActionSupport {
	private String tableType;

	public ExColumnAction() {
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
	 * Listed in the extension field of information
	 * 
	 * @return Action.SUCCESS
	 */
	public String list() {
		try {
			ExColumnService columnService = ExColumnService.getColumnService();
			Map<String, String> columnInfoList = columnService
					.listCoulumn(tableType);

			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("columnInfoList", columnInfoList);
			request.setAttribute("tableType", tableType);
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			return INPUT;
		}
	}

	/**
	 * Add the extension field
	 * 
	 * @return Action.SUCCESS
	 */
	public String add() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String column = request.getParameter("columnArray");
			String[] columnArray = column.split(",");
			Map<String, String> columnMap = new HashMap<String, String>();
			for (String columnInfo : columnArray) {
				String[] value = columnInfo.split("'~'");
				columnMap.put(WhoisUtil.EXTENDPRX + value[0], value[1]);
			}

			ExColumnService columnService = ExColumnService.getColumnService();
			columnService.addCoulumn(tableType, columnMap);
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			processError(e.getMessage());
			return null;
		}
	}

	/**
	 * Update the extension field
	 * 
	 * @return Action.SUCCESS
	 */
	public String update() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String oldColumnName = request.getParameter("oldColumnName");
			String newCloumnName = request.getParameter("newCoulumnName");
			String columnLength = request.getParameter("coulumnLength");

			ExColumnService columnService = ExColumnService.getColumnService();
			columnService.updateCoulumn(tableType, oldColumnName,
					newCloumnName, columnLength);
			return SUCCESS;
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			processError(e.getMessage());
			return null;
		}
	}

	/**
	 * Delete the extension field
	 * 
	 * @return Action.SUCCESS
	 */
	public String delete() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			ExColumnService columnService = ExColumnService.getColumnService();
			columnService.deleteCoulumn(tableType,
					request.getParameter("coName"));
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
