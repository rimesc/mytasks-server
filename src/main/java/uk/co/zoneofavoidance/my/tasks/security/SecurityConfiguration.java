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
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

   /** Admin role. */
   public static final String ADMIN_ROLE = "ROLE_ADMIN";

   @Autowired
   private DataSource dataSource;

   @Autowired
   private UserDetailsManager userDetailsManager;

   @Override
   protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder());
   }

   @Bean
   public UserDetailsManager userDetailsManager() {
      final JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
      userDetailsManager.setDataSource(dataSource);
      return userDetailsManager;
   }

   @Override
   protected void configure(final HttpSecurity http) throws Exception {
      http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
   }

   @Override
   public void configure(final WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/webjars/**", "/error/**", "/css/**", "/images/**", "/js/**", "/db/**");
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

}
