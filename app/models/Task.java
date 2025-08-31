package models;

import io.ebean.Finder;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Task extends BaseModel {

  @Id
  public Long id;

  public String title;

  public String description;

  public String status;

  @ManyToOne
  @JoinColumn(name = "owner_id", insertable = false, updatable = false)
  public AppUser owner;

  public Long ownerId;

  public String dueDate;

  public Integer priority;

  public Long parentTaskId;

  public static Finder<Long, Task> find = new Finder<>(Task.class);
}
