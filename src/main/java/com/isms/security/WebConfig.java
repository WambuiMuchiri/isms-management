package com.isms.security;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.isms.interceprtors.VisitorLoggerHandler;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private VisitorLoggerHandler visitorLogger;
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
	     registry.addConverter(new MulitpartConverter());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		Path profileUploadDir = Paths.get("./profile-pictures");
		Path profileUploadDir = Paths.get("/var/spring/isms-app/profile-pictures");
		String profileUploadPath = profileUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/profile-pictures/**").addResourceLocations("file:/"+profileUploadPath+"/");
		
//		Path scannedUploadDir = Paths.get("./scanned-documents");
		Path scannedUploadDir = Paths.get("/var/spring/isms-app/scanned-documents");
		String scannedUploadPath = scannedUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/scanned-documents/**").addResourceLocations("file:/"+scannedUploadPath+"/");

//		Path proofUploadDir = Paths.get("./payment-proofs");
		Path proofUploadDir = Paths.get("/var/spring/isms-app/payment-proofs");
		String proofUploadPath = proofUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/payment-proofs/**").addResourceLocations("file:/"+proofUploadPath+"/");

//		Path userUploadDir = Paths.get("./user-logos");
		Path userUploadDir = Paths.get("/var/spring/isms-app/user-logos");
		String userUploadPath = userUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/user-logos/**").addResourceLocations("file:/"+userUploadPath+"/");
		
//		Path personnelUploadDir = Paths.get("./personnel-pictures");
		Path personnelUploadDir = Paths.get("/var/spring/isms-app/personnel-pictures");
		String personnelUploadPath = personnelUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/personnel-pictures/**").addResourceLocations("file:/"+personnelUploadPath+"/");
		
//		Path personnelScannedUploadDir = Paths.get("./personnel-scanned-documents");
		Path personnelScannedUploadDir = Paths.get("/var/spring/isms-app/personnel-scanned-documents");
		String personnelScannedUploadPath = personnelScannedUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/personnel-scanned-documents/**").addResourceLocations("file:/"+personnelScannedUploadPath+"/");

//		Path equipmentUploadDir = Paths.get("./equipment-pictures");
		Path equipmentUploadDir = Paths.get("/var/spring/isms-app/equipment-pictures");
		String equipmentUploadPath = equipmentUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/equipment-pictures/**").addResourceLocations("file:/"+equipmentUploadPath+"/");
		
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	        String pathPattern1 = "/**.js";
	        String pathPattern2 = "/**.css";
	        String pathPattern3 = "/profile-pictures/**";
	        String pathPattern4 = "/plugins/**";
	        String pathPattern5 = "/dist/**";
	        String pathPattern6 = "/**/index";
	        String pathPattern7 = "/scanned-documents/**";
	        String pathPattern8 = "/payment-proofs/**";
	        String pathPattern9 = "/user-logos/**";
	        String pathPattern10 = "/personnel-pictures/**";
	        String pathPattern11 = "/personnel-scanned-documents/**";
	        String pathPattern12 = "/equipment-pictures/**";
		registry.addInterceptor(visitorLogger).excludePathPatterns(pathPattern1, pathPattern2, pathPattern3, pathPattern4, pathPattern5, pathPattern6, pathPattern7, pathPattern8, pathPattern9, pathPattern10, pathPattern11, pathPattern12);
	}
	
	 
}
