package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller that serves the login page.
 */
@Controller
public class LoginController {

   /**
    * Handler for the login page.
    *
    * @param logout request parameter set when the user has arrived here as a
    *           result of logging out
    * @param error request parameter set when the user has arrived here as a
    *           result of an authentication error
    * @param principal principal of the logged in user, if available
    * @return the login view, or a redirect to the main page if the user is
    *         already logged in
    */
   @RequestMapping(path = "/login", method = GET)
   public ModelAndView getLogin(@RequestParam(required = false) final String logout, @RequestParam(required = false) final String error, final Principal principal) {
      if (principal instanceof Authentication && ((Authentication) principal).isAuthenticated()) {
         return new ModelAndView("redirect:/");
      }
      final ModelAndView modelAndView = new ModelAndView("login");
      if (logout != null) {
         modelAndView.addObject("success", "logout");
      }
      else if (error != null) {
         modelAndView.addObject("error", "error.invalid.credentials");
      }
      return modelAndView;
   }

}
