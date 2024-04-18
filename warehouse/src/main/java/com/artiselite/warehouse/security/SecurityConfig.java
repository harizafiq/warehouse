package com.artiselite.warehouse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.artiselite.warehouse.services.MyUserDetailService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private MyUserDetailService myUserDetailsService;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
//        httpSecurity.csrf(Customizer.withDefaults())
//            .authorizeHttpRequests(authorize -> {
//            	authorize.anyRequest().permitAll();
////            	authorize.requestMatchers("/","/home","/products").permitAll();
////            	authorize.requestMatchers("/**").hasRole("MANAGER");
////            	authorize.anyRequest().authenticated();
//            }
//            )
//            .httpBasic(Customizer.withDefaults())
//            .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails manager = User.builder()
//				.username("hariz")
//				.password(passwordEncoder().encode("password"))
//				.roles("MANAGER")
//				.build();
//		UserDetails operator = User.builder()
//				.username("afiq")
//				.password(passwordEncoder().encode("password"))
//				.roles("OPERATOR")
//				.build();
//		return new InMemoryUserDetailsManager(manager, operator);
//	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return myUserDetailsService;
	}
	
	 @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
