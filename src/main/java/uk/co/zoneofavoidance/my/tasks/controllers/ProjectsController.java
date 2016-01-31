package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@Controller
@RequestMapping(path = "projects")
public class ProjectsController {

   @Autowired
   private ProjectRepository projects;

   @Autowired
   private TaskRepository tasks;

   @RequestMapping(method = GET)
   public ModelAndView getProjects() {
      final ModelAndView modelAndView = new ModelAndView("projects");
      modelAndView.addObject("projects", projects.findAll());
      return modelAndView;
   }

   @RequestMapping(path = "new", method = GET)
   public ModelAndView getNewProject(final ProjectForm projectForm) {
      final ModelAndView modelAndView = new ModelAndView("new_project");
      return modelAndView;
   }

   @RequestMapping(path = "new", method = POST)
   public ModelAndView postNewProject(@Valid final ProjectForm projectForm, final BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("new_project");
         modelAndView.addObject("name", projectForm.getName());
         modelAndView.addObject("description", projectForm.getDescription());
         return modelAndView;
      }
      final Project project = projects.save(new Project(projectForm.getName(), projectForm.getDescription()));
      return new ModelAndView("redirect:/projects/" + project.getId());
   }

   @RequestMapping(path = "{projectId}", method = GET)
   public ModelAndView getProject(@PathVariable final long projectId) {
      final ModelAndView modelAndView = new ModelAndView("project");
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      modelAndView.addObject("project", project);
      modelAndView.addObject("tasks", tasks.findByProjectId(projectId));
      return modelAndView;
   }

}
