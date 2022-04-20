mysql> delimiter //
create trigger solventUser
after update on telco_service_db.order
for each row
BEGIN
update user set num_failed_payments = num_failed_payments - 1
where ID = new.customer_id and old.valid = 1 and new.valid = 0;
update user set status = 0
where ID = new.customer_id and old.valid = 1 and new.valid = 0 and num_failed_payments = 0;
END;
//

create trigger removeAlert
after update on user
for each row
delete from Alert where ID_client = new.ID and new.num_failed_payments < 3 and old.num_failed_payments >= 3