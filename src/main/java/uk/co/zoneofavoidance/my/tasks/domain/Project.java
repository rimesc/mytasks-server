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

   @OneToOne(fetch = LAZY, optional = true, cascade = ALL)
   private Note readMe;

   public Project() {
   }

   public Project(final String name, final String description) {
      this.name = name;
      this.description = description;
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public Date getCreated() {
      return created;
   }

   public int getNumberOfTasks() {
      return numberOfTasks;
   }

   public Note getReadMe() {
      return readMe;
   }

   public void setId(final Long id) {
      this.id = id;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

   public void setCreated(final Date created) {
      this.created = created;
   }

   public void setReadMe(final Note readMe) {
      this.readMe = readMe;
   }

}
