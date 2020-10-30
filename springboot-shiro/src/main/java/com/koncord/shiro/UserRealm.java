package com.koncord.shiro;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.koncord.model.User;
import com.koncord.service.UserService;

/**
 * 自定义 Realm
 * @author Administrator
 *
 */
public class UserRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;
	
	/**
	 * 执行授权逻辑
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("执行授权逻辑");
		//给资源进行授权
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//添加资源的授权字符串
		//info.addStringPermission("user:add");
		
		//导数据库查询当前登录用户的授权字符串
		//获取当前登录用户
		Subject subject=SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal(); 
		
		User sbUser=userService.findUserByName(user.getName());
		
		info.addStringPermission(sbUser.getPerms());
		return info;
	}

	/**
	 * 执行认证逻辑
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("执行认证逻辑");
		
		//编写shiro 判断逻辑，判断用户名和密码
		//1.判断用户名
		UsernamePasswordToken userToken=(UsernamePasswordToken) token;
		//根据用户名获取用户信息
		User user=userService.findUserByName(userToken.getUsername());
				
		if(user == null){
			//用户不存在
			return null;//shiro底层会抛出 UNknowAccountException
		}
		//2.判断密码     第一个参数:需要返回给 subject.login方法的数据   第二个参数：数据库密码       第三个参数：realm的名字
//		return new SimpleAuthenticationInfo(user, user.getPassword(), "");
		
		//根据用户的情况，来构建AuthenticationInfo对像并返回，通常使用的实现类是SimpleAuthenticationInfo
		//以下信息是从数据库获取的
		//1).principal:认证的实体类信息。可以是username，也可以是数据表对应的用户的体类对象
		Object principal=user;
		//2).credentials:密码（数据库获取的用户的密码）
		Object credentials=user.getPassword();
		//3).realmName：当前realm对象的name，调用父类的getName()方法即可
		String realmName=getName();
		//4).盐值
		ByteSource credentialsSalt = ByteSource.Util.bytes(user.getName());//盐值 要唯一
		return new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
	}

	public static void main(String[] args) {
		String hashAlgorithmName="MD5";//加密算法（与配置文件中的一致）
		String credentials="111111";//密码
		ByteSource salt=ByteSource.Util.bytes("admin");//盐值
//		ByteSource salt=null;//盐值
		int hashIterations=1024;//加密次数（与配置文件中的一致）
		System.out.println(new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations));
	}
}
