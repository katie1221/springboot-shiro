package com.koncord.service;

import com.koncord.model.User;

public interface UserService {

	/**
	 * 根据用户名获取用户信息
	 */
	public User findUserByName(String name);
}
