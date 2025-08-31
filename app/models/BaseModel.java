package models;

import io.ebean.Model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseModel extends Model {

    public LocalDateTime createdOn;

    public LocalDateTime updatedOn;

    public LocalDateTime deletedOn;

    public String createdBy;

    public String updatedBy;

    public String deletedBy;
}
