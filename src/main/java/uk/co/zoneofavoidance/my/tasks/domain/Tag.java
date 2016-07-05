package uk.co.zoneofavoidance.my.tasks.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Tag {

   @Id
   @GeneratedValue
   private Long id;

   @Column(unique = true, nullable = false)
   @Length(max = 255)
   @NotBlank
   private String name;

   public static Tag create(final String name) {
      final Tag tag = new Tag();
      tag.setName(name);
      return tag;
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public void setId(final Long id) {
      this.id = id;
   }

   public void setName(final String name) {
      this.name = name;
   }

}
