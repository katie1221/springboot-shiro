package com.koncord.mapper;

import com.koncord.model.User;

public interface UserMapper {

	/**
	 * 根据用户名获取用户信息
	 */
	public User findUserByName(String name);
}
