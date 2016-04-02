package uk.co.zoneofavoidance.my.tasks.controllers;

import java.util.List;

import org.hibernate.validator.constraints.Length;

/**
 * Bean backing the new user form.
 */
public class UserForm {

   @Length(min = 2, max = 20)
   private String username;

   @Length(min = 8, max = 20)
   private String password;

   private List<String> authorities;

   public UserForm() {
   }

   public UserForm(final String username, final String password, final List<String> authorities) {
      this.username = username;
      this.password = password;
      this.authorities = authorities;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public List<String> getAuthorities() {
      return authorities;
   }

   public void setUsername(final String username) {
      this.username = username;
   }

   public void setPassword(final String password) {
      this.password = password;
   }

   public void setAuthorities(final List<String> authorities) {
      this.authorities = authorities;
   }

}
