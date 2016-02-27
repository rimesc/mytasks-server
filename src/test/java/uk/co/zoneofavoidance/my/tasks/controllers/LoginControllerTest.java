package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static uk.co.zoneofavoidance.my.tasks.testing.ModelAndViewAssertExtras.assertModelAttributeNotAvailable;
import static uk.co.zoneofavoidance.my.tasks.testing.ModelAndViewAssertExtras.assertRedirect;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit tests for {@link LoginController}.
 */
public class LoginControllerTest {

   @Rule
   public final MockitoRule mockitoRule = MockitoJUnit.rule();

   @Mock
   private Authentication authentication;

   private final LoginController controller = new LoginController();

   @Test
   public void getLoginReturnsLoginViewIfNoPrincipal() {
      final ModelAndView modelAndView = controller.getLogin(null, null, null);
      assertViewName(modelAndView, "login");
      assertModelAttributeNotAvailable(modelAndView, "success");
      assertModelAttributeNotAvailable(modelAndView, "error");
   }

   @Test
   public void getLoginReturnsLoginViewIfNotAuthenticated() {
      when(authentication.isAuthenticated()).thenReturn(false);
      final ModelAndView modelAndView = controller.getLogin(null, null, authentication);
      assertViewName(modelAndView, "login");
      assertModelAttributeNotAvailable(modelAndView, "success");
      assertModelAttributeNotAvailable(modelAndView, "error");
   }

   @Test
   public void getLoginRedirectsToRootIfAlreadyAuthenticated() {
      when(authentication.isAuthenticated()).thenReturn(true);
      assertRedirect(controller.getLogin(null, null, authentication), "/");
   }

   @Test
   public void getLoginReturnsLoginViewWithSuccessMessageOnLogout() {
      final ModelAndView modelAndView = controller.getLogin("", null, null);
      assertViewName(modelAndView, "login");
      assertModelAttributeValue(modelAndView, "success", "logout");
      assertModelAttributeNotAvailable(modelAndView, "error");
   }

   @Test
   public void getLoginReturnsLoginViewWithErrorMessageOnFailedLogin() {
      final ModelAndView modelAndView = controller.getLogin(null, "", null);
      assertViewName(modelAndView, "login");
      assertModelAttributeValue(modelAndView, "error", "error.invalid.credentials");
      assertModelAttributeNotAvailable(modelAndView, "success");
   }

}
