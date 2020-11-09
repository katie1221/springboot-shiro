package com.koncord.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koncord.mapper.MemberMapper;
import com.koncord.model.Member;
import com.koncord.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{

	//注入Mapper接口
	@Autowired
	private MemberMapper memberMapper;
	
	/**
	 * 根据用户名获取用户信息
	 */	
	@Override
	public Member findMemberByName(String name) {
		return memberMapper.findMemberByName(name);
	}

}
