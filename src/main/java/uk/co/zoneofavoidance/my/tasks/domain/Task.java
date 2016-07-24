package uk.co.zoneofavoidance.my.tasks.domain;

import static javax.persistence.EnumType.STRING;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Domain object representing a task.
 */
@Entity
public class Task {

   @Id
   @GeneratedValue
   private Long id;

   @Column(nullable = false)
   @Length(max = 255)
   @NotBlank
   private String summary;

   @Lob
   private String description;

   @Column(nullable = false)
   @Enumerated(STRING)
   @NotNull
   private Priority priority = NORMAL;

   @Column(nullable = false)
   @Enumerated(STRING)
   @NotNull
   private State state = TO_DO;

   @ManyToOne(optional = false)
   private Project project;

   @CreationTimestamp
   private Date created;

   @UpdateTimestamp
   private Date updated;

   @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
   @JoinTable(name = "TaskTag", joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
   private Set<Tag> tags;

   /**
    * Creates a new task.
    *
    * @param project parent project
    * @param summary brief summary of the task
    * @param description detailed description of the task
    * @param priority initial priority of the task
    * @param state tags for the task
    * @param tags initial state of the task
    * @return a new task
    */
   public static Task create(final Project project, final String summary, final String description, final Priority priority, final Set<Tag> tags, final State state) {
      final Task task = create(project, summary, description, priority, tags);
      task.setState(state);
      return task;
   }

   /**
    * Creates a new task in the default state ({@link State#TO_DO}).
    *
    * @param project parent project
    * @param summary brief summary of the task
    * @param description detailed description of the task
    * @param priority initial priority of the task
    * @param tags tags for the task
    * @return a new task
    */
   public static Task create(final Project project, final String summary, final String description, final Priority priority, final Set<Tag> tags) {
      final Task task = new Task();
      task.setProject(project);
      task.setSummary(summary);
      task.setDescription(description);
      task.setPriority(priority);
      task.setTags(tags);
      return task;
   }

   /**
    * @return the ID of this task
    */
   public Long getId() {
      return id;
   }

   /**
    * @return a summary of this task
    */
   public String getSummary() {
      return summary;
   }

   /**
    * @return a description of this task
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return the priority of this task
    */
   public Priority getPriority() {
      return priority;
   }

   /**
    * @return the state of this task
    */
   public State getState() {
      return state;
   }

   /**
    * @return the parent project of this task
    */
   public Project getProject() {
      return project;
   }

   /**
    * @return the date when this task was created
    */
   public Date getCreated() {
      return created;
   }

   /**
    * @return the date when this task was last modified
    */
   public Date getUpdated() {
      return updated;
   }

   /**
    * @return the set of tags associated with this task
    */
   public Set<Tag> getTags() {
      return tags;
   }

   /**
    * Sets the ID of this task.
    *
    * @param id task ID
    */
   public void setId(final Long id) {
      this.id = id;
   }

   /**
    * Sets a summary for this task.
    *
    * @param summary new summary
    */
   public void setSummary(final String summary) {
      this.summary = summary;
   }

   /**
    * Sets a description for this task.
    *
    * @param description new description
    */
   public void setDescription(final String description) {
      this.description = description;
   }

   /**
    * Sets the priority of this task.
    *
    * @param priority new priority
    */
   public void setPriority(final Priority priority) {
      this.priority = priority;
   }

   /**
    * Sets the state of this task.
    *
    * @param state new state
    */
   public void setState(final State state) {
      this.state = state;
   }

   /**
    * Sets the parent project of this task.
    *
    * @param project parent project
    */
   public void setProject(final Project project) {
      this.project = project;
   }

   /**
    * Sets the date when this task was created.
    *
    * @param created date when the task was created
    */
   public void setCreated(final Date created) {
      this.created = created;
   }

   /**
    * Sets the date when this task was last modified.
    *
    * @param updated created date when the task was last modified
    */
   public void setUpdated(final Date updated) {
      this.updated = updated;
   }

   /**
    * Sets the tags associated with this task.
    *
    * @param tags set of tags
    */
   public void setTags(final Set<Tag> tags) {
      this.tags = tags;
   }

   /**
    * @return <code>true</code> if this task has been updated since creation,
    *         otherwise <code>false</code>
    */
   public boolean isModified() {
      return getUpdated() != null && getCreated() != null && getUpdated().after(getCreated());
   }

   @Override
   public String toString() {
      return String.format("Task %d: '%s'", id, summary);
   }
}
