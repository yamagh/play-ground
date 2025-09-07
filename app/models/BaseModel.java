package models;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseModel extends Model {

    @Id
    @GeneratedValue
    public Long id;

    @WhenCreated
    public LocalDateTime createdOn;

    @WhenModified
    public LocalDateTime updatedOn;

    public LocalDateTime deletedOn;

    public String createdBy;

    public String updatedBy;

    public String deletedBy;

    @io.ebean.annotation.NotNull
    public Boolean isActive = true;
}
