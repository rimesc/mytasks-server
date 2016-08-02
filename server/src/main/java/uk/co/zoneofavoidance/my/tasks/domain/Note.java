package uk.co.zoneofavoidance.my.tasks.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * Domain object representing a note.
 */
@Entity
public class Note {

   @Id
   @GeneratedValue
   private Long id;

   @Lob
   private String text;

   /**
    * For JPA.
    */
   public Note() {
   }

   /**
    * Creates a new note.
    *
    * @param text text of the note
    */
   public Note(final String text) {
      this.text = text;
   }

   /**
    * @return the ID of this note
    */
   public Long getId() {
      return id;
   }

   /**
    * @return the text of this note
    */
   public String getText() {
      return text;
   }

   /**
    * Sets the ID of this note.
    *
    * @param id note ID
    */
   public void setId(final Long id) {
      this.id = id;
   }

   /**
    * Sets the text of this note.
    * 
    * @param text new text
    */
   public void setText(final String text) {
      this.text = text;
   }

}
