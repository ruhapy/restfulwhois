package com.cnnic.whois.bean;

/**
 * authenticate bean ,store user auth info
 * 
 * @author nic
 * 
 */
public class Authentication {
	private String role;// user role:authenticated,administrator,root

	/**
	 * contruction
	 * @param role
	 */
	public Authentication(String role) {
		super();
		this.role = role;
	}

	/**
	 * get user role
	 * @return
	 */
	public String getRole() {
		return role;
	}

	/**
	 * set user role
	 * @param role:usr role
	 */
	public void setRole(String role) {
		this.role = role;
	}
}
