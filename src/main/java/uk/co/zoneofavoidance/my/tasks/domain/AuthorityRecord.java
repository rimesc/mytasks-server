package uk.co.zoneofavoidance.my.tasks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class AuthorityRecord {

   @Id
   @Column(length = 50, nullable = false)
   private String authority;

   @ManyToOne
   @JoinColumn(name = "username", nullable = false, updatable = false)
   private UserRecord user;

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
