package uk.co.zoneofavoidance.my.tasks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Domain object representing a tag that can be attached to a task.
 */
@Entity
public class Tag {

   @Id
   @GeneratedValue
   private Long id;

   @Column(unique = true, nullable = false)
   @Length(max = 255)
   @NotBlank
   private String name;

   /**
    * Creates a new tag.
    *
    * @param name name of the tag
    * @return a new tag
    */
   public static Tag create(final String name) {
      final Tag tag = new Tag();
      tag.setName(name);
      return tag;
   }

   /**
    * @return ID of this tag
    */
   public Long getId() {
      return id;
   }

   /**
    * @return name of this tag
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the ID of this tag.
    *
    * @param id tag ID
    */
   public void setId(final Long id) {
      this.id = id;
   }

   /**
    * Sets the name of this tag.
    *
    * @param name new name
    */
   public void setName(final String name) {
      this.name = name;
   }

}
