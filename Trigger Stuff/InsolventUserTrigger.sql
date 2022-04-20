CREATE TRIGGER FailedOrder
AFTER UPDATE ON telco_service_db.order 
FOR EACH ROW
UPDATE user 
SET user.status = 1,  
user.num_failed_payments = user.num_failed_payments + 1
WHERE user.ID = NEW.customer_id AND NEW.valid = 1;

CREATE TRIGGER InsolventUser
AFTER UPDATE ON telco_service_db.order
FOR EACH ROW
FOLLOWS FailedOrder
INSERT INTO Alert (ID_Client, Username, Email, TotalAmount, DateOfCreation, HourOfCreation)
SELECT u.ID, u.username, u.email, o.total_value, o.date_of_creation, o.hour_of_creation FROM user AS u JOIN telco_service_db.Order AS o on u.ID = o.customer_id
WHERE u.num_failed_payments > 2 AND o.id = NEW.id AND NEW.valid = 1;