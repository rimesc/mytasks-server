package uk.co.zoneofavoidance.my.tasks.rest.controllers;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.zoneofavoidance.my.tasks.domain.Tag;
import uk.co.zoneofavoidance.my.tasks.rest.response.TagsResponse;
import uk.co.zoneofavoidance.my.tasks.services.TagService;

/**
 * REST controller for the tags API.
 */
@RestController
@RequestMapping("/api/tags")
public class TagsRestController {

   private final TagService tags;

   @Autowired
   public TagsRestController(final TagService tags) {
      this.tags = tags;
   }

   /**
    * End-point for obtaining a list of in-use tags.
    *
    * @return a REST response containing a list of tags
    */
   @RequestMapping(method = GET, produces = "application/json")
   public TagsResponse getTags() {
      return new TagsResponse(tags.getAll().stream().map(Tag::getName).sorted().collect(toList()));
   }

}
