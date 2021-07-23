package com.softtek.assetworx_api.config;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditConfig implements AuditorAware<String> {
	
	@Autowired
	HttpServletRequest request;
	
	
	@Override
	public Optional<String> getCurrentAuditor() {
		String threadName =  Thread.currentThread().getName();
		System.out.println("ThreadName:" + threadName);
		if(threadName.startsWith("BULK_UPLOAD") || threadName.startsWith("scheduling")) {
			return Optional.of("");
		}
		return Optional.of(request.getHeader("username"));
	}

}
