package com.koncord.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koncord.shiro.UserToken;

@Controller
public class UserController {

	/**
	 * 测试
	 */
	@RequestMapping("/hello")
	@ResponseBody
	public String  hello(){
		System.out.println("UserController-hello");
		return "ok";
	}
	
	/**
	 * 测试 thymeleaf
	 */
	@RequestMapping("/testThymeleaf")
	public String  testThymeleaf(Model model){
		//将数据存入model
		model.addAttribute("name", "程序猿");
		//返回test.html
		return "test";
	}
	
	/**
	 * 进入添加页面
	 */
	@RequestMapping("/add")
	public String  add(){
		return "user/add";
	}
	
	/**
	 * 进入编辑页面
	 */
	@RequestMapping("/update")
	public String  update(){
		return "user/update";
	}
	
	/**
	 * 进入查看页面
	 */
	@RequestMapping("/list")
	public String  list(){
		return "user/list";
	}
	
	/**
	 * 进入登录页面
	 */
	@RequestMapping("/toLogin")
	public String  toLogin(){
		return "login";
	}
	
	/**
	 * 登录逻辑处理
	 * @param userName:用户名
	 * @param password:密码
	 * @param loginType:登录用户类型
	 */
	@RequestMapping("/login")
	public String login(String userName,String password,String loginType,Model model){
		/**
		 * 使用Shiro编写认证操作
		 */
		//1.获取Subject
		Subject subject=SecurityUtils.getSubject();
		//2.封装用户数据（把用户名和密码封装为UsernamePasswordToken对象）
//		UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
		UserToken token = new UserToken(userName, password, loginType);
		try {
			//3.执行登录方法
			subject.login(token);
			//只要没有异常，则登录成功；有异常则登录失败
			System.out.println("用户["+userName+"]登录成功");
			model.addAttribute("userName", userName);
			//登录成功，跳转test.html
			return "test";
//			return "redirect:/testThymeleaf";//重定向请求
		} 
		//若没有指定的账户，则shiro会抛出UnknownAccountException 异常
		catch (UnknownAccountException e) {
			//登录失败
			model.addAttribute("msg", "用户不存在");
			return "login";//返回页面链接
		} 
		//若账户存在，密码不匹配，则shiro会抛出IncorrectCredentialsException 异常
		catch (IncorrectCredentialsException e) {
			//登录失败
			model.addAttribute("msg", "密码错误");
			return "login";
		}
		/*//所有认证时异常的父类
		catch (AuthenticationException e) {
			return "login";
		}*/
	}
	
	/**
	 * 进入未授权页面
	 */
	@RequestMapping("/noAuth")
	public String  noAuth(){
		return "noAuth";
	}
}
