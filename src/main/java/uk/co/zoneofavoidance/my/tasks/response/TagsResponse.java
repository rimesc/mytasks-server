package uk.co.zoneofavoidance.my.tasks.response;

import java.util.List;

public class TagsResponse {

   private final List<String> tags;

   public TagsResponse(final List<String> tags) {
      this.tags = tags;
   }

   public List<String> getTags() {
      return tags;
   }

}
