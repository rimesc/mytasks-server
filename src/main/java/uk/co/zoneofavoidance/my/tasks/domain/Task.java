package uk.co.zoneofavoidance.my.tasks.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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

   @ManyToOne(fetch = LAZY, optional = false)
   private Project project;

   @CreationTimestamp
   private Date created;

   @UpdateTimestamp
   private Date updated;

   public Task() {
   }

   public Task(final Project project, final String summary, final String description, final Priority priority) {
      this.project = project;
      this.summary = summary;
      this.description = description;
      this.priority = priority;
   }

   public Task(final Project project, final String summary, final String description, final Priority priority, final State state) {
      this(project, summary, description, priority);
      this.state = state;
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

}
