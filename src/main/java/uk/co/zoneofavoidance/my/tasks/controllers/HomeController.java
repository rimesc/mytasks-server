package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class HomeController {

   @RequestMapping(method = GET)
   public String get() {
      return "home";
   }

}
