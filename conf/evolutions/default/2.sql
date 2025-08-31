# --- Sample dataset

# --- !Ups

insert into app_user
  (id,email,password,name,created_on,updated_on,deleted_on,created_by,updated_by,deleted_by)
values
  (1,'admin@invalid.com','FFFFFF','Admin',now(),null,null,'system',null,null)
, (2,'user1@invalid.com','FFFFFF','User1',now(),null,null,'system',null,null)
, (3,'user1@invalid.com','FFFFFF','User2',now(),null,null,'system',null,null)
;

insert into task
  (id,title,description,status,owner_id,due_date,priority,parent_task_id,created_on,updated_on,deleted_on,created_by,updated_by,deleted_by)
values
  (1,'title 1','description 1','Open',1,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (2,'title 2','description 2','WIP',2,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (3,'title 3','description 3','Done',3,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (4,'title 4','description 4','Cancel',1,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (5,'title 5','description 5','Open',2,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (6,'title 6','description 6','WIP',3,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (7,'title 7','description 7','Done',1,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (8,'title 8','description 8','Cancel',2,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (9,'title 9','description 9','Open',3,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (10,'title 10','description 10','WIP',1,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
, (11,'title 11','description 11','Donw',2,'2099-12-31 23:59:59',1,null,null,null,null,'system',null,null)
;

# --- !Downs

delete from app_user;
delete from task;
