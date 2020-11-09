package com.koncord.mapper;

import com.koncord.model.Member;

public interface MemberMapper {

	/**
	 * 根据用户名获取用户信息
	 */
	public Member findMemberByName(String name);
}
