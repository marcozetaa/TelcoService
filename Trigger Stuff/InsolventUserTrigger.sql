CREATE TRIGGER InsolventUser
AFTER UPDATE ON telco_service_db.Order
FOR EACH ROW
FOLLOWS FailedOrder
INSERT INTO Alert (ID_Client, Username, Email, TotalAmount, DateOfCreation, HourOfCreation)
SELECT u.ID, u.username, u.email, o.total_value, o.date_of_creation, o.hour_of_creation FROM user AS u JOIN telco_service_db.Order AS o on u.ID = o.customer_id
WHERE u.num_failed_payments > 2 AND o.id = NEW.id;