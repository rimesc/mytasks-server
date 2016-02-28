package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.OPEN;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.services.TaskService;

@Controller
@RequestMapping(path = "projects")
public class ProjectsController {

   private final ProjectRepository projects;
   private final TaskService tasks;

   @Autowired
   public ProjectsController(final ProjectRepository projects, final TaskService tasks) {
      this.projects = projects;
      this.tasks = tasks;
   }

   @RequestMapping(method = GET)
   public ModelAndView getProjects() {
      final ModelAndView modelAndView = new ModelAndView("projects/list");
      modelAndView.addObject("projects", projects.findAll());
      return modelAndView;
   }

   @RequestMapping(path = "{projectId}", method = GET)
   public ModelAndView getProject(@PathVariable final long projectId) {
      final ModelAndView modelAndView = new ModelAndView("projects/view");
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      modelAndView.addObject("project", project);
      return modelAndView;
   }

   @RequestMapping(path = "new", method = GET)
   public ModelAndView getNewProject(final ProjectForm projectForm) {
      final ModelAndView modelAndView = new ModelAndView("projects/new");
      modelAndView.addObject("onCancel", "/projects");
      return modelAndView;
   }

   @RequestMapping(path = "new", method = POST)
   public ModelAndView postNewProject(@Valid final ProjectForm projectForm, final BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("projects/new");
         modelAndView.addObject("projectForm", projectForm);
         modelAndView.addObject("onCancel", "/projects");
         return modelAndView;
      }
      final Project project = projects.save(new Project(projectForm.getName(), projectForm.getDescription()));
      return new ModelAndView("redirect:/projects/" + project.getId());
   }

   @RequestMapping(path = "edit/{projectId}", method = GET)
   public ModelAndView getEditProject(@PathVariable("projectId") final Long projectId) {
      final ModelAndView modelAndView = new ModelAndView("projects/edit");
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      final ProjectForm projectForm = new ProjectForm(project.getName(), project.getDescription());
      modelAndView.addObject("projectForm", projectForm);
      modelAndView.addObject("onCancel", "/projects/" + projectId);
      return modelAndView;
   }

   @RequestMapping(path = "edit/{projectId}", method = POST)
   public ModelAndView postEditProject(@PathVariable("projectId") final Long projectId, @Valid final ProjectForm projectForm, final BindingResult bindingResult) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("projects/edit");
         modelAndView.addObject("projectForm", projectForm);
         modelAndView.addObject("onCancel", "/projects/" + projectId);
         return modelAndView;
      }
      project.setName(projectForm.getName());
      project.setDescription(projectForm.getDescription());
      projects.save(project);
      return new ModelAndView("redirect:/projects/" + projectId);
   }

   @RequestMapping(path = "delete/{projectId}", method = GET)
   public ModelAndView getDeleteProject(@PathVariable("projectId") final Long projectId) {
      final ModelAndView modelAndView = new ModelAndView("projects/delete");
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      modelAndView.addObject("project", project);
      return modelAndView;
   }

   @RequestMapping(path = "delete/{projectId}", method = POST)
   public ModelAndView postDeleteProject(@PathVariable("projectId") final Long projectId) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      projects.delete(project);
      return new ModelAndView("redirect:/projects");
   }

   @RequestMapping(path = "edit/{projectId}/documentation", method = GET)
   public ModelAndView getEditDocument(@PathVariable("projectId") final Long projectId) {
      final ModelAndView modelAndView = new ModelAndView("projects/edit-documentation");
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
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
      if (project.getReadMe() == null) {
         project.setReadMe(new Note(text));
      }
      else {
         project.getReadMe().setText(text);
      }
      projects.save(project);
      return new ModelAndView("redirect:/projects/" + projectId);
   }

   @RequestMapping(path = "{projectId}/tasks", method = GET)
   public ModelAndView getTasks(@PathVariable("projectId") final Long projectId, @RequestParam(defaultValue = "open") final TaskSelection select) {
      final Project project = projects.findOne(projectId);
      if (project == null) {
         throw new NotFoundException("project");
      }
      final List<Task> projectTasks = tasks.getForProject(projectId, select.getStates());
      final ModelAndView modelAndView = new ModelAndView("projects/tasks");
      modelAndView.addObject("project", project);
      modelAndView.addObject("selection", select);
      modelAndView.addObject("tasks", projectTasks);
      return modelAndView;
   }

   @InitBinder
   public void initBinder(final WebDataBinder dataBinder) {
      dataBinder.registerCustomEditor(TaskSelection.class, new EnumConverter<>(TaskSelection.class, OPEN));
   }

}
