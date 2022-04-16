CREATE TRIGGER updateSalesReport
AFTER INSERT ON subscription
FOR EACH ROW
UPDATE salesReport SET TotalPurchase = TotalPurchase + 1, TotalValue = TotalValue + NEW.fee
WHERE id_package = NEW.id_package;
    
CREATE TRIGGER updateSalesReport12
AFTER INSERT ON subscription
FOR EACH ROW
FOLLOWS updateSalesReport
UPDATE salesReport SET TotalFor12 = TotalFor12 + 1,
NetValue = NetValue + (SELECT fee12 FROM packages WHERE id = NEW.id_package)
WHERE id_package = NEW.id_package AND NEW.validity_period = 12;

CREATE TRIGGER updateSalesReport24
AFTER INSERT ON subscription
FOR EACH ROW
FOLLOWS updateSalesReport12
UPDATE salesReport SET TotalFor24 = TotalFor24 + 1,
NetValue = NetValue + (SELECT fee24 FROM packages WHERE id = NEW.id_package)
WHERE id_package = NEW.id_package AND NEW.validity_period = 24;

CREATE TRIGGER updateSalesReport36
AFTER INSERT ON subscription
FOR EACH ROW
FOLLOWS updateSalesReport24
UPDATE salesReport SET TotalFor36 = TotalFor36 + 1,
NetValue = NetValue + (SELECT fee36 FROM packages WHERE id = NEW.id_package)
WHERE id_package = NEW.id_package AND NEW.validity_period = 36;

 