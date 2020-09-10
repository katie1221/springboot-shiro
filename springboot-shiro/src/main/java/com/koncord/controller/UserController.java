package com.koncord.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	 * 进入登录页面
	 */
	@RequestMapping("/toLogin")
	public String  toLogin(){
		return "login";
	}
	
	/**
	 * 登录逻辑处理
	 */
	@RequestMapping("/login")
	public String login(String userName,String password,Model model){
		/**
		 * 使用Shiro编写认证操作
		 */
		//1.获取Subject
		Subject subject=SecurityUtils.getSubject();
		//2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
		try {
			//3.执行登录方法
			subject.login(token);
			//只要没有异常，则登录成功；有异常则登录失败
			
			//登录成功，跳转test.html
			return "redirect:/testThymeleaf";//重定向请求
		} catch (UnknownAccountException e) {
			//登录失败
			model.addAttribute("msg", "用户不存在");
			return "login";//返回页面链接
		} catch (IncorrectCredentialsException e) {
			//登录失败
			model.addAttribute("msg", "密码错误");
			return "login";
		}
	}
	
	/**
	 * 进入未授权页面
	 */
	@RequestMapping("/noAuth")
	public String  noAuth(){
		return "noAuth";
	}
}
