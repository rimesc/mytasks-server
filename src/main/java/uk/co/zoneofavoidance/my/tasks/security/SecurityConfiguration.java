package uk.co.zoneofavoidance.my.tasks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;

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

   @Autowired
   private Delegate delegate;

   @Override
   protected void authorizeRequests(final HttpSecurity http) throws Exception {
      delegate.authorizeRequests(http);
   }

   /**
    * Pluggable delegate for different profiles.
    */
   private interface Delegate {
      void authorizeRequests(final HttpSecurity http) throws Exception;
   }

   /**
    * Production configuration.
    */
   @Component
   @Profile("!dev")
   public static class Production implements Delegate {
      @Override
      public void authorizeRequests(final HttpSecurity http) throws Exception {
         http.authorizeRequests().anyRequest().authenticated();
      }
   }

   /**
    * Development configuration.
    */
   @Component
   @Profile("dev")
   public static class Development extends Production {
      @Override
      public void authorizeRequests(final HttpSecurity http) throws Exception {
         super.authorizeRequests(http.authorizeRequests().antMatchers("/api/dev/**").permitAll().and());
      }
   }

}
