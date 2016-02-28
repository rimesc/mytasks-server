package uk.co.zoneofavoidance.my.tasks.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class AuthorityRecord implements Serializable {

   @Id
   @Column(length = 50, nullable = false)
   private String authority;

   @Id
   @ManyToOne
   @JoinColumn(name = "username", nullable = false, updatable = false)
   private UserRecord user;

   public AuthorityRecord(final UserRecord user, final String authority) {
      this.user = user;
      this.authority = authority;
   }

   public String getAuthority() {
      return authority;
   }

   public UserRecord getUser() {
      return user;
   }

   public void setAuthority(final String authority) {
      this.authority = authority;
   }

   public void setUser(final UserRecord user) {
      this.user = user;
   }

}
