package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.ProjectNotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@Controller
@RequestMapping(path = "tasks")
public class TasksController {

   @Autowired
   private ProjectRepository projects;

   @Autowired
   private TaskRepository tasks;

   @RequestMapping(path = "new", method = GET)
   public ModelAndView getNewTask(final TaskForm taskForm) {
      final Project project = projects.findOne(taskForm.getProject());
      if (project == null) {
         throw new ProjectNotFoundException();
      }
      final ModelAndView modelAndView = new ModelAndView("new_task");
      modelAndView.addObject("project", project);
      modelAndView.addObject("priorities", Priority.values());
      return modelAndView;
   }

   @RequestMapping(path = "new", method = POST)
   public ModelAndView postNewTask(@Valid final TaskForm taskForm, final BindingResult bindingResult) {
      final Project project = projects.findOne(taskForm.getProject());
      if (project == null) {
         throw new ProjectNotFoundException();
      }
      if (bindingResult.hasErrors()) {
         final ModelAndView modelAndView = new ModelAndView("new_task");
         modelAndView.addObject("project", project);
         modelAndView.addObject("summary", taskForm.getSummary());
         modelAndView.addObject("description", taskForm.getDescription());
         modelAndView.addObject("priority", taskForm.getPriority());
         modelAndView.addObject("priorities", Priority.values());
         return modelAndView;
      }
      tasks.save(new Task(project, taskForm.getSummary(), taskForm.getDescription(), taskForm.getPriority()));
      return new ModelAndView("redirect:/projects/" + project.getId());
   }

   // @RequestMapping(path = "{taskId}", method = GET)
   // public ModelAndView getProject(@PathVariable final long projectId) {
   // final ModelAndView modelAndView = new ModelAndView("project");
   // final Project project = projects.findOne(projectId);
   // if (project == null) {
   // throw new ProjectNotFoundException();
   // }
   // modelAndView.addObject("project", project);
   // modelAndView.addObject("tasks", tasks.findByProjectId(projectId));
   // return modelAndView;
   // }

}
