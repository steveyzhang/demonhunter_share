/**
 * 
 */
package com.dh.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author root
 *
 */
public class ApiDoAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Input Parameters
	 * */
	private String platform;
	
	@Override
	public String execute() throws Exception {
		try { 
			
			if( platform.equals("android") ) 
			{  
				return "res_android";
			} 
			else if( platform.equals("wp") )
			{
				return "res_wp";
			}
	
		} 
		catch (Exception e) 
		{ 
	
		} 
	
		// this shouldn't happen in this example 
		return null;
	}

	public String s() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	} 
}
