package com.softtek.assetworx_api.config;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class ApplicationConfig {
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate( getClientHttpRequestFactory());
		return restTemplate;
	}
	
	@Bean
	@ConditionalOnEnabledResourceChain
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		return new ResourceUrlEncodingFilter();
	}
	
	@Bean(name="objectMapper")
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
		objectMapper.setTimeZone(TimeZone.getDefault());
		objectMapper.registerModule(new StringTrimModule());
		return objectMapper;
	}
	
	@Bean
	public TaskExecutor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
        executor.setMaxPoolSize(50);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        executor.setThreadNamePrefix("BULK_UPLOAD");
        return executor;
	}
	
	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() 
	{
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
		= new HttpComponentsClientHttpRequestFactory();

		clientHttpRequestFactory.setHttpClient(httpClient());
		clientHttpRequestFactory.setConnectTimeout(15000);
		return clientHttpRequestFactory;
	}

	private HttpClient httpClient() {
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "password"));
		HttpClient client = HttpClientBuilder
				.create()
				.setDefaultCredentialsProvider(credentialsProvider)
				.build();
		return client; 
	}
	

}
