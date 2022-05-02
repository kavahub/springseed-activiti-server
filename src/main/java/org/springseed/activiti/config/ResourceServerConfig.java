package org.springseed.activiti.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * 资源服务器配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
  
	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
		.and()
		.csrf().disable()
          .authorizeRequests()
            .antMatchers("/*")
              .hasAnyAuthority("ROLE_activiti_user", "ROLE_activiti_admin", "SCOPE_activiti_user", "SCOPE_activiti_admin")            
            .anyRequest()
              .authenticated()
		.and()
			.oauth2ResourceServer()
				.jwt();
	}
	// @formatter:on

	@Bean
	JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
		return NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri()).build();
	}  
}
