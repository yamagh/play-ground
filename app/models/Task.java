package models;

import io.ebean.Model;
import io.ebean.Finder;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity 
public class Task extends BaseModel {

  @Id
  public Long id;

  public String title;

  public String description;

  public String status;

  public Long ownerId;

  public String dueDate;

  public Integer priority;

  public Long parentTaskId;

  public static Finder<Long, Task> find = new Finder<>(Task.class);
}
