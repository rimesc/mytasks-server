package uk.co.zoneofavoidance.my.tasks.request;

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

   /**
    * @return the name of the user
    */
   public String getUsername() {
      return username;
   }

   /**
    * @return the password for the user
    */
   public String getPassword() {
      return password;
   }

   /**
    * @return a list of authorities for the user
    */
   public List<String> getAuthorities() {
      return authorities;
   }

   /**
    * @param username name of the user
    */
   public void setUsername(final String username) {
      this.username = username;
   }

   /**
    * @param password password for the user
    */
   public void setPassword(final String password) {
      this.password = password;
   }

   /**
    * @param authorities list of authorities to give the user
    */
   public void setAuthorities(final List<String> authorities) {
      this.authorities = authorities;
   }

}
