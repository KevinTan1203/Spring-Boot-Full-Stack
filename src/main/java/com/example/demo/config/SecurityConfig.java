package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Add configuration for security, which marks a class for some aspect of Springboot
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	// A bean method returns a component that is used by Springboot.
	// Think of it as a factory method that springboot will use later, sometimes
	// internally to
	// create instances of a component that it needs.
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// the httpSecurity class allows us to define security rules
		http.authorizeHttpRequests(auth -> auth.requestMatchers(
				"/register", "/login", "/css/**", "/js/**" // All these urls can be accessed w/o login
		)
				.permitAll()
				.anyRequest()
				.authenticated())
				.formLogin(form -> form.loginPage("/login").permitAll())
				.logout(logout -> logout.permitAll());
		return http.build();
	}

}
