package com.training.springcore;

import com.training.springcore.config.BigCorpApplicationConfig;
import com.training.springcore.model.ApplicationInfo;
import com.training.springcore.service.SiteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BigcorpApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(BigcorpApplication.class, args);
		ApplicationInfo applicationInfo = context.getBean(ApplicationInfo.class);
		System.out.println("========================================================================");
		System.out.println("Application [" + applicationInfo.getName() + "] - version: " + applicationInfo.getVersion());
		System.out.println("plus d'informations sur " +
				applicationInfo.getWebSiteUrl());
		System.out.println("========================================================================");
		context.getBean(SiteService.class).findById("test");
	}


}
