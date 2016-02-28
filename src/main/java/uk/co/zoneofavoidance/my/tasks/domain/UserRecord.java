package uk.co.zoneofavoidance.my.tasks.domain;

import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
public class UserRecord implements Serializable {

   @Id
   @Column(name = "username", length = 50, nullable = false)
   @Length(max = 50)
   private String userName;

   @Column(length = 60, nullable = false)
   private String password;

   @Column(nullable = false)
   boolean enabled = true;

   @OneToMany(mappedBy = "user", cascade = ALL)
   private Collection<AuthorityRecord> authorities;

   public UserRecord() {
   }

   public UserRecord(final String username, final String password, final String... authorities) {
      this.userName = username;
      this.password = password;
      this.authorities = stream(authorities).map(a -> new AuthorityRecord(this, a)).collect(toList());
   }

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
      return authorities == null ? emptySet() : authorities;
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