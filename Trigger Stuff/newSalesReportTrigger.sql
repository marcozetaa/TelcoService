CREATE TRIGGER newSalesReport
AFTER INSERT ON packages
FOR EACH ROW
INSERT INTO salesReport
SET PackageName = NEW.name, id_package = NEW.id;