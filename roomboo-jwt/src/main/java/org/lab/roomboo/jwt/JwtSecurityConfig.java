package org.lab.roomboo.jwt;

import org.lab.roomboo.jwt.JwtAuthenticationFilter;
import org.lab.roomboo.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

	private String[] SWAGGER_RESOURCES = new String[] { "/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**",
		"/webjars/**" };

	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		log.debug("Configuring JWT security");
		String authorizationPath = env.getProperty("app.env.jwt.authorization.path");
		AuthenticationManager authenticationManager = authenticationManager();

		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager(), env);
		authenticationFilter.setFilterProcessesUrl(authorizationPath);

		//@formatter:off
		httpSecurity 
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.cors()
				.and()
			.csrf()
				.disable()
			.authorizeRequests()
				.antMatchers(SWAGGER_RESOURCES).permitAll()
				.antMatchers(HttpMethod.POST, authorizationPath).permitAll()
				.anyRequest().authenticated()
				.and()
			.addFilter(authenticationFilter)
			.addFilter(new JwtAuthorizationFilter(authenticationManager, env));
		//@formatter:on
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.debug("Configuring AuthenticationManager");
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}