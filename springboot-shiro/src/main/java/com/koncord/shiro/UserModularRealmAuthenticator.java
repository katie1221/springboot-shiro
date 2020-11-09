package com.koncord.shiro;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

/**
 * 当配置了多个Realm时，我们通常使用的 认证器 是shiro自带的org.apache.shiro.authc.pam.ModularRealmAuthenticator，
 * 其中决定使用的Realm的是doAuthenticate()方法。
 * 
 * 自定义Authenticator
 * 注意，当需要分别定义处理 普通用户和系统管理员验证的Realm时，对应Realm的全类名应该包含字符串“Member”或者“User”。
 * 并且，他们不能相互包含，例如 处理 普通用户的Realm的全类名中不应该包含字符串“User”
 * 
 * @author Administrator
 *
 */
public class UserModularRealmAuthenticator extends ModularRealmAuthenticator{

	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		//判断getRealms()是否返回为空
		assertRealmsConfigured();
		//强制转换回自定义的UserToken
		UserToken userToken = (UserToken) authenticationToken;
		//获取登录类型
		String loginType = userToken.getLoginType();
		
		//所有Realm
        Collection<Realm> realms = getRealms();
        
        //登录类型对应的所有Realm
        Collection<Realm> loginTypeRealms = new ArrayList<Realm>();
        for(Realm realm : realms){
        	if(realm.getName().contains(loginType)){
        		loginTypeRealms.add(realm);
        	}
        }
        //判断是 单Realm 还是 多Realm
        if (loginTypeRealms.size() == 1) {
        	System.out.println("doSingleRealmAuthentication() 执行");
            return doSingleRealmAuthentication(loginTypeRealms.iterator().next(), userToken);
        } else {
        	System.out.println("doMultiRealmAuthentication() 执行");
            return doMultiRealmAuthentication(loginTypeRealms, userToken);
        }
	}
}
