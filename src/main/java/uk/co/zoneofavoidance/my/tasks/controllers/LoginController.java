package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

   @RequestMapping(path = "/login", method = GET)
   public ModelAndView getLogin(@RequestParam(required = false) final String logout, @RequestParam(required = false) final String error, final Principal principal) {
      if (principal instanceof Authentication && ((Authentication) principal).isAuthenticated()) {
         return new ModelAndView("redirect:/");
      }
      ;
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
