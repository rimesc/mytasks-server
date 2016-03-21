package uk.co.zoneofavoidance.my.tasks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

   @Autowired
   private UserDetailsManager userDetailsManager;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Override
   protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder);
   }

   @Override
   protected void configure(final HttpSecurity http) throws Exception {
      http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
   }

   @Override
   public void configure(final WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/webjars/**", "/error/**", "/css/**", "/images/**", "/js/**", "/db/**");
   }

   // For testing
   @Bean(name = "authenticationManager")
   @Profile("dev")
   @Override
   public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
   }
}
