package com.koncord.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koncord.mapper.UserMapper;
import com.koncord.model.User;
import com.koncord.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	//注入Mapper接口
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 根据用户名获取用户信息
	 */	
	@Override
	public User findUserByName(String name) {
		return userMapper.findUserByName(name);
	}

}
