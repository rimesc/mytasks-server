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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

   private final UserDetailsManager manager;
   private final UserRepository users;
   private final PasswordEncoder encoder;

   @Autowired
   public UserService(final UserDetailsManager manager, final UserRepository users, final PasswordEncoder encoder) {
      this.manager = manager;
      this.users = users;
      this.encoder = encoder;
   }

   /**
    * Returns a list of all registered users.
    *
    * @return list of users
    */
   public List<UserDetails> getUsers() {
      return users.findAll().stream().map(UserService::convert).collect(toList());
   }

   /**
    * Returns the number of registered users.
    *
    * @return number of users
    */
   public long size() {
      return users.count();
   }

   /**
    * Checks if a user exists with the given username.
    *
    * @param username username to check
    * @return <code>true</code> if the named user exists, otherwise
    *         <code>false</code>.
    */
   public boolean exists(final String username) {
      return manager.userExists(username);
   }

   /**
    * Gets the full details of the user with the given name. Note that the
    * user's password is not decoded.
    *
    * @param username name of the user for which to retrieve details
    * @return the details of the named user
    * @throws UsernameNotFoundException if no user exists with the given name
    */
   public UserDetails getUser(final String username) {
      return manager.loadUserByUsername(username);
   }

   /**
    * Creates a new user with the given name and password.
    *
    * @param username name of the user to create
    * @param password password for the new user
    * @param admin whether the new user should have admin privileges
    * @throws UsernameAlreadyExistsException if a user already exists with the
    *            given name
    */
   public void createUser(final String username, final String password, final boolean admin) {
      if (exists(username)) {
         throw new UsernameAlreadyExistsException(username);
      }
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
