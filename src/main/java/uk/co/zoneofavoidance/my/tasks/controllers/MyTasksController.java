package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;

/**
 * Controller that serves the main view for the Angular JS front-end.
 */
@Controller
public class MyTasksController {

   private final ProjectRepository projects;

   @Autowired
   public MyTasksController(final ProjectRepository projects) {
      this.projects = projects;
   }

   /**
    * Handler for the root of the servlet context.
    * 
    * @return the main view
    */
   @RequestMapping(path = "/", method = GET)
   public ModelAndView getHome() {
      final ModelAndView modelAndView = new ModelAndView("home");
      modelAndView.addObject("projects", projects.findAll());
      return modelAndView;
   }

}
