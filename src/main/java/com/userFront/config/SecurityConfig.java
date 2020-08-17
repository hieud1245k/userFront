package com.userFront.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.userFront.service.ServiceImpl.UserSecurityService;

@Configuration //  xac dinh lop WebSecurityconfig cua ta la mot lop dung de cau hinh
@EnableWebSecurity // kich hoat viec tich hop Spring Security voi Spring MVC
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private Environment env;

	@Autowired
	private UserSecurityService userSecurityService;

	private static final String SALT = "salt"; // salt be protected carefully

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}

	private static final String[] PUBLIC_MATCHERS = { "/webjars/**", "/css/**", "/js/**", "/images/**", "/",
			"/about/**", "/contact/**", "/error/**/**", "/console/**", "/signup" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().
//			antMatchers().
			antMatchers(PUBLIC_MATCHERS).
			permitAll().anyRequest().authenticated();
		
		http
			.csrf().disable().cors().disable()
			.formLogin().failureUrl("/index?error").defaultSuccessUrl("/userFront").loginPage("/index").permitAll()
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("logout")).logoutSuccessUrl("/index").deleteCookies("remember-me").permitAll()
			.and()
			.rememberMe();
			
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}
}
