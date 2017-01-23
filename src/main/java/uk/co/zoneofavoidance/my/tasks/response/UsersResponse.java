package uk.co.zoneofavoidance.my.tasks.response;

import java.util.List;

/**
 * Response body containing a list of users.
 */
public class UsersResponse {

   private final List<UserResponse> users;

   /**
    * @param users list of individual user responses
    */
   public UsersResponse(final List<UserResponse> users) {
      this.users = users;
   }

   /**
    * @return a list of individual user responses
    */
   public List<UserResponse> getUsers() {
      return users;
   }
}
