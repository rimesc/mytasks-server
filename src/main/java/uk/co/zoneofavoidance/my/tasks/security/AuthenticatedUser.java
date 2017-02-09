package uk.co.zoneofavoidance.my.tasks.security;

/**
 * Information on the authenticated user.
 */
public interface AuthenticatedUser {

   /**
    * The global user. Resources belonging to this user are visible to everyone.
    */
   String GLOBAL_USER = "global|0123456789abcdef01234567";

   /**
    * @return a unique ID for the authenticated user
    */
   String getId();

}
