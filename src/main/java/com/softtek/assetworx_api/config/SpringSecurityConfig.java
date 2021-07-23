package com.softtek.assetworx_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/employee/getByEmail/**").access("hasRole('ROLE_HIREF_USER') or hasRole('ROLE_admin')")
		.antMatchers("/**").access("hasRole('ROLE_admin')")
		.and()
		.httpBasic();
	}


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("user").password("{noop}password").roles("user") .and()
		.withUser("hiref-user").password("{noop}b6f01808-298a-47d2-a072-b1482b0dc465").roles("HIREF_USER") .and()
		.withUser("admin").password("{noop}password").roles("admin");

	}
}
