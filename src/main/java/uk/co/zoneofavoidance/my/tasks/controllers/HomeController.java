package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.co.zoneofavoidance.my.tasks.repositories.TaskRepository;

@Controller
@RequestMapping(path = "/")
public class HomeController {

   @Autowired
   private TaskRepository tasks;

   @RequestMapping(method = GET)
   public ModelAndView get() {
      return new ModelAndView("home", "tasks", tasks.findAll());
   }

}
