package com.DharleyEcommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.DharleyEcommerce.jwt.JwtFilter;

 
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired
    private JwtFilter jwtFilter;
	
	@Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
		
		http
        .csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/static/**/").permitAll()
        .antMatchers("/users").permitAll()
        .antMatchers("/authenticate").permitAll()
        .antMatchers("/isConnected").permitAll()
        .antMatchers("/category").permitAll()
        .antMatchers("/subcategory").permitAll()
        .antMatchers("/product").permitAll()
        .antMatchers("/productById{id}").permitAll()
        .antMatchers("/productBySubcat{id}").permitAll()
        .antMatchers("/selectSubCat{id}").permitAll()
        .antMatchers("/show/{email}").permitAll()
        
        .anyRequest().authenticated();
	http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	return http.build();
	}    
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

