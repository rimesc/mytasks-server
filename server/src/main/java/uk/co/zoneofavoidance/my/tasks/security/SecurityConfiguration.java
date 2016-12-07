package uk.co.zoneofavoidance.my.tasks.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.auth0.spring.security.api.Auth0SecurityConfig;

/**
 * Security configuration backed by <a href="https://auth0.com">Auth0</a>.
 */
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@ComponentScan(basePackageClasses = Auth0SecurityConfig.class)
public class SecurityConfiguration extends Auth0SecurityConfig {

   @Override
   protected void authorizeRequests(final HttpSecurity http) throws Exception {
      http.authorizeRequests().anyRequest().authenticated();
   }

}
