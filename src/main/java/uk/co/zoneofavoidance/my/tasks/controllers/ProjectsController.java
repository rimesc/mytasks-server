package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
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
      final ModelAndView modelAndView = new ModelAndView("projects/list");
      modelAndView.addObject("projects", projects.findAll());
      return modelAndView;
   }

   @RequestMapping(path = "new", method = GET)
   public ModelAndView getNewProject(final ProjectForm projectForm) {
      final ModelAndView modelAndView = new ModelAndView("projects/new");
      return modelAndView;
   }

   @RequestMapping(path = "new", method = POST)
   public ModelAndView postNewProject(@Valid final ProjectForm projectForm, final BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("projects/new");
         modelAndView.addObject("name", projectForm.getName());
         modelAndView.addObject("description", projectForm.getDescription());
         return modelAndView;
      }
      final Project project = projects.save(new Project(projectForm.getName(), projectForm.getDescription()));
      return new ModelAndView("redirect:/projects/" + project.getId());
   }

   @RequestMapping(path = "{projectId}", method = GET)
   public ModelAndView getProject(@PathVariable final long projectId) {
      final ModelAndView modelAndView = new ModelAndView("projects/view");
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      modelAndView.addObject("project", project);
      modelAndView.addObject("tasks", tasks.findByProjectId(projectId));
      return modelAndView;
   }

   @RequestMapping(path = "edit/{projectId}/documentation", method = GET)
   public ModelAndView getEditDocument(@PathVariable("projectId") final Long projectId) {
      final ModelAndView modelAndView = new ModelAndView("projects/edit-documentation");
      final Project project = projects.findOne(projectId);
      modelAndView.addObject("project", project);
      final Note readMe = project.getReadMe();
      modelAndView.addObject("text", readMe == null ? "" : readMe.getText());
      return modelAndView;
   }

   @RequestMapping(path = "edit/{projectId}/documentation", method = POST)
   public ModelAndView postEditDocument(@PathVariable("projectId") final Long projectId, @RequestParam("text") final String text) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      project.getReadMe().setText(text);
      projects.save(project);
      return new ModelAndView("redirect:/projects/" + projectId);
   }

   @RequestMapping(path = "{projectId}/tasks", method = GET)
   public ModelAndView getTasks(@PathVariable("projectId") final Long projectId) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      final List<Task> projectTasks = tasks.findByProjectId(projectId);
      final ModelAndView modelAndView = new ModelAndView("projects/tasks");
      modelAndView.addObject("project", project);
      modelAndView.addObject("tasks", projectTasks);
      return modelAndView;
   }

}
