package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;

@Controller
public class MyTasksController {

   private final ProjectRepository projects;

   @Autowired
   public MyTasksController(final ProjectRepository projects) {
      this.projects = projects;
   }

   @RequestMapping(path = "/", method = GET)
   public ModelAndView getHome() {
      final ModelAndView modelAndView = new ModelAndView("home");
      modelAndView.addObject("projects", projects.findAll());
      return modelAndView;
   }

}
