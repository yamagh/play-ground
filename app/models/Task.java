package models;

import io.ebean.Finder;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends BaseModel {

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
