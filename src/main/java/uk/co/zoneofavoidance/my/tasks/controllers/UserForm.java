package uk.co.zoneofavoidance.my.tasks.controllers;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Bean backing the new user form.
 */
public class UserForm {

   @NotBlank
   private String username;

   @NotBlank
   private String password;

   private boolean admin;

   public UserForm() {
   }

   public UserForm(final String username, final String password, final boolean admin) {
      this.username = username;
      this.password = password;
      this.admin = admin;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public boolean isAdmin() {
      return admin;
   }

   public void setUsername(final String username) {
      this.username = username;
   }

   public void setPassword(final String password) {
      this.password = password;
   }

   public void setAdmin(final boolean admin) {
      this.admin = admin;
   }

}
