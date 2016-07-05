package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import uk.co.zoneofavoidance.my.tasks.domain.UserRecord;
import uk.co.zoneofavoidance.my.tasks.repositories.UserRepository;

/**
 * Unit tests for {@link UserService}.
 */
public class UserServiceTest {

   private static final UserRecord USER_FOO = new UserRecord("foo", "abcdefgh");
   private static final UserRecord USER_BAR = new UserRecord("bar", "ijklmnop", "ROLE_ADMIN", "ROLE_USER");

   private static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
   private static final SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

   @Mock
   private UserDetailsManager userDetailsManager;

   @Mock
   private UserRepository repository;

   @Mock
   private PasswordEncoder encoder;

   private UserService service;

   @Before
   public void setUp() throws Exception {
      service = new UserService(userDetailsManager, repository, encoder);
   }

   @Test
   @SuppressWarnings("unchecked")
   public void getUsersReturnsAllUsers() {
      when(repository.findAll()).thenReturn(asList(USER_FOO, USER_BAR));
      final List<UserDetails> users = service.getUsers();
      final UserDetails fooDetails = new User("foo", "abcdefgh", emptyList());
      final UserDetails barDetails = new User("bar", "ijklmnop", asList(ROLE_ADMIN, ROLE_USER));
      assertThat(users, containsInAnyOrder(sameDetails(fooDetails), sameDetails(barDetails)));
   }

   @Test
   public void getUserReturnsUser() {
      final String username = "foo";
      final UserDetails userDetails = new User(username, "abcdefgh", emptyList());
      when(userDetailsManager.loadUserByUsername(username)).thenReturn(userDetails);
      assertThat(service.getUser(username), sameDetails(userDetails));
   }

   @Test(expected = UsernameNotFoundException.class)
   public void getUserPropagatesErrorIfUserNotFound() {
      final String username = "foo";
      final UserDetails userDetails = new User(username, "abcdefgh", emptyList());
      when(userDetailsManager.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException(""));
      assertThat(service.getUser(username), sameDetails(userDetails));
   }

   @Test
   public void existsReturnsTrueIfUserExists() {
      final String username = "foo";
      when(userDetailsManager.userExists(username)).thenReturn(true);
      assertTrue(service.exists(username));
   }

   @Test
   public void existsReturnsFalseIfUserDoesNotExist() {
      final String username = "foo";
      when(userDetailsManager.userExists(username)).thenReturn(false);
      assertFalse(service.exists(username));
   }

   @Test
   public void createUserCreatesAdminUserWithEncodedPassword() {
      when(encoder.encode("abcdefgh")).thenReturn("ijklmnop");
      service.createUser("foo", "abcdefgh", true);
      final ArgumentCaptor<UserDetails> argument = ArgumentCaptor.forClass(UserDetails.class);
      verify(userDetailsManager).createUser(argument.capture());
      assertThat(argument.getValue(), sameDetails(new User("foo", "ijklmnop", asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")))));
   }

   @Test
   public void createUserCreatesNonAdminUserWithEncodedPassword() {
      when(encoder.encode("abcdefgh")).thenReturn("ijklmnop");
      service.createUser("foo", "abcdefgh", false);
      final ArgumentCaptor<UserDetails> argument = ArgumentCaptor.forClass(UserDetails.class);
      verify(userDetailsManager).createUser(argument.capture());
      assertThat(argument.getValue(), sameDetails(new User("foo", "ijklmnop", singleton(new SimpleGrantedAuthority("ROLE_USER")))));
   }

   @Test(expected = UsernameAlreadyExistsException.class)
   public void createUserThrowsIfUserAlreadyExists() {
      final String username = "foo";
      when(userDetailsManager.userExists(username)).thenReturn(true);
      service.createUser("foo", "abcdefgh", false);
   }

   /**
    * A matcher that matches when all of the following details are equal:
    * <ul>
    * <li>{@link UserDetails#getUsername() username}
    * <li>{@link UserDetails#getPassword() password}
    * <li>{@link UserDetails#getAuthorities() authorities}
    * <li>{@link UserDetails#isEnabled() enabled}
    * </ul>
    * In contrast, the equality of {@link UserDetails} implementations typically
    * only includes {@code username}.
    *
    * @param userDetails expected user details
    * @return a matcher
    */
   private Matcher<UserDetails> sameDetails(final UserDetails userDetails) {
      return new UserDetailsMatcher(userDetails);
   }

   private class UserDetailsMatcher extends BaseMatcher<UserDetails> {

      private final UserDetails expected;

      UserDetailsMatcher(final UserDetails expected) {
         this.expected = expected;
      }

      @Override
      public boolean matches(final Object item) {
         if (!(item instanceof UserDetails)) {
            return false;
         }
         return matchesInternal((UserDetails) item);
      }

      private boolean matchesInternal(final UserDetails userDetails) {
         return expected.getUsername().equals(userDetails.getUsername()) && expected.getPassword().equals(userDetails.getPassword()) && expected.getAuthorities().equals(userDetails.getAuthorities()) && expected.isEnabled() == userDetails.isEnabled();
      }

      @Override
      public void describeTo(final Description description) {
         description.appendValue(expected);
      }

   }

   @Rule
   public MockitoRule mockitoRule() {
      return MockitoJUnit.rule();
   }

}
