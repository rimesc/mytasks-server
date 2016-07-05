package uk.co.zoneofavoidance.my.tasks.domain;

import static javax.persistence.EnumType.STRING;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

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
   @JoinTable(name = "TaskTag", joinColumns = @JoinColumn(name = "task", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "tag", referencedColumnName = "id"))
   private Set<Tag> tags;

   public static Task create(final Project project, final Long id, final String summary, final String description, final Priority priority, final State state) {
      final Task task = create(project, summary, description, priority, state);
      task.setId(id);
      return task;
   }

   public static Task create(final Project project, final String summary, final String description, final Priority priority, final State state) {
      final Task task = create(project, summary, description, priority);
      task.setState(state);
      return task;
   }

   public static Task create(final Project project, final String summary, final String description, final Priority priority) {
      final Task task = new Task();
      task.setProject(project);
      task.setSummary(summary);
      task.setDescription(description);
      task.setPriority(priority);
      return task;
   }

   public Long getId() {
      return id;
   }

   public String getSummary() {
      return summary;
   }

   public String getDescription() {
      return description;
   }

   public Priority getPriority() {
      return priority;
   }

   public State getState() {
      return state;
   }

   public Project getProject() {
      return project;
   }

   public Date getCreated() {
      return created;
   }

   public Date getUpdated() {
      return updated;
   }

   public Set<Tag> getTags() {
      return tags;
   }

   public void setId(final Long id) {
      this.id = id;
   }

   public void setSummary(final String summary) {
      this.summary = summary;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

   public void setPriority(final Priority priority) {
      this.priority = priority;
   }

   public void setState(final State state) {
      this.state = state;
   }

   public void setProject(final Project project) {
      this.project = project;
   }

   public void setCreated(final Date created) {
      this.created = created;
   }

   public void setUpdated(final Date updated) {
      this.updated = updated;
   }

   public boolean isModified() {
      return getUpdated() != null && getCreated() != null && getUpdated().after(getCreated());
   }

   public void setTags(final Set<Tag> tags) {
      this.tags = tags;
   }

   @Override
   public String toString() {
      return String.format("Task %d: '%s'", id, summary);
   }
}
