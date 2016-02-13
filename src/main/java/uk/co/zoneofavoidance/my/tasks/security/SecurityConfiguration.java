package uk.co.zoneofavoidance.my.tasks.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
// @Profile("!dev")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

   @Autowired
   private DataSource dataSource;

   @Override
   protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
      final JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
      userDetailsService.setDataSource(dataSource);
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
   }

   @Override
   protected void configure(final HttpSecurity http) throws Exception {
      http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().logout();
   }

   @Override
   public void configure(final WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/webjars/**", "/error/**", "/css/**", "/js/**", "/db/**");
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

}
