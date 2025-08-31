package models;

import io.ebean.Model;
import io.ebean.Finder;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity 
public class AppUser extends BaseModel {

  @Id
  public Long id;

  public String email;

  public String password;

  public String name;

  public static Finder<Long, AppUser> find = new Finder<>(AppUser.class);
}
