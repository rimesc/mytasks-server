package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.exceptions.ProjectNotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@Controller
public class MyTasksController {

   @Autowired
   private TaskRepository tasks;

   @Autowired
   private ProjectRepository projects;

   @RequestMapping(path = "/", method = GET)
   public ModelAndView getHome() {
      final ModelAndView modelAndView = new ModelAndView("home");
      modelAndView.addObject("projects", projects.findAll());
      modelAndView.addObject("tasks", tasks.findAll());
      return modelAndView;
   }

   @RequestMapping(path = "/projects", method = GET)
   public ModelAndView getProjects() {
      final ModelAndView modelAndView = new ModelAndView("projects");
      modelAndView.addObject("projects", projects.findAll());
      return modelAndView;
   }

   @RequestMapping(path = "/projects/{projectId}", method = GET)
   public ModelAndView getProject(@PathVariable final long projectId) {
      final ModelAndView modelAndView = new ModelAndView("project");
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new ProjectNotFoundException();
      }
      modelAndView.addObject("project", project);
      return modelAndView;
   }

}
