package com.koncord;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot 启动类
 * @author Administrator
 *
 */
@SpringBootApplication
@MapperScan("com.koncord.mapper")
public class Application {

	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}

}
