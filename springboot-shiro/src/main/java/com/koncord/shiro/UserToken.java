package com.koncord.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserToken extends UsernamePasswordToken{

	private static final long serialVersionUID = 1L;
	
	private String loginType;//登录类型 
	
	public UserToken(){};
	
	public UserToken(final String username,final String password,final String loginType){
		super(username,password);
		this.loginType = loginType;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
	
}
