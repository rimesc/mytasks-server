package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.services.TaskService;

@Controller
@RequestMapping(path = "tasks")
public class TasksController {

   private final ProjectRepository projects;
   private final TaskService tasks;

   @Autowired
   public TasksController(final ProjectRepository projects, final TaskService tasks) {
      super();
      this.projects = projects;
      this.tasks = tasks;
   }

   @RequestMapping(path = "new", method = GET)
   public ModelAndView getNewTask(final TaskForm taskForm) {
      final Project project = projects.findOne(taskForm.getProject());
      if (project == null) {
         throw new NotFoundException("project");
      }
      final ModelAndView modelAndView = new ModelAndView("tasks/new");
      modelAndView.addObject("priorities", Priority.values());
      return modelAndView;
   }

   @RequestMapping(path = "new", method = POST)
   public ModelAndView postNewTask(@Valid final TaskForm taskForm, final BindingResult bindingResult) {
      final Project project = projects.findOne(taskForm.getProject());
      if (project == null) {
         throw new NotFoundException("project");
      }
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("tasks/new");
         modelAndView.addObject("project", project);
         modelAndView.addObject("taskForm", taskForm);
         modelAndView.addObject("priorities", Priority.values());
         return modelAndView;
      }
      final Task task = tasks.save(Task.create(project, taskForm.getSummary(), taskForm.getDescription(), taskForm.getPriority()));
      return new ModelAndView("redirect:/tasks/" + task.getId());
   }

   @RequestMapping(path = "{taskId}", method = GET)
   public ModelAndView getTask(@PathVariable final long taskId) {
      final ModelAndView modelAndView = new ModelAndView("tasks/view");
      modelAndView.addObject("task", tasks.get(taskId));
      return modelAndView;
   }

   @RequestMapping(path = "{taskId}", method = POST)
   public ModelAndView postTask(@PathVariable final long taskId, @RequestParam("state") final State state) {
      final ModelAndView modelAndView = new ModelAndView("tasks/view");
      final Task task = tasks.get(taskId);
      task.setState(state);
      tasks.save(task);
      modelAndView.addObject("task", task);
      return modelAndView;
   }

   @RequestMapping(path = "edit/{taskId}", method = GET)
   public ModelAndView getEditTask(@PathVariable final long taskId) {
      final ModelAndView modelAndView = new ModelAndView("tasks/edit");
      final Task task = tasks.get(taskId);
      final TaskForm taskForm = new TaskForm(task.getProject().getId(), task.getSummary(), task.getDescription(), task.getPriority());
      modelAndView.addObject("taskForm", taskForm);
      modelAndView.addObject("priorities", Priority.values());
      return modelAndView;
   }

   @RequestMapping(path = "edit/{taskId}", method = POST)
   public ModelAndView postEditTask(@PathVariable final long taskId, @Valid final TaskForm taskForm, final BindingResult bindingResult) {
      final Task task = tasks.get(taskId);
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("tasks/edit");
         modelAndView.addObject("taskForm", taskForm);
         modelAndView.addObject("priorities", Priority.values());
         return modelAndView;
      }
      task.setSummary(taskForm.getSummary());
      task.setDescription(taskForm.getDescription());
      task.setPriority(taskForm.getPriority());
      tasks.save(task);
      return new ModelAndView("redirect:/tasks/" + taskId);
   }

   @RequestMapping(path = "delete/{taskId}", method = GET)
   public ModelAndView getDeleteTask(@PathVariable("taskId") final Long taskId) {
      final ModelAndView modelAndView = new ModelAndView("tasks/delete");
      final Task task = tasks.get(taskId);
      modelAndView.addObject("task", task);
      return modelAndView;
   }

   @RequestMapping(path = "delete/{taskId}", method = POST)
   public ModelAndView postDeleteTask(@PathVariable("taskId") final Long taskId) {
      final Task task = tasks.get(taskId);
      tasks.delete(task);
      return new ModelAndView("redirect:/projects/" + task.getProject().getId());
   }

}
