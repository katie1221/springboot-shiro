package com.koncord.service;

import com.koncord.model.Member;

public interface MemberService {

	/**
	 * 根据用户名获取用户信息
	 */
	public Member findMemberByName(String name);
}
