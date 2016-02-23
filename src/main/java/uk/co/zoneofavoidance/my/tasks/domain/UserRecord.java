package uk.co.zoneofavoidance.my.tasks.domain;

import static javax.persistence.CascadeType.ALL;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
public class UserRecord {

   @Id
   @Column(name = "username", length = 50, nullable = false)
   @Length(min = 8, max = 50)
   private String userName;

   @Column(length = 60, nullable = false)
   private String password;

   @Column(nullable = false)
   boolean enabled;

   @OneToMany(mappedBy = "user", cascade = ALL)
   private Collection<AuthorityRecord> authorities;

   public String getUserName() {
      return userName;
   }

   public String getPassword() {
      return password;
   }

   public boolean isEnabled() {
      return enabled;
   }

   public Collection<AuthorityRecord> getAuthorities() {
      return authorities;
   }

   public void setUserName(final String userName) {
      this.userName = userName;
   }

   public void setPassword(final String password) {
      this.password = password;
   }

   public void setEnabled(final boolean enabled) {
      this.enabled = enabled;
   }

   public void setAuthorities(final Collection<AuthorityRecord> authorities) {
      this.authorities = authorities;
   }

}
