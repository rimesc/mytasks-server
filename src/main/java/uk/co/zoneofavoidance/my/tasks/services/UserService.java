package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static uk.co.zoneofavoidance.my.tasks.security.SecurityConfiguration.ADMIN_ROLE;
import static uk.co.zoneofavoidance.my.tasks.security.SecurityConfiguration.USER_ROLE;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableSet;

import uk.co.zoneofavoidance.my.tasks.domain.AuthorityRecord;
import uk.co.zoneofavoidance.my.tasks.domain.UserRecord;
import uk.co.zoneofavoidance.my.tasks.repositories.UserRepository;

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
    * @return details of the new user
    * @throws UsernameAlreadyExistsException if a user already exists with the
    *            given name
    */
   public UserDetails createUser(final String username, final String password, final boolean admin) {
      if (exists(username)) {
         throw new UsernameAlreadyExistsException(username);
      }
      final Collection<GrantedAuthority> authorities = getRoles(admin).stream().map(SimpleGrantedAuthority::new).collect(toSet());
      final User user = new User(username, encoder.encode(password), authorities);
      manager.createUser(user);
      return user;
   }

   private static Set<String> getRoles(final boolean admin) {
      if (admin) {
         return ImmutableSet.of(ADMIN_ROLE, USER_ROLE);
      }
      return singleton(USER_ROLE);
   }

   private static User convert(final UserRecord record) {
      final Collection<GrantedAuthority> authorities = record.getAuthorities().stream().map(UserService::convert).collect(toList());
      return new User(record.getUserName(), record.getPassword(), record.isEnabled(), true, true, true, authorities);
   }

   private static GrantedAuthority convert(final AuthorityRecord record) {
      return new SimpleGrantedAuthority(record.getAuthority());
   }

}
