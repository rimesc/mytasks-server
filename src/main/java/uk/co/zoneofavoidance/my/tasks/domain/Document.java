package uk.co.zoneofavoidance.my.tasks.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Document {

   @Id
   @GeneratedValue
   private Long id;

   @Lob
   private String text;

   public Document() {
   }

   public Document(final String text) {
      this.text = text;
   }

   public Long getId() {
      return id;
   }

   public String getText() {
      return text;
   }

   public void setId(final Long id) {
      this.id = id;
   }

   public void setText(final String text) {
      this.text = text;
   }

}
