package uk.co.zoneofavoidance.my.tasks.services;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.zoneofavoidance.my.tasks.domain.Tag;
import uk.co.zoneofavoidance.my.tasks.repositories.TagRepository;

/**
 * Service that provides transactional access to tags.
 */
@Service
@Transactional
public class TagService {

   private final TagRepository tags;

   @Autowired
   public TagService(final TagRepository tags) {
      this.tags = tags;
   }

   /**
    * Retrieves an existing tag by name, or creates one if none exist.
    *
    * @param name name of the tag
    * @return task with the given name
    */
   @Transactional(readOnly = true)
   public Tag get(final String name) {
      return Optional.ofNullable(tags.findByName(name)).orElse(Tag.create(name));
   }

   /**
    * @return a list of all currently used tags, ordered lexicographically by
    *         name.
    */
   public List<Tag> getAll() {
      return tags.findUsed().stream().sorted((a, b) -> a.getName().compareTo(b.getName())).collect(toList());
   }

}
