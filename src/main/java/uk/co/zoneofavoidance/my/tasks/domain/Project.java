package uk.co.zoneofavoidance.my.tasks.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Domain object representing a project.
 */
@Entity
public class Project {

   @Id
   @GeneratedValue
   private Long id;

   @Column(unique = true, nullable = false)
   @Length(max = 255)
   @NotBlank
   private String name;

   @Lob
   private String description;

   @CreationTimestamp
   private Date created;

   @Formula("(select count(*) from Task t where t.project_id = id)")
   private int numberOfTasks;

   @Formula("(select count(*) from Task t where t.project_id = id and t.state != 'DONE')")
   private int numberOfOpenTasks;

   @OneToOne(fetch = LAZY, optional = true, cascade = ALL)
   private Note readMe;

   /**
    * Creates a new project.
    *
    * @param name project name
    * @param description brief description of the project
    * @return a new project
    */
   public static Project create(final String name, final String description) {
      final Project project = new Project();
      project.setName(name);
      project.setDescription(description);
      return project;
   }

   /**
    * @return the ID of this project
    */
   public Long getId() {
      return id;
   }

   /**
    * @return the name of this project
    */
   public String getName() {
      return name;
   }

   /**
    * @return a description of this project
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return the date when this project was created
    */
   public Date getCreated() {
      return created;
   }

   /**
    * @return the number of tasks belonging to this project
    */
   public int getNumberOfTasks() {
      return numberOfTasks;
   }

   /**
    * @return the number of open tasks (tasks with states other than
    *         {@link State#DONE} belonging to this project
    */
   public int getNumberOfOpenTasks() {
      return numberOfOpenTasks;
   }

   /**
    * @return the 'read me' for this project
    */
   public Note getReadMe() {
      return readMe;
   }

   /**
    * Sets the ID of this project.
    *
    * @param id project ID
    */
   public void setId(final Long id) {
      this.id = id;
   }

   /**
    * Sets the name of this project.
    *
    * @param name new name
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * Sets a brief description for this project.
    *
    * @param description new description
    */
   public void setDescription(final String description) {
      this.description = description;
   }

   /**
    * Sets the date when this project was created.
    *
    * @param created date when the project was created
    */
   public void setCreated(final Date created) {
      this.created = created;
   }

   /**
    * Sets the 'read me' for this project.
    *
    * @param readMe the new project 'read me'
    */
   public void setReadMe(final Note readMe) {
      this.readMe = readMe;
   }

   @Override
   public String toString() {
      return String.format("Task %d: '%s'", id, name);
   }

}
