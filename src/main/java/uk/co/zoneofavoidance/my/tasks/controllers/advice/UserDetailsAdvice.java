package uk.co.zoneofavoidance.my.tasks.controllers.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Adds the active user to the model exposed to views.
 */
@ControllerAdvice
public class UserDetailsAdvice {

   @ModelAttribute
   public User userDetails(@AuthenticationPrincipal final User user) {
      return user;
   }

}
