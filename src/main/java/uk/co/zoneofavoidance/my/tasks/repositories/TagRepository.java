package uk.co.zoneofavoidance.my.tasks.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uk.co.zoneofavoidance.my.tasks.domain.Tag;

/**
 * Repository for accessing tags.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

   /**
    * Find all tags currently in use.
    *
    * @return a list of tags
    */
   @Query("SELECT tag FROM uk.co.zoneofavoidance.my.tasks.domain.Task task JOIN task.tags tag")
   Set<Tag> findUsed();

   /**
    * Find a tag by name.
    *
    * @param name name of the tag
    * @return the tag with the given name, or <code>null</code>
    */
   Tag findByName(String name);

}
