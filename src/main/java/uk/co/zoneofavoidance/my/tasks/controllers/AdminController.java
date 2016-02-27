package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.exceptions.AlreadyExistsException;
import uk.co.zoneofavoidance.my.tasks.services.UserService;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

   @Autowired
   private UserService users;

   @RequestMapping(method = GET)
   public ModelAndView getConsole() {
      return new ModelAndView("redirect:/admin/users");
   }

   @RequestMapping(path = "users", method = GET)
   public ModelAndView getUsers(final UserForm userForm) {
      final ModelAndView modelAndView = new ModelAndView("admin/users");
      modelAndView.addObject("users", users.getUsers());
      modelAndView.addObject("userForm", userForm);
      return modelAndView;
   }

   @RequestMapping(path = "users/new", method = POST)
   public ModelAndView postNewUser(@Valid final UserForm userForm, final BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("admin/users");
         modelAndView.addObject("users", users.getUsers());
         modelAndView.addObject("userForm", userForm);
         return modelAndView;
      }
      if (users.exists(userForm.getUsername())) {
         throw new AlreadyExistsException("user", userForm.getUsername());
      }
      users.createUser(userForm.getUsername(), userForm.getPassword(), userForm.isAdmin());
      final ModelAndView modelAndView = new ModelAndView("redirect:/admin/users");
      return modelAndView;
   }

}
