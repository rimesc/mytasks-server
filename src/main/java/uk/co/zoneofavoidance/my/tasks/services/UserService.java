package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import uk.co.zoneofavoidance.my.tasks.domain.AuthorityRecord;
import uk.co.zoneofavoidance.my.tasks.domain.UserRecord;
import uk.co.zoneofavoidance.my.tasks.repositories.UserRepository;
import uk.co.zoneofavoidance.my.tasks.security.SecurityConfiguration;

/**
 * Provides access to the details of registered users.
 */
@Service
public class UserService {

   @Autowired
   private UserDetailsManager manager;

   @Autowired
   private UserRepository users;

   @Autowired
   private PasswordEncoder encoder;

   /**
    * Returns a list of all registered users.
    *
    * @return list of users
    */
   public List<UserDetails> getUsers() {
      return users.findAll().stream().map(UserService::convert).collect(toList());
   }

   public boolean exists(final String username) {
      return manager.userExists(username);
   }

   public UserDetails getUser(final String username) {
      return manager.loadUserByUsername(username);
   }

   public void createUser(final String username, final String password, final boolean admin) {
      final Collection<GrantedAuthority> authorities = admin ? singleton(new SimpleGrantedAuthority(SecurityConfiguration.ADMIN_ROLE)) : emptyList();
      manager.createUser(new User(username, encoder.encode(password), authorities));
   }

   private static User convert(final UserRecord record) {
      final Collection<GrantedAuthority> authorities = record.getAuthorities().stream().map(UserService::convert).collect(toList());
      return new User(record.getUserName(), record.getPassword(), record.isEnabled(), true, true, true, authorities);
   }

   private static GrantedAuthority convert(final AuthorityRecord record) {
      return new SimpleGrantedAuthority(record.getAuthority());
   }

}
