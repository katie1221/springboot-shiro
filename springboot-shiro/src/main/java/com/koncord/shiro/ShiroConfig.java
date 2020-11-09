package com.koncord.shiro;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * Shiro配置类
 * @author Administrator
 *
 */
@Configuration
public class ShiroConfig {

	/**
	 * 3.创建ShiroFilterFactoryBean
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean =new ShiroFilterFactoryBean();
		
		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		//添加Shiro内置过滤器
		/**
		 * Shiro内置过滤器，可以实现权限相关的拦截
		 *    常用的过滤器：
		 *       anon:无需认证（登录）可以访问
		 *       authc:必须认证才可以访问
		 *       user:如果使用rememberMe的功能可以直接访问
		 *       perms:该资源必须得到资源权限才可以访问
		 *       role:该资源必须得到角色权限才可以访问
		 */
		Map<String, String> filterMap = new LinkedHashMap<String, String>();
//		filterMap.put("/add", "authc");
//		filterMap.put("/update", "authc");
		filterMap.put("/testThymeleaf", "anon");
		filterMap.put("/login", "anon");
		
		//授权过滤器
		//注意：当前授权拦截后，shiro会自动跳转到未授权页面
		filterMap.put("/add", "perms[user:add]");
		filterMap.put("/update", "perms[user:update]");
		
		filterMap.put("/*", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		
		//设置登录跳转链接
		shiroFilterFactoryBean.setLoginUrl("/toLogin");
		//设置未授权提示页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");
		return shiroFilterFactoryBean;
	}
	
	/**
	 * 2.创建 DefaultWebSecurityManager
	 */
	@Bean(name="securityManager")
	public DefaultWebSecurityManager securityManager(@Qualifier("userRealm")UserRealm userRealm){
		DefaultWebSecurityManager securityManager =new DefaultWebSecurityManager();
//		//关联realm
//		securityManager.setRealm(userRealm);
		//设置realm（需要在realm定义之前）
		securityManager.setAuthenticator(modularRealmAuthenticator());
		//添加多个realms
		List<Realm> realms =new ArrayList<Realm>();
		realms.add(userRealm);
		realms.add(memberRealm());
//		realms.add(secondRealm());
		securityManager.setRealms(realms);
		return securityManager;
	}
	
	/**
	 * 1.创建Realm
	 */
	@Bean
	public UserRealm userRealm(){
		UserRealm userRealm = new UserRealm();
		//设置 凭证匹配器
		userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return userRealm;
	}
	
	/**
	 * 创建Realm:普通用户
	 * @return
	 */
	@Bean
	public MemberRealm memberRealm(){
		MemberRealm memberRealm = new MemberRealm();
		//设置 凭证匹配器
		memberRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return memberRealm;
	}
	
	/**
	 * 创建第二个Realm
	 */
	@Bean
	public SecondRealm secondRealm(){
		SecondRealm secondRealm = new SecondRealm();
		//设置 凭证匹配器
		secondRealm.setCredentialsMatcher(hashedCredentialsMatcherSHA());
		return secondRealm;
	}
	
	/**
	 * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
	 */
	@Bean
	public ShiroDialect getShiroDialect(){
		return new ShiroDialect();
	}
	
	/**
	 * 配置 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了,
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码;）
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher(){
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		//设置加密算法
		hashedCredentialsMatcher.setHashAlgorithmName("MD5");
		//设置加密次数，比如两次，相当于md5(md5())
		hashedCredentialsMatcher.setHashIterations(1024);
		return hashedCredentialsMatcher;
	}
	
	/**
	 * 配置 凭证匹配器 ,加密算法为 SHA1
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcherSHA(){
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		//设置加密算法
		hashedCredentialsMatcher.setHashAlgorithmName("SHA1");
		//设置加密次数，比如两次，相当于SHA1(SHA1())
		hashedCredentialsMatcher.setHashIterations(1024);
		return hashedCredentialsMatcher;
	}
	
	/**
	 * 系统自带的Realm管理，主要针对多realm
	 */
	@Bean
	public ModularRealmAuthenticator modularRealmAuthenticator(){
		//自己重写的ModularRealmAuthenticator
		UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
		//设置认证策略
		modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
//		ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
//		//设置认证策略
//		modularRealmAuthenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
		return modularRealmAuthenticator;
	}
}
