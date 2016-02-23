package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.services.UserService;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

   @Autowired
   private UserService users;

   @RequestMapping(method = GET)
   public ModelAndView getProjects() {
      final ModelAndView modelAndView = new ModelAndView("admin/console");
      modelAndView.addObject("users", users.getUsers());
      return modelAndView;
   }

}
