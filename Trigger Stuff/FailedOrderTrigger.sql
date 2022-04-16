CREATE TRIGGER FailedOrder
AFTER UPDATE ON telco_service_db.Order 
FOR EACH ROW
UPDATE user 
SET user.status = 1,  
user.num_failed_payments = user.num_failed_payments + 1
WHERE user.ID = NEW.customer_id AND NEW.valid = 1;