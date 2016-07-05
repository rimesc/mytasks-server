package uk.co.zoneofavoidance.my.tasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.Tag;

@Transactional
public interface TagRepository extends JpaRepository<Tag, Long> {

   @Query("SELECT tag FROM uk.co.zoneofavoidance.my.tasks.domain.Task task JOIN task.tags tag")
   List<Tag> findUsed();

   Tag findByName(String name);

}
